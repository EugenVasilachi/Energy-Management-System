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
public class EnergyConsumptionConsumerConfig {

    @Value("${rabbitmq.energy-queue}")
    private String queueName;

    @Value("${rabbitmq.energy-exchange}")
    private String exchange;

    @Value("${rabbitmq.energy-routingKey}")
    private String routingKey;

    @Bean(name = "energyQueue")
    public Queue energyQueue() {
        return new Queue(queueName, false);
    }

    @Bean(name = "energyExchange")
    public DirectExchange energyExchange() {
        return new DirectExchange(exchange);
    }

    @Bean(name = "energyBinding")
    public Binding energyBinding(Queue energyQueue, DirectExchange energyExchange) {
        return BindingBuilder
                .bind(energyQueue)
                .to(energyExchange)
                .with(routingKey);
    }

    @Bean(name = "energyJsonMessageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "energyRabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
