package edu.duke.ece651.risk.apiserver.models;


import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.shared.order.Order;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HistoryGame")
public class HistoryGame {
    Long roomID;
    Long roundNumber;
    APIGameHandler apiGameHandler;

    public HistoryGame(Long roomID, Long roundNumber, APIGameHandler apiGameHandler) {
        this.roomID = roomID;
        this.roundNumber = roundNumber;
        this.apiGameHandler = apiGameHandler;
    }

    public HistoryGame() {
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public Long getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Long roundNumber) {
        this.roundNumber = roundNumber;
    }

    public APIGameHandler getApiGameHandler() {
        return apiGameHandler;
    }

    public void setApiGameHandler(APIGameHandler apiGameHandler) {
        this.apiGameHandler = apiGameHandler;
    }


}
