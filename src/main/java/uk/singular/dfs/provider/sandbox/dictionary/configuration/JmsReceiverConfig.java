package uk.singular.dfs.provider.sandbox.dictionary.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.handlers.SampleErrorHandler;

import javax.jms.Session;

@EnableJms
@Configuration
public class JmsReceiverConfig {

    private final String brokerUrl = "tcp://127.0.0.1:61616";

    private final String username = "admin";

    private final String password = "admin";

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(receiveActiveMQConnectionFactory());
        factory.setConcurrency("10-20");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setSessionTransacted(true);
        factory.setErrorHandler(new SampleErrorHandler());
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(){
        return new CachingConnectionFactory(receiveActiveMQConnectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        return new JmsTemplate(cachingConnectionFactory());
    }

    @Bean
    public ActiveMQConnectionFactory receiveActiveMQConnectionFactory() {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(brokerUrl);
        if (username != null) {
            cf.setUserName(username);
        }
        if (password != null) {
            cf.setPassword(password);
        }
        return cf;
    }
}