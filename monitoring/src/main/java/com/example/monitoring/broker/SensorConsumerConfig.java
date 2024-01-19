package com.example.monitoring.broker;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SensorConsumerConfig {

    @Value("${rabbitmq.device-queue}")
    private String queueName;

    @Value("${rabbitmq.device-exchange}")
    private String exchange;

    @Value("${rabbitmq.device-routingKey}")
    private String routingKey;

    @Bean(name = "deviceQueue")
    public Queue deviceQueue() {
        return new Queue(queueName, false);
    }

    @Bean(name = "deviceExchange")
    public DirectExchange deviceExchange() {
        return new DirectExchange(exchange);
    }

    @Bean(name = "deviceBinding")
    public Binding binding(Queue deviceQueue, DirectExchange deviceExchange) {
        return BindingBuilder
                .bind(deviceQueue)
                .to(deviceExchange)
                .with(routingKey);
    }

    @Bean(name = "deviceJsonMessageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "deviceRabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}

