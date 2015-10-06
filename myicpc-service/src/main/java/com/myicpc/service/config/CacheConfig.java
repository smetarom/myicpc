package com.myicpc.service.config;

import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Cache configuration in MyICPC
 *
 * It uses Infinispan as a backend, but it is hidden behind Spring cache abstraction
 *
 * @author Roman Smetana
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates the cache manager with infinispan configuration
     *
     * @return Infinispan cache manager
     */
    @Bean(name = "cacheManager")
    public SpringEmbeddedCacheManagerFactoryBean cacheManager() {
        SpringEmbeddedCacheManagerFactoryBean cacheManager = new SpringEmbeddedCacheManagerFactoryBean();
        cacheManager.setConfigurationFileLocation(new ClassPathResource("cache/infinispan-config.xml"));
        return cacheManager;
    }
}
