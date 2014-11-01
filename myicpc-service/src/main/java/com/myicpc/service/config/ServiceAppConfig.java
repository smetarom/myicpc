package com.myicpc.service.config;

import com.myicpc.persistence.config.PersistenceAppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Roman Smetana
 */
@Configuration
@Import(PersistenceAppConfig.class)
public class ServiceAppConfig {
}
