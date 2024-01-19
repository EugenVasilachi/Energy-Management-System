package com.example.chat.entities;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String senderName;
    private String receiverName;
    private String content;
    private Status status;
}
