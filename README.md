## 前言

看到诸如阮一峰等人拥有独立技术博客，从其中学到了许多的知识与技能。也因为看到国内诸多Ctrl-c、Crtl-v还不标出处的伪博客，甚是鄙夷。所以自立一个博客项目，既是逐渐打造符合自身要求的博客系统，也是为社区环境出一份小小的力，还是一个新的开始。


## 关于

该项目我的个人博客系统 alpaca-blog  的后端程序。启动于2018-09-20，于2018-10-12完成0.0.1初版。


## 系统特点
- 集成博文主要功能
- 使用SpringBoot作为基本架构
- restful风格Api
- Spring Security Oauth 登录
- Spring Data Jpa 持久化
- redis缓存


## 功能规划
* 博文（完成）
* 分类（完成）
* 系统信息（完成）
* 搜索（正在实现）
* 标签（规划中）
* 评论（规划中）


## 详细
### restful风格Api
统一的restful风格

```
GET **/articles/1  获取 ‘1’ 的记录
POST **/articles  添加一条记录
PUT **/articles/1  修改 ‘1’ 的记录
DELETE **/articles/1  删除 ‘1’ 的记录
GET **/articles  获取集合（可筛选，幂等，相同参数固定资源）
```

```java
public abstract class BaseRestController<T, E extends BaseService<T>> {

    @Autowired
    private E e;

    @GetMapping("/{id}")
    public T get(@PathVariable Long id) {
        return this.e.get(id);
    }

    @PostMapping
    public ResultFactory.Info add(T t) {
        this.e.add(t);
        return ResultFactory.get200Info().message("添加成功");
    }

    @PutMapping("/{id}")
    public ResultFactory.Info update(T t, @PathVariable Long id) {
        this.e.update(t, id);
        return ResultFactory.get200Info().message("修改成功");
    }

    @DeleteMapping("/{id}")
    public ResultFactory.Info delete(@PathVariable Long id) {
        this.e.delete(id);
        return ResultFactory.get200Info().message("删除成功");
    }

}
```
```java
@RestController
@RequestMapping("/articles")
public class ArticleController extends BaseRestController<Article, ArticleService> {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询博文集合
     * 
     * 可传递参数：sortByAsc, sortByDesc, before, beforeBy, after, afterBy, page, size, categoryId
     *
     * page 分页。页码。默认1。
     * size 分页。每页记录。默认10。
     *
     * sortByAsc, sortByDesc 排序
     * sortByAsc 正序
     * sortByDesc 反序
     * 可选参数 id, title, content, summary, traffic，created, modified
     * 多个排序可用 created,modified （SQL同规则）
     *
     * before、beforeBy， after、afterBy 筛选在什么时间之前/之后的记录
     *   before、beforeBy 必须同时存在。在什么时候之前的记录
     *     before 时间
     *     beforeBy 什么规则，可选created（创建时间），modified（最后修改时间）
     *   after、afterBy 必须同时存在。在什么时候之后的记录
     *     after 时间
     *     afterBy 什么规则，可选created（创建时间），modified（最后修改时间）
     *
     * view 用于搜索，可搜索字段 title, content
     *
     * categoryId 分类id，通过分类id筛选
     *
     * @param requestParam
     * @param categoryId
     * @return
     */
    @GetMapping
    public ResultFactory.Collection<Article> list(RestRequestParam requestParam, Long categoryId) {
        return this.articleService.list(requestParam, categoryId);
    }

    /**
     * 查询博文集合总数
     *
     * 参数: {@link ArticleController#list(RestRequestParam, Long)}
     *
     * @param requestParam
     * @param categoryId
     * @return
     */
    @GetMapping("/total")
    public ResultFactory.Info listTotal(RestRequestParam requestParam, Long categoryId) {
        Long total = this.articleService.total(requestParam, categoryId);
        return ResultFactory.get200Info().data(total);
    }

}
```

###　Spring Security Oauth 登录
使用　Spring security Oauth 作为安全框架
```
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().disable();
        http.authorizeRequests()
                // 对 /oauth/** 开放所有访问权限
                .antMatchers("/oauth/**").permitAll();
    }

    /**
     * 加密工具
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }
}
```

```
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(redisTokenStore())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public CustomClientDetailsService customClientDetailsService() {
        return new CustomClientDetailsService();
    }

}
```

```
@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final static String DEMO_RESOURCE_ID = "alpaca_blog";

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(DEMO_RESOURCE_ID).tokenStore(redisTokenStore()).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.authorizeRequests()
                // 按顺序匹配
                // permitAll 开放所有权限
                // 则 .antMatchers(HttpMethod.GET).permitAll().antMatchers(HttpMethod.GET， “/user/**”).permitAll()
                // antMatchers(HttpMethod.GET， “/user/**”) 无效
                // 对get请求开放权限
                .antMatchers("/user/**").authenticated()
                .antMatchers(HttpMethod.GET).permitAll()
                // post请求需要认证
                .antMatchers(HttpMethod.POST).authenticated()
                // put请求需要认证
                .antMatchers(HttpMethod.PUT).authenticated()
                // delete请求需要认证
                .antMatchers(HttpMethod.DELETE).authenticated();
    }

    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

}
```

### Spring Data Jpa 持久化

```
public interface ArticleRepository extends JpaRepository<ArticleDO, Long>, JpaSpecificationExecutor<ArticleDO> {

    /**
     * 查询　{@link ArticleDO}
     * 
     * @param id articleDO的id {@link ArticleDO#id}
     * @param articleDODelete articleDO delete　{@link ArticleDO#delete}
     * @param categoryDODelete categoryDO delete　{@link com.yd1994.alpacablog.entity.CategoryDO#delete}
     * @return
     */
    ArticleDO findFirstByIdAndDeleteAndCategoryDODelete(Long id, Boolean articleDODelete, Boolean categoryDODelete);

}
```

```
    protected Specification<E> getRestSpecification(RestRequestParam requestParam) {
        return (Specification<E>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (requestParam.getBefore() != null) {
                if (!StringUtils.isEmpty(requestParam.getBeforeBy())) {
                    if ("created".equals(requestParam.getBeforeBy())) {
                        requestParam.setBeforeBy("gmtCreated");
                    }
                    else if ("modified".equals(requestParam.getBeforeBy())) {
                        requestParam.setBeforeBy("gmtModified");
                    } else {
                        throw new RequestParamErrorException("beforeBy参数不可为：" + requestParam.getBeforeBy() +  "。");
                    }
                    // 在 该时间 之前（包含该时间）
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(requestParam.getBeforeBy()).as(Date.class), requestParam.getBefore()));
                } else {
                    throw new RequestParamErrorException("beforeBy参数不可空。");
                }
            }
            if (requestParam.getAfter() != null) {
                if (!StringUtils.isEmpty(requestParam.getAfterBy())) {
                    if ("created".equals(requestParam.getAfterBy())) {
                        requestParam.setAfterBy("gmtCreated");
                    }
                    else if ("modified".equals(requestParam.getAfterBy())) {
                        requestParam.setAfterBy("gmtModified");
                    } else {
                        throw new RequestParamErrorException("afterBy参数不可为：" + requestParam.getBeforeBy() +  "。");
                    }
                    // 在 该时间 之后（包含该时间）
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(requestParam.getAfterBy()).as(Date.class), requestParam.getAfter()));
                } else {
                    throw new RequestParamErrorException("afterBy参数不可空。");
                }
            }
            // 筛选出未删除
            predicateList.add(criteriaBuilder.equal(root.get("delete").as(Boolean.class), false));
            this.addRestSpecificationPredicateList(predicateList, root, criteriaBuilder, requestParam);
            Predicate[] p = new Predicate[predicateList.size()];
            query.where(criteriaBuilder.and(predicateList.toArray(p)));

            List<Order> orderList = new ArrayList<>();
            if (!StringUtils.isEmpty(requestParam.getSortByAsc())) {
                String[] sortByAscArray = requestParam.getSortByAsc().split(",");
                Arrays.asList(sortByAscArray).forEach(sortByAsc -> {
                    sortByAsc = sortByAsc.trim();
                    if ("created".equals(sortByAsc)) {
                        sortByAsc = "gmtCreated";
                    }
                    else if ("modified".equals(sortByAsc)) {
                        sortByAsc = "gmtModified";
                    }
                    orderList.add(criteriaBuilder.asc(root.get(sortByAsc)));
                });
            }
            // 排序
            if (!StringUtils.isEmpty(requestParam.getSortByDesc())) {
                String[] sortByDescArray = requestParam.getSortByDesc().split(",");
                Arrays.asList(sortByDescArray).forEach(sortByDesc -> {
                    sortByDesc = sortByDesc.trim();
                    if ("created".equals(sortByDesc)) {
                        sortByDesc = "gmtCreated";
                    }
                    else if ("modified".equals(sortByDesc)) {
                        sortByDesc = "gmtModified";
                    }
                    orderList.add(criteriaBuilder.desc(root.get(sortByDesc)));
                });
            }
            if (orderList.size() > 0) {
                query.orderBy(orderList);
            }
            return query.getRestriction();
        };
    }
```

### redis缓存

```
@EnableCaching
@Configuration
public class RedisCacheConfigurer extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // entryTtl 中 return redisCacheConfiguration。
        RedisCacheConfiguration redisCacheConfigurationEntity = redisCacheConfiguration.entryTtl(Duration.ofDays(30)).disableCachingNullValues();
        RedisCacheConfiguration sysConfiguration = redisCacheConfiguration.entryTtl(Duration.ofSeconds(60)).disableCachingNullValues();

        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("articles");
        cacheNames.add("categories");
        cacheNames.add("sys_information");
        cacheNames.add("oauth_client_detail");
        cacheNames.add("users");

        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        cacheConfigurationMap.put("articles", redisCacheConfigurationEntity);
        cacheConfigurationMap.put("categories", redisCacheConfigurationEntity);
        cacheConfigurationMap.put("sys_information", redisCacheConfigurationEntity);
        cacheConfigurationMap.put("oauth_client_detail", sysConfiguration);
        cacheConfigurationMap.put("users", sysConfiguration);

        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();

        return cacheManager;
    }

}
```
