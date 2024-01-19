package com.example.device.controllers;

import com.example.device.broker.DeviceProducer;
import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.UserDeviceDTO;
import com.example.device.services.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value="/userdevices")
public class UserDeviceController {

    private final UserDeviceService userDeviceService;
    private final DeviceProducer deviceProducer;

    @Autowired
    public UserDeviceController(UserDeviceService userDeviceService, DeviceProducer deviceProducer) {
        this.userDeviceService = userDeviceService;
        this.deviceProducer = deviceProducer;
    }

    @GetMapping()
    public ResponseEntity<List<UserDeviceDTO>> getUserDevices() {
        List<UserDeviceDTO> dtos = userDeviceService.findUserDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> createUserDevice(@RequestBody UserDeviceDTO userDeviceDTO) {
        UUID idUser = userDeviceService.save(userDeviceDTO);
        return new ResponseEntity<>(idUser, HttpStatus.CREATED);
    }

    @PostMapping("/publish")
    public ResponseEntity<Void> sendDevicesToMonitoring() {
        deviceProducer.publish();
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<UserDeviceDTO> getUserDevice(@PathVariable("id") UUID idUserDevice) {
        UserDeviceDTO dto = userDeviceService.findUserDeviceById(idUserDevice);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/getDevicesForUser/{idUser}")
    public ResponseEntity<List<DeviceDTO>> getDevicesForUser(@PathVariable("idUser") UUID idUser) {
        List<DeviceDTO> dtos = userDeviceService.findDevicesForUser(idUser);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteUserDeviceById(@PathVariable("id") UUID idUserDevice) {
        userDeviceService.deleteMappingById(idUserDevice);
        return new ResponseEntity<>(idUserDevice, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteFromUser/{idUser}")
    public ResponseEntity<UUID> deleteUserDeviceFromUser(@PathVariable("idUser") UUID idUser) {
        userDeviceService.deleteMappingByUserId(idUser);
        return new ResponseEntity<>(idUser, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteFromDevice/{idDevice}")
    public ResponseEntity<UUID> deleteUserDeviceFromDevice(@PathVariable("idDevice") UUID idDevice) {
        userDeviceService.deleteMappingByDeviceId(idDevice);
        return new ResponseEntity<>(idDevice, HttpStatus.NO_CONTENT);
    }
}
