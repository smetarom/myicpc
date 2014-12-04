package com.myicpc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.myicpc.controller")
public class WebappConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(86400);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(86400);
        registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(86400);
        registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/").setCachePeriod(86400);
        registry.addResourceHandler("/cache.manifest").addResourceLocations("/cache.manifest");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(
            ContentNegotiationManager manager) {

        List<ViewResolver> resolvers = new ArrayList<>();

        InternalResourceViewResolver internalResolver = new InternalResourceViewResolver();
        internalResolver.setPrefix("/WEB-INF/views/");
        internalResolver.setSuffix(".jsp");
        internalResolver.setViewClass(JstlView.class);
        resolvers.add(internalResolver);

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setViewResolvers(resolvers);
        resolver.setContentNegotiationManager(manager);
        return resolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        String[] resources = {"classpath:i18n/text"};
        messageSource.setBasenames(resources);
        return messageSource;
    }
}
