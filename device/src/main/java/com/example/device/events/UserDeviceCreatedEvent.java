package com.example.device.events;

import org.springframework.context.ApplicationEvent;

public class UserDeviceCreatedEvent extends ApplicationEvent {

    public UserDeviceCreatedEvent(Object source) {
        super(source);
    }
}
