package edu.duke.ece651.risk.apiserver.payload.request;

import javax.validation.constraints.NotBlank;


public class CreateRoomRequest {
    @NotBlank
    private int roomSize;

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }
}