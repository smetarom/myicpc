package com.myicpc.service.config;

import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Roman Smetana
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "cacheManager")
    public SpringEmbeddedCacheManagerFactoryBean cacheManager() throws Exception {
        SpringEmbeddedCacheManagerFactoryBean cacheManager = new SpringEmbeddedCacheManagerFactoryBean();
        cacheManager.setConfigurationFileLocation(new ClassPathResource("cache/infinispan-config.xml"));
        return cacheManager;
    }
}
