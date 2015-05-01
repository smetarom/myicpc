package com.myicpc.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Roman Smetana
 */
@Configuration
@EnableJms
public class JMSConfig {
    private static final Logger logger = LoggerFactory.getLogger(JMSConfig.class);

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean(name = "eventFeedControlTopic")
    public JmsTemplate eventFeedControlTopic() {
        JmsTemplate template = new JmsTemplate(connectionFactory());
        template.setDestinationResolver(destinationResolver());
        template.setDefaultDestinationName("java:/jms/topic/EventFeedControlTopic");
        template.setPubSubDomain(true);
        return template;
    }

    @Bean
    public DestinationResolver destinationResolver() {
        JndiDestinationResolver destinationResolver = new JndiDestinationResolver();
        return destinationResolver;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        try {
            Context ctx = new InitialContext();
            return (ConnectionFactory) ctx.lookup("java:/ConnectionFactory");
        } catch (NamingException e) {
            logger.error("Failed to create JMS ConnectionFactory.", e);
            return null;
        }
    }
}
