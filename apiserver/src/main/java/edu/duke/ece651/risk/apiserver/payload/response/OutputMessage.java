package edu.duke.ece651.risk.apiserver.payload.response;

import java.time.LocalDateTime;
import java.util.Date;

public class OutputMessage {

    private Long from;
    private String message;
    private String topic;

    private LocalDateTime timestamp;

    public OutputMessage(Long from, String message, String topic,LocalDateTime timestamp) {
        this.from = from;
        this.message = message;
        this.topic = topic;
        this.timestamp = timestamp;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

// add getters and setters here
}