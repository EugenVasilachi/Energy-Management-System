package com.example.monitoring.controllers;

import com.example.monitoring.dtos.SensorDTO;
import com.example.monitoring.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin()
@RequestMapping(value = "/sensor")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping()
    public ResponseEntity<List<SensorDTO>> getSensors() {
        List<SensorDTO> dtos = sensorService.findSensors();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<UUID> deleteSensor(@PathVariable("id") UUID sensorID) {
        sensorService.deleteSensorById(sensorID);
        return new ResponseEntity<>(sensorID, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping()
    public ResponseEntity<UUID> deleteSensors() {
        sensorService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
