package com.example.device.dtos.builders;

import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.UserDeviceDTO;
import com.example.device.entities.Device;
import com.example.device.entities.UserDevice;

public class UserDeviceBuilder {

    private UserDeviceBuilder() {
    }

    public static UserDeviceDTO toUserDeviceDTO(UserDevice userDevice) {
        return new UserDeviceDTO(userDevice.getIdUserDevice(), userDevice.getIdUser(),
                userDevice.getIdDevice());
    }

    public static UserDevice toEntity(UserDeviceDTO userDeviceDTO) {
        return new UserDevice(userDeviceDTO.getIdUser(), userDeviceDTO.getIdDevice());
    }

}
