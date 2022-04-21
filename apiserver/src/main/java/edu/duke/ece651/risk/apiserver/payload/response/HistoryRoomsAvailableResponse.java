package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.models.HistoryGame;
import edu.duke.ece651.risk.apiserver.models.HistoryGameConcise;

import java.util.List;

public class HistoryRoomsAvailableResponse {
    private List<HistoryGameConcise> rooms;

    public HistoryRoomsAvailableResponse(List<HistoryGameConcise> rooms) {
        this.rooms = rooms;
    }

    public List<HistoryGameConcise> getRooms() {
        return rooms;
    }
}
