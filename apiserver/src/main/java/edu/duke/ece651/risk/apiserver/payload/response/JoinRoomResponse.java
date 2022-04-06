package edu.duke.ece651.risk.apiserver.payload.response;

public class JoinRoomResponse {
    private String message;
    private Long roomIDJoined;

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
     * get room id joined
     * @return room id joined
     */
    public Long getRoomIDJoined() {
        return roomIDJoined;
    }

    /**
     * set room id joined
     * @param roomIDJoined
     */
    public void setRoomIDJoined(Long roomIDJoined) {
        this.roomIDJoined = roomIDJoined;
    }

    /**
     * default constructor
     * @param message
     * @param roomIDJoined
     */
    public JoinRoomResponse(String message, Long roomIDJoined) {
        this.message = message;
        this.roomIDJoined = roomIDJoined;
    }
}
