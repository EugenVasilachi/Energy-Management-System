package com.example.device.broker;

import com.example.device.dtos.MonitoringDTO;
import com.example.device.services.UserDeviceService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DeviceProducer {

    private final Logger LOGGER = LoggerFactory.getLogger(DeviceProducer.class);
    private final AmqpTemplate amqpTemplate;
    private final UserDeviceService userDeviceService;

    @Value("${rabbitmq.device-exchange}")
    private String exchange;

    @Value("${rabbitmq.device-routingKey}")
    private String routingKey;

    public DeviceProducer(AmqpTemplate amqpTemplate, UserDeviceService userDeviceService) {
        this.amqpTemplate = amqpTemplate;
        this.userDeviceService = userDeviceService;
    }

    public void publish() {
        List<MonitoringDTO> monitoringDTOs = userDeviceService.findDevicesForMonitoring();
        sendToDeviceQueue(monitoringDTOs);
    }

    public void sendToDeviceQueue(List<MonitoringDTO> monitoringDTOs) {
        try {
            amqpTemplate.convertAndSend(exchange, routingKey, monitoringDTOs);
            LOGGER.info("Published to RabbitMQ - Exchange: {}, Routing Key: {}, Message: {}",
                    exchange, routingKey, monitoringDTOs);
        } catch (Exception e) {
            LOGGER.error("Error publishing to RabbitMQ: {}", e.getMessage());
        }
    }

    public void sendToDeviceQueue(MonitoringDTO monitoringDTO) {
        try {
            amqpTemplate.convertAndSend(exchange, routingKey, monitoringDTO);
            LOGGER.info("Published to RabbitMQ - Exchange: {}, Routing Key: {}, Message: {}",
                    exchange, routingKey, monitoringDTO);
        } catch (Exception e) {
            LOGGER.error("Error publishing to RabbitMQ: {}", e.getMessage());
        }
    }
}
