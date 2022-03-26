package edu.duke.ece651.risk.apiserver.payload.request;

import jdk.jfr.DataAmount;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class JoinRoomRequest {
    @NotNull
    private long roomID;

    public long getRoomID() {

        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }



    public JoinRoomRequest() {
    }
}
