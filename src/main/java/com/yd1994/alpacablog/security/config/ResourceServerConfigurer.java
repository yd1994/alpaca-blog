package com.yd1994.alpacablog.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * oauth 资源服务配置
 *
 * @author yd
 */
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
                .antMatchers("/user/**").authenticated()
                // 对get请求开放权限
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
