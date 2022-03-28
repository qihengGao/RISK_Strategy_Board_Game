package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.APIGameHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomsAvailableResponse {

    private List<APIGameHandler> rooms;

    public RoomsAvailableResponse(List<APIGameHandler> res) {
        this.rooms = res;
    }

    public List<APIGameHandler> getRooms() {
        return rooms;
    }


}
