package com.example.monitoring.services;

import com.example.monitoring.controllers.handlers.ResourceNotFoundException;
import com.example.monitoring.dtos.EnergyConsumptionDTO;
import com.example.monitoring.dtos.builders.EnergyConsumptionBuilder;
import com.example.monitoring.dtos.builders.SensorBuilder;
import com.example.monitoring.entities.EnergyConsumption;
import com.example.monitoring.entities.Sensor;
import com.example.monitoring.repositories.EnergyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnergyConsumptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumption.class);
    private final EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    public EnergyConsumptionService(EnergyConsumptionRepository energyConsumptionRepository) {
        this.energyConsumptionRepository = energyConsumptionRepository;
    }

    public List<EnergyConsumptionDTO> findEnergyConsumptions() {
        List<EnergyConsumption> energyConsumptionList = energyConsumptionRepository.findAll();
        return energyConsumptionList.stream()
                .map(EnergyConsumptionBuilder::toEnergyConsumtionDTO)
                .collect(Collectors.toList());
    }

    public EnergyConsumptionDTO findEnergyConsumptionById(UUID id) {
        Optional<EnergyConsumption> prosumerOptional = energyConsumptionRepository.findById(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Energy Consumption with id {} was not found in db", id);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id);
        }
        return EnergyConsumptionBuilder.toEnergyConsumtionDTO(prosumerOptional.get());
    }

    public List<EnergyConsumptionDTO> findEnergyConsumptionsByDeviceId(UUID id) {
        Optional<List<EnergyConsumption>> prosumerOptional = energyConsumptionRepository.findAllByIdDevice(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Energy Consumption with id {} was not found in db", id);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + id);
        }
        return prosumerOptional.get().stream().map(EnergyConsumptionBuilder::toEnergyConsumtionDTO)
                .collect(Collectors.toList());
    }

    public UUID save(EnergyConsumptionDTO energyConsumptionDTO) {
        EnergyConsumption energyConsumption = EnergyConsumptionBuilder.toEntity(energyConsumptionDTO);
        energyConsumption = energyConsumptionRepository.save(energyConsumption);
        LOGGER.debug("Energy Consumption with id {} was inserted in db", energyConsumption.getId());
        return energyConsumption.getId();
    }

    public void update(UUID id, EnergyConsumptionDTO energyConsumptionDTO) {
        energyConsumptionRepository.findById(id)
                .map(energy -> {
                    energy.setTimestamp(energyConsumptionDTO.getTimestamp());
                    energy.setValue(energyConsumptionDTO.getValue());
                    return energyConsumptionRepository.save(energy);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(EnergyConsumption.class.getSimpleName() + " with id: " + id));
    }

    public void deleteEnergyConsumptionById(UUID id) {
        Optional<EnergyConsumption> prosumerOptional = energyConsumptionRepository.findById(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Energy Consumption with id {} was not found in db", id);
            throw new ResourceNotFoundException(EnergyConsumption.class.getSimpleName() + " with id: " + id);
        }
        energyConsumptionRepository.deleteById(id);
    }

    public void deleteAll() {
        energyConsumptionRepository.deleteAll();
    }
}
