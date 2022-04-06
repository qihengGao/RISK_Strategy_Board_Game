package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.APIGameHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomsAvailableResponse {
    private List<APIGameHandler> rooms;

    /**
     * default constructor
     * @param res
     */
    public RoomsAvailableResponse(List<APIGameHandler> res) {
        this.rooms = res;
    }

    /**
     * get rooms
     * @return list of rooms
     */
    public List<APIGameHandler> getRooms() {
        return rooms;
    }
}
