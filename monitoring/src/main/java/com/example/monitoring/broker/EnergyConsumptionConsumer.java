package com.example.monitoring.broker;

import com.example.monitoring.dtos.EnergyConsumptionDTO;
import com.example.monitoring.services.EnergyConsumptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnergyConsumptionConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionConsumer.class);

    private final EnergyConsumptionService energyConsumptionService;

    private final ObjectMapper objectMapper;

    @Autowired
    public EnergyConsumptionConsumer(EnergyConsumptionService energyConsumptionService,
                                     ObjectMapper objectMapper) {
        this.energyConsumptionService = energyConsumptionService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.energy-queue}")
    public void consume(Message message) {
        try {
            String messageBody = new String(message.getBody());
            EnergyConsumptionDTO energyConsumptionDTO =
                    objectMapper.readValue(messageBody, EnergyConsumptionDTO.class);
            energyConsumptionService.save(energyConsumptionDTO);
            LOGGER.info(String.valueOf(energyConsumptionDTO));
        } catch (Exception e) {
            LOGGER.error("Error processing the received message", e);
        }
    }
}
