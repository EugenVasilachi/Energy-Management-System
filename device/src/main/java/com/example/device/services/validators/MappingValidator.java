package com.example.device.services.validators;

import com.example.device.dtos.UserDeviceDTO;
import com.example.device.entities.UserDevice;

import java.util.List;
import java.util.Objects;

public class MappingValidator {

    public static boolean validateUniqueDevice(UserDevice newMapping, List<UserDeviceDTO> mappings) {
        return mappings.stream()
                .noneMatch(existingMatching ->
                        Objects.equals(existingMatching.getIdDevice(), newMapping.getIdDevice()));
    }

}
