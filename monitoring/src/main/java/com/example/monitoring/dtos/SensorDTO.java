package com.example.monitoring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {

    private UUID id;
    private UUID idUser;
    private UUID idDevice;
    private String description;
    private Double max;

    public SensorDTO(UUID idUser, UUID idDevice, String description, Double max) {
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.description = description;
        this.max = max;
    }
}
