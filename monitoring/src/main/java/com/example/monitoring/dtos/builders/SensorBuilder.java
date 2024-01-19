package com.example.monitoring.dtos.builders;

import com.example.monitoring.dtos.SensorDTO;
import com.example.monitoring.entities.Sensor;

public class SensorBuilder {

    private SensorBuilder() {
    }

    public static SensorDTO toSensorDTO(Sensor sensor) {
        return new SensorDTO(sensor.getId(), sensor.getIdUser(),
                sensor.getIdDevice(), sensor.getDescription(), sensor.getMax());
    }

    public static Sensor toEntity(SensorDTO sensorDTO) {
        return new Sensor(sensorDTO.getIdUser(),
                sensorDTO.getIdDevice(),
                sensorDTO.getDescription(),
                sensorDTO.getMax());
    }
}
