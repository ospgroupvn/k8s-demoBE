package com.osp.bttp.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager1() {
        return new ConcurrentMapCacheManager("appVersion");
    }

    @Bean
    public CacheManager cacheManager2() {
        return new ConcurrentMapCacheManager("places");
    }

    @Bean
    public CacheResolver cacheResolver() {
        return new MultipleCacheResolver(cacheManager1(), cacheManager2());
    }


}