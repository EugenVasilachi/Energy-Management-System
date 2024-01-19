package com.example.monitoring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumptionDTO {

    private UUID id;
    private UUID idDevice;
    private Timestamp timestamp;
    private Double value;

    public EnergyConsumptionDTO(UUID idDevice, Timestamp timestamp, Double value) {
        this.idDevice = idDevice;
        this.timestamp = timestamp;
        this.value = value;
    }
}
