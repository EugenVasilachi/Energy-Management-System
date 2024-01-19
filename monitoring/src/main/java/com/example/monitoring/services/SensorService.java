package com.example.monitoring.services;

import com.example.monitoring.controllers.handlers.ResourceNotFoundException;
import com.example.monitoring.dtos.SensorDTO;
import com.example.monitoring.dtos.builders.SensorBuilder;
import com.example.monitoring.entities.Sensor;
import com.example.monitoring.repositories.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SensorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sensor.class);
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<SensorDTO> findSensors() {
        List<Sensor> sensorList = sensorRepository.findAll();
        return sensorList.stream()
                .map(SensorBuilder::toSensorDTO)
                .collect(Collectors.toList());
    }

    public SensorDTO findSensorById(UUID id) {
        Optional<Sensor> prosumerOptional = sensorRepository.findById(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Sensor with id {} was not found in db", id);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id);
        }
        return SensorBuilder.toSensorDTO(prosumerOptional.get());
    }

    public SensorDTO findSensorByDeviceId(UUID id) {
        Optional<Sensor> prosumerOptional = sensorRepository.findByIdDevice(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Sensor with id {} was not found in db", id);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id);
        }
        return SensorBuilder.toSensorDTO(prosumerOptional.get());
    }

    public List<SensorDTO> findSensorsByUserId(UUID id) {
        Optional<List<Sensor>> prosumerOptional = sensorRepository.findAllByIdUser(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Sensor with id {} was not found in db", id);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id);
        }
        return prosumerOptional.get().stream().map(SensorBuilder::toSensorDTO).collect(Collectors.toList());
    }

    public UUID save(SensorDTO sensorDTO) {
        Sensor sensor = SensorBuilder.toEntity(sensorDTO);
        sensor = sensorRepository.save(sensor);
        LOGGER.debug("Sensor with id {} was inserted in db", sensor.getId());
        return sensor.getId();
    }

    public List<UUID> saveAll(List<SensorDTO> sensorDTOs) {
        List<Sensor> sensors = sensorDTOs.stream()
                .map(SensorBuilder::toEntity)
                .toList();
        List<UUID> savedIds = sensorRepository.saveAll(sensors).stream()
                .map(Sensor::getId)
                .toList();
        LOGGER.debug("Saved Sensors with ids: {}", savedIds);
        return savedIds;
    }

    public void update(UUID id, SensorDTO sensorDTO) {
        sensorRepository.findById(id)
                .map(sensor -> {
                    sensor.setIdUser(sensorDTO.getIdUser());
                    sensor.setIdDevice(sensorDTO.getIdDevice());
                    sensor.setMax(sensorDTO.getMax());
                    return sensorRepository.save(sensor);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id));
    }

    public void deleteSensorById(UUID id) {
        Optional<Sensor> prosumerOptional = sensorRepository.findById(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id);
        }
        sensorRepository.deleteById(id);
    }

    public void deleteAll() {
        sensorRepository.deleteAll();
    }

    public boolean isDatabaseEmpty() {
        return sensorRepository.count() == 0;
    }
}
