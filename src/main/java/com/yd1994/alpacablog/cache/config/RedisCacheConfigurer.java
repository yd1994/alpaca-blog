package com.yd1994.alpacablog.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * reids 缓存配置
 *
 * @author yd
 */
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
        cacheNames.add("oauth_client_detail");
        cacheNames.add("users");

        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        cacheConfigurationMap.put("articles", redisCacheConfigurationEntity);
        cacheConfigurationMap.put("categories", redisCacheConfigurationEntity);
        cacheConfigurationMap.put("oauth_client_detail", sysConfiguration);
        cacheConfigurationMap.put("users", sysConfiguration);

        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();

        return cacheManager;
    }

}
