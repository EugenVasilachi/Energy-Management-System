package com.example.device.controllers;

import com.example.device.controllers.handlers.ResourceNotFoundException;
import com.example.device.dtos.DeviceDTO;
import com.example.device.services.DeviceService;
import com.example.device.services.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin()
@RequestMapping(value="/devices")
public class DeviceController {

    private final DeviceService deviceService;
    private final UserDeviceService userDeviceService;

    @Autowired
    public DeviceController(DeviceService deviceService, UserDeviceService userDeviceService) {
        this.deviceService = deviceService;
        this.userDeviceService = userDeviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> createDevice(@RequestBody DeviceDTO deviceDTO) {
        UUID userID = deviceService.save(deviceDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceID) {
        DeviceDTO dto = deviceService.findDeviceById(deviceID);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateDevice(@PathVariable("id") UUID deviceID, @RequestBody DeviceDTO newDeviceDTO) {
        try {
            DeviceDTO updateDTO = deviceService.updateDeviceById(deviceID, newDeviceDTO);
            return ResponseEntity.ok(updateDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID deviceID) {
        deviceService.deleteDeviceById(deviceID);

        userDeviceService.deleteMappingByDeviceId(deviceID);

        return new ResponseEntity<>(deviceID, HttpStatus.NO_CONTENT);
    }
}
