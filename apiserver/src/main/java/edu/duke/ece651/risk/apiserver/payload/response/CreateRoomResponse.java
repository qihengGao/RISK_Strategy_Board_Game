package edu.duke.ece651.risk.apiserver.payload.response;

import java.util.List;

public class CreateRoomResponse {
    private String message;
    private Long roomIDCreated;

    /**
     * get message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * set message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * get room id
     * @return room id
     */
    public Long getRoomIDCreated() {
        return roomIDCreated;
    }

    /**
     * set room id
     * @param roomIDCreated
     */
    public void setRoomIDCreated(Long roomIDCreated) {
        this.roomIDCreated = roomIDCreated;
    }

    /**
     * default constructor
     * @param message
     * @param roomIDCreated
     */
    public CreateRoomResponse(String message, Long roomIDCreated) {
        this.message = message;
        this.roomIDCreated = roomIDCreated;
    }
}
