package com.example.GoogleDrive.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hasin.raiyan
 * @since 9/17/23
 */

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.upload.queue.name}")
    private String uploadQueue;

    @Value("${rabbitmq.upload.exchange.name}")
    private String uploadExchange;

    @Value("${rabbitmq.upload.routing.key}")
    private String uploadRoutingKey;

    @Value("${rabbitmq.notify.queue.name}")
    private String notifyQueue;

    @Value("${rabbitmq.notify.exchange.name}")
    private String notifyExchange;

    @Value("${rabbitmq.notify.routing.key}")
    private String notifyRoutingKey;

    @Bean
    public Queue uploadQueue() {
        return new Queue(uploadQueue);
    }

    @Bean
    public Queue notifyQueue() {
        return new Queue(notifyQueue);
    }

    @Bean
    public DirectExchange uploadExchange() {
        return new DirectExchange(uploadExchange);
    }

    @Bean
    public DirectExchange notifyExchange() {
        return new DirectExchange(notifyExchange);
    }

    @Bean
    public Binding uploadBinding() {
        return BindingBuilder
                .bind(uploadQueue())
                .to(uploadExchange())
                .with(uploadRoutingKey);
    }

    @Bean
    public Binding notifyBinding() {
        return BindingBuilder
                .bind(notifyQueue())
                .to(notifyExchange())
                .with(notifyRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        return rabbitTemplate;
    }

    //Connection Factory
    //Rabbit Template
    //Rabbit Admin
}
