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

    private boolean competitive;

    /**
     * get room size
     * @return room size
     */
    public int getRoomSize() {
        return roomSize;
    }

    /**
     * set room size
     * @param roomSize
     */
    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }


    public boolean isCompetitive() {
        return competitive;
    }

    public void setCompetitive(boolean competitive) {
        this.competitive = competitive;
    }

}