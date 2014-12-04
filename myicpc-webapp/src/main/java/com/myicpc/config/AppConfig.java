package com.myicpc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author Roman Smetana
 */
@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan("com.myicpc")
public class AppConfig {

    private static final int MAX_UPLOAD_SIZE = 20 * 1024 * 1024; // 20 MB


    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
        return multipartResolver;
    }
}
