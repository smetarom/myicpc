package com.myicpc.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * @author Roman Smetana
 */
@Configuration
@EnableJpaRepositories({"com.myicpc.repository"})
@EnableTransactionManagement
public class PersistenceAppConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.myicpc.model");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);

        return em;
    }

    @Bean
    public DataSource dataSource() {
        JndiDataSourceLookup dataLookup = new JndiDataSourceLookup();
        dataLookup.setResourceRef(true);

        DataSource dataSource = dataLookup.getDataSource("java:jboss/datasources/PostgreSQLDS");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties additionalProperties() {
        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        prop.setProperty("hibernate.show_sql", "false");
        prop.setProperty("hibernate.hbm2ddl.auto", "update");
        prop.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        prop.setProperty("hibernate.connection.useUnicode", "true");

//        prop.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.infinispan.JndiInfinispanRegionFactory");
//        prop.setProperty("hibernate.cache.infinispan.cachemanager", "java:jboss/infinispan/container/hibernate");
//        prop.setProperty("hibernate.cache.use_second_level_cache", "true");
//        prop.setProperty("hibernate.cache.use_query_cache", "true");
        return prop;
    }
}
