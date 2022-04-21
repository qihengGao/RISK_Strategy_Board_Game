package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.models.HistoryGame;

import java.util.List;

public class HistoryRoomsAvailableResponse {
    private List<HistoryGame> rooms;

    public HistoryRoomsAvailableResponse(List<HistoryGame> rooms) {
        this.rooms = rooms;
    }

    public List<HistoryGame> getRooms() {
        return rooms;
    }
}
