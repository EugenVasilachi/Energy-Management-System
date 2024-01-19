package com.example.monitoring.dtos.builders;

import com.example.monitoring.dtos.EnergyConsumptionDTO;
import com.example.monitoring.entities.EnergyConsumption;

public class EnergyConsumptionBuilder {

    private EnergyConsumptionBuilder() {
    }

    public static EnergyConsumptionDTO toEnergyConsumtionDTO(EnergyConsumption energyConsumption) {
        return new EnergyConsumptionDTO(energyConsumption.getId(), energyConsumption.getIdDevice(),
                energyConsumption.getTimestamp(), energyConsumption.getValue());
    }

    public static EnergyConsumption toEntity(EnergyConsumptionDTO energyConsumptionDTO) {
        return new EnergyConsumption(energyConsumptionDTO.getIdDevice(),
                energyConsumptionDTO.getTimestamp(), energyConsumptionDTO.getValue());
    }
}
