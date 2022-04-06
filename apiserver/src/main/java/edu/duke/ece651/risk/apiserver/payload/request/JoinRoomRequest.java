package edu.duke.ece651.risk.apiserver.payload.request;

import jdk.jfr.DataAmount;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class JoinRoomRequest {
    @NotNull
    private long roomID;

    /**
     * get room id
     * @return room id
     */
    public long getRoomID() {
        return roomID;
    }

    /**
     * set room id
     * @param roomID
     */
    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    /**
     * default constructor
     */
    public JoinRoomRequest() {
    }
}
