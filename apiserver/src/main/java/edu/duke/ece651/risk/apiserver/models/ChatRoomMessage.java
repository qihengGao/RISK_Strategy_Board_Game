package edu.duke.ece651.risk.apiserver.models;


import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("ChatRoomMessages")
public class ChatRoomMessage {

    private Long roomID;
    private String message;

    private Long senderID;

    private String sendTo;

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    private LocalDateTime messageTime;
    public ChatRoomMessage() {
    }

    public ChatRoomMessage(Long roomID, String message, Long senderID, String sendTo,LocalDateTime messageTime) {
        this.roomID = roomID;
        this.message = message;
        this.senderID = senderID;
        this.sendTo = sendTo;
        this.messageTime = messageTime;
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}