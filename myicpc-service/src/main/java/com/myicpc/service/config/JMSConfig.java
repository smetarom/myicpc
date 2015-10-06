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
 * JMS messaging configuration
 *
 * @author Roman Smetana
 */
@Configuration
@EnableJms
public class JMSConfig {
    private static final Logger logger = LoggerFactory.getLogger(JMSConfig.class);

    /**
     * Creates JMS Queue listeners
     *
     * @return queue listener factory
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency("3-10");
        return factory;
    }

    /**
     * Creates JMS Topic listeners
     *
     * @return topic listener factory
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency("3-10");
        factory.setPubSubDomain(true);
        return factory;
    }

    /**
     * Creates a template, which uses JMS Topic
     *
     * @return topic template
     */
    @Bean(name = "jmsTopicTemplate")
    public JmsTemplate jmsTopicTemplate() {
        JmsTemplate template = new JmsTemplate(connectionFactory());
        template.setDestinationResolver(destinationResolver());
        template.setPubSubDomain(true);
        return template;
    }

    /**
     * Creates a template, which uses JMS Queue
     *
     * @return queue topic
     */
    @Bean(name = "jmsQueueTemplate")
    public JmsTemplate jmsQueueTemplate() {
        JmsTemplate template = new JmsTemplate(connectionFactory());
        template.setDestinationResolver(destinationResolver());
        return template;
    }

    /**
     * Creates JNDI destination resolver
     *
     * It resolves queues and topics destinations in Java context
     *
     * @return JNDI destination resolver
     */
    @Bean
    public DestinationResolver destinationResolver() {
        JndiDestinationResolver destinationResolver = new JndiDestinationResolver();
        return destinationResolver;
    }

    /**
     * Gets the connection factory from context
     *
     * @return connection factory
     */
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
