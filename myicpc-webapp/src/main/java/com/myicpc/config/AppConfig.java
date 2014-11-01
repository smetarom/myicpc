package com.myicpc.config;

import com.myicpc.service.config.ServiceAppConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Roman Smetana
 */
@Configuration
@ComponentScan("com.myicpc")
@Import({ServiceAppConfig.class})
public class AppConfig {
}
