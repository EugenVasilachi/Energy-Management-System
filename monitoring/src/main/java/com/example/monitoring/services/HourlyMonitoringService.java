package com.example.monitoring.services;

import com.example.monitoring.dtos.EnergyConsumptionDTO;
import com.example.monitoring.dtos.SensorDTO;
import com.example.monitoring.websocket.WebSocketListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HourlyMonitoringService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyMonitoringService.class);

    private final SensorService sensorService;
    private final EnergyConsumptionService energyConsumptionService;
    private final WebSocketListener webSocketListener;

    @Autowired
    public HourlyMonitoringService(SensorService sensorService,
                                   EnergyConsumptionService energyConsumptionService,
                                   WebSocketListener webSocketListener) {
        this.sensorService = sensorService;
        this.energyConsumptionService = energyConsumptionService;
        this.webSocketListener = webSocketListener;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void notifyHighConsumption() {
        List<EnergyConsumptionDTO> energyConsumptionDTOS = energyConsumptionService.findEnergyConsumptions();

        Timestamp startMinute = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.MINUTES));
        Timestamp endMinute = new Timestamp(startMinute.getTime() + 60000);
        if (!energyConsumptionDTOS.isEmpty()) {
            Map<UUID, List<EnergyConsumptionDTO>> energyConsumptionByDevice = energyConsumptionDTOS.stream()
                    .filter(energyConsumption ->
                            isInMinuteRange(startMinute, endMinute, energyConsumption.getTimestamp()))
                    .collect(Collectors.groupingBy(EnergyConsumptionDTO::getIdDevice));

            energyConsumptionByDevice.forEach((deviceId, deviceEnergyConsumptions) -> {
                double average = deviceEnergyConsumptions.stream()
                        .mapToDouble(EnergyConsumptionDTO::getValue)
                        .sum() / deviceEnergyConsumptions.size();
                LOGGER.info("Average for the last minute for device {}: {}", deviceId, average);

                SensorDTO sensor = sensorService.findSensorByDeviceId(deviceId);
                if (average > sensor.getMax()) {
                    LOGGER.info("NOTIFICATION !!!!!!!! for device " + deviceId);
                    webSocketListener.pushSystemStatusToWebSocket(sensor);
                }
            });
        }
    }

    private boolean isInMinuteRange(Timestamp startMinute, Timestamp endMinute, Timestamp energyTime) {
        return (energyTime.getTime() >= startMinute.getTime())
                && (energyTime.getTime() < endMinute.getTime());
    }
}
