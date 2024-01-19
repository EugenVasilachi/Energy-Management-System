package com.example.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDTO {

    private UUID idUser;
    private UUID idDevice;
    private String description;
    private Double max;

}
