package edu.duke.ece651.risk.apiserver.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class CreateRoomRequest {
    @NotNull
    @Min(2)
    @Max(5)
    private int roomSize;

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }
}