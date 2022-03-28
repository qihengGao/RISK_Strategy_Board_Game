package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.APIGameHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomsAvailableResponse {

    private Map<Long, APIGameHandler> rooms;

    public RoomsAvailableResponse(Map<Long, APIGameHandler> res) {
        this.rooms = res;
    }

    public Map<Long, APIGameHandler> getRooms() {
        return rooms;
    }


}
