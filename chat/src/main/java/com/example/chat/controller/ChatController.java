package com.example.chat.controller;

import com.example.chat.entities.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")             // /app/message
    @SendTo("/chatroom/public")
    private ChatMessage receivePublicMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/private-message")         // /user/Simona/private
    private ChatMessage receivePrivateMessage(@Payload ChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverName(),
                "/private",
                chatMessage
                );
        return chatMessage;
    }
}
