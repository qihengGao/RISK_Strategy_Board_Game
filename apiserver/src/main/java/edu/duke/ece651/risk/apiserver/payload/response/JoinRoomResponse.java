package edu.duke.ece651.risk.apiserver.payload.response;

public class JoinRoomResponse {
    private String message;
    private Long roomIDJoined;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRoomIDJoined() {
        return roomIDJoined;
    }

    public void setRoomIDJoined(Long roomIDJoined) {
        this.roomIDJoined = roomIDJoined;
    }

    public JoinRoomResponse(String message, Long roomIDJoined) {
        this.message = message;
        this.roomIDJoined = roomIDJoined;
    }
}
