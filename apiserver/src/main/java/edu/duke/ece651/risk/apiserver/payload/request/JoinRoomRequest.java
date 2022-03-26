package edu.duke.ece651.risk.apiserver.payload.request;

import javax.validation.constraints.NotBlank;

public class JoinRoomRequest {
    @NotBlank
    private Long roomID;

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public JoinRoomRequest(Long roomID) {
        this.roomID = roomID;
    }
}
