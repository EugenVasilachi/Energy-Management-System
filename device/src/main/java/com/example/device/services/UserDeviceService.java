package com.example.device.services;

import com.example.device.broker.DeviceProducer;
import com.example.device.controllers.handlers.ResourceNotFoundException;
import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.MonitoringDTO;
import com.example.device.dtos.UserDeviceDTO;
import com.example.device.dtos.builders.DeviceBuilder;
import com.example.device.dtos.builders.UserDeviceBuilder;
import com.example.device.entities.Device;
import com.example.device.entities.UserDevice;
import com.example.device.events.UserDeviceCreatedEvent;
import com.example.device.repositories.DeviceRepository;
import com.example.device.repositories.UserDeviceRepository;
import com.example.device.services.validators.MappingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserDeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeviceService.class);

    private final UserDeviceRepository userDeviceRepository;
    private final DeviceRepository deviceRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserDeviceService(UserDeviceRepository userDeviceRepository,
                             DeviceRepository deviceRepository,
                             ApplicationEventPublisher eventPublisher) {
        this.userDeviceRepository = userDeviceRepository;
        this.deviceRepository = deviceRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<UserDeviceDTO> findUserDevices() {
        List<UserDevice> userDevices = userDeviceRepository.findAll();
        return userDevices.stream()
                .map(UserDeviceBuilder::toUserDeviceDTO)
                .collect(Collectors.toList());
    }

    public UserDeviceDTO findUserDeviceById(UUID idUserDevice) {
        Optional<UserDevice> prosumerOptional = userDeviceRepository.findById(idUserDevice);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("UserDevice with id {} was not found in db", idUserDevice);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + idUserDevice);
        }
        return UserDeviceBuilder.toUserDeviceDTO(prosumerOptional.get());
    }

    public List<DeviceDTO> findDevicesForUser(UUID idUser) {
        List<UserDevice> userDevices = userDeviceRepository.findByIdUser(idUser);

        List<UUID> deviceIds = userDevices.stream()
                .map(UserDevice::getIdDevice)
                .collect(Collectors.toList());

        return deviceRepository.findAllById(deviceIds).stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public List<MonitoringDTO> findDevicesForMonitoring() {
        return userDeviceRepository.findAll().stream()
                .map(this::createMonitoringDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private MonitoringDTO createMonitoringDTO(UserDevice userDevice) {
        return deviceRepository.findById(userDevice.getIdDevice())
                .map(device -> new MonitoringDTO(
                        userDevice.getIdUser(),
                        device.getId(),
                        device.getDescription(),
                        device.getMaximumHourlyEnergyConsumption().doubleValue()
                ))
                .orElse(null);
    }

    public UUID save(UserDeviceDTO userDeviceDTO) {
        UserDevice userDevice = UserDeviceBuilder.toEntity(userDeviceDTO);
        List<UserDeviceDTO> existingMappings = findUserDevices();

        /** Respects the one-to-many relationship
         *  A device cannot have multiple users
         */
        if (MappingValidator.validateUniqueDevice(userDevice, existingMappings)) {
            MonitoringDTO monitoringDTO = createMonitoringDTO(userDevice);
            eventPublisher.publishEvent(new UserDeviceCreatedEvent(monitoringDTO));
            userDevice = userDeviceRepository.save(userDevice);
            LOGGER.debug("UserDevice with id {} was inserted in db", userDevice.getIdUserDevice());
            return userDevice.getIdUserDevice();
        }
        return null;
    }

    public void deleteMappingById(UUID userDeviceID) {
        Optional<UserDevice> prosumerOptional = userDeviceRepository.findById(userDeviceID);
        if(prosumerOptional.isEmpty()) {
            LOGGER.error("UserDevice with id {} was not found in db", userDeviceID);
            throw new ResourceNotFoundException(UserDevice.class.getSimpleName() + " with id: " + userDeviceID);
        }
        userDeviceRepository.deleteById(userDeviceID);
    }

    public void deleteMappingByUserId(UUID idUser) {
        userDeviceRepository.deleteByIdUser(idUser);
    }

    public void deleteMappingByDeviceId(UUID idDevice) {
        userDeviceRepository.deleteByIdDevice(idDevice);
    }
}
