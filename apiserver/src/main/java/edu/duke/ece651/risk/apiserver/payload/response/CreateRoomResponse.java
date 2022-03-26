package edu.duke.ece651.risk.apiserver.payload.response;

import java.util.List;

public class CreateRoomResponse {
    private String message;
    private Long roomIDCreated;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRoomIDCreated() {
        return roomIDCreated;
    }

    public void setRoomIDCreated(Long roomIDCreated) {
        this.roomIDCreated = roomIDCreated;
    }

    public CreateRoomResponse(String message, Long roomIDCreated) {
        this.message = message;
        this.roomIDCreated = roomIDCreated;
    }
}
