package com.example.monitoring.controllers;

import com.example.monitoring.dtos.EnergyConsumptionDTO;
import com.example.monitoring.dtos.SensorDTO;
import com.example.monitoring.services.EnergyConsumptionService;
import com.example.monitoring.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin()
@RequestMapping(value = "/energy-consumption")
public class EnergyConsumptionController {

    private final EnergyConsumptionService energyConsumptionService;
    private final SensorService sensorService;

    @Autowired
    public EnergyConsumptionController(EnergyConsumptionService energyConsumptionService,
                                       SensorService sensorService) {
        this.energyConsumptionService = energyConsumptionService;
        this.sensorService = sensorService;
    }

    @GetMapping()
    public ResponseEntity<List<EnergyConsumptionDTO>> getEnergyConsumptions() {
        List<EnergyConsumptionDTO> dtos = energyConsumptionService.findEnergyConsumptions();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "{idUser}")
    public ResponseEntity<Map<UUID, List<EnergyConsumptionDTO>>> getEnergyConsumptionsForUser(@PathVariable UUID idUser) {
        List<SensorDTO> sensorDTOs = sensorService.findSensorsByUserId(idUser);
        List<UUID> deviceIds = sensorDTOs.stream().map(SensorDTO::getIdDevice).toList();

        Map<UUID, List<EnergyConsumptionDTO>> energyConsumptionsByDeviceId = new HashMap<>();

        for (UUID deviceId : deviceIds) {
            List<EnergyConsumptionDTO> energyConsumptionDTOs =
                    energyConsumptionService.findEnergyConsumptionsByDeviceId(deviceId);
            energyConsumptionsByDeviceId.put(deviceId, energyConsumptionDTOs);
        }

        return new ResponseEntity<>(energyConsumptionsByDeviceId, HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<UUID> deleteEnergyConsumption(@PathVariable("id") UUID energyID) {
        energyConsumptionService.deleteEnergyConsumptionById(energyID);
        return new ResponseEntity<>(energyID, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping()
    public ResponseEntity<UUID> deleteEnergyConsumptions() {
        energyConsumptionService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
