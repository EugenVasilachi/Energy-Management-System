package com.example.monitoring.broker;

import com.example.monitoring.dtos.SensorDTO;
import com.example.monitoring.services.SensorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SensorConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(SensorConsumer.class);

    private final SensorService sensorService;

    private final ObjectMapper objectMapper;

    @Autowired
    public SensorConsumer(SensorService sensorService,
                          ObjectMapper objectMapper) {
        this.sensorService = sensorService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.device-queue}")
    public void consume(Message message) {
        try {
            String messageBody = new String(message.getBody());

            try {
                List<SensorDTO> sensorDTOs = objectMapper.readValue(messageBody,
                        new TypeReference<>() {
                        });

                if (sensorService.isDatabaseEmpty()) {
                    sensorService.saveAll(sensorDTOs);
                    LOGGER.info("Received and saved MonitoringDTOs: {}", sensorDTOs);
                } else {
                    LOGGER.info("Database is not empty. Skipped saving MonitoringDTOs.");
                }
            } catch (JsonMappingException listDeserializationException) {
                SensorDTO sensorDTO = objectMapper.readValue(messageBody, SensorDTO.class);
                sensorService.save(sensorDTO);
                LOGGER.info("Received from REGISTER and save MonitoringDTO: {}", sensorDTO);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing the received message", e);
        }
    }
}
