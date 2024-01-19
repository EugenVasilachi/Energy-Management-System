package com.example.device.events;

import com.example.device.broker.DeviceProducer;
import com.example.device.dtos.MonitoringDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserDeviceCreatedEventListener {

    private final DeviceProducer deviceProducer;

    @Autowired
    public UserDeviceCreatedEventListener(DeviceProducer deviceProducer) {
        this.deviceProducer = deviceProducer;
    }

    @EventListener
    public void handleUserDeviceCreatedEvent(UserDeviceCreatedEvent event) {
        deviceProducer.sendToDeviceQueue((MonitoringDTO) event.getSource());
    }
}
