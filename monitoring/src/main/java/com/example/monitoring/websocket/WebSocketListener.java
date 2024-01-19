package com.example.monitoring.websocket;

import com.example.monitoring.dtos.SensorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketListener {

    private final SimpMessagingTemplate webSocket;

    @Autowired
    public WebSocketListener(SimpMessagingTemplate webSocket) {
        this.webSocket = webSocket;
    }

    public void pushSystemStatusToWebSocket (SensorDTO sensorDTO) {
        webSocket.convertAndSend("/broker/notify-high-consumption", sensorDTO);
    }
}
