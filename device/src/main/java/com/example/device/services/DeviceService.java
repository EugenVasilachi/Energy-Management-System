package com.example.device.services;

import com.example.device.controllers.handlers.ResourceNotFoundException;
import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.builders.DeviceBuilder;
import com.example.device.entities.Device;
import com.example.device.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> userList = deviceRepository.findAll();
        return userList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public UUID save(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    public DeviceDTO findDeviceById(UUID deviceID) {
        Optional<Device> prosumerOptional = deviceRepository.findById(deviceID);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", deviceID);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + deviceID);
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public void deleteDeviceById(UUID deviceID) {
        Optional<Device> prosumerOptional = deviceRepository.findById(deviceID);
        if(prosumerOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", deviceID);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + deviceID);
        }
        deviceRepository.deleteById(deviceID);
    }

    public DeviceDTO updateDeviceById(UUID deviceID, DeviceDTO newUserDTO) {
        Device updatedDevice =  deviceRepository.findById(deviceID)
                .map(device -> {
                    device.setDescription(newUserDTO.getDescription());
                    device.setAddress(newUserDTO.getAddress());
                    device.setMaximumHourlyEnergyConsumption(newUserDTO.getMaximumHourlyEnergyConsumption());
                    return deviceRepository.save(device);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + deviceID));

        return DeviceBuilder.toDeviceDTO(updatedDevice);
    }

}
