package com.dy.mcrecorder.mc_recorder.config;

import com.dy.mcrecorder.mc_recorder.common.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(MqConstants.USER_REGISTERED_QUEUE, true);
    }

    @Bean
    public DirectExchange eventsExchange() {
        return new DirectExchange(MqConstants.EVENTS_EXCHANGE);
    }

    @Bean
    public Binding userRegisteredBinding(Queue userRegisteredQueue, DirectExchange eventsExchange) {
        return BindingBuilder.bind(userRegisteredQueue)
                .to(eventsExchange)
                .with(MqConstants.USER_REGISTERED_KEY);
    }
}