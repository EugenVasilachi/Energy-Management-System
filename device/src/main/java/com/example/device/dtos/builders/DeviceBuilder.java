package com.example.device.dtos.builders;

import com.example.device.dtos.DeviceDTO;
import com.example.device.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getDescription(),
                device.getAddress(), device.getMaximumHourlyEnergyConsumption());
    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getDescription(),
                deviceDTO.getAddress(),
                deviceDTO.getMaximumHourlyEnergyConsumption());
    }

}
