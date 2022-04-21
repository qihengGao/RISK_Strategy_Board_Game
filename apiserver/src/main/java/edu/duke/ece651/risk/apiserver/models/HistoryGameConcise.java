package edu.duke.ece651.risk.apiserver.models;

public class HistoryGameConcise {
    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    Long roomID;

    public Long getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Long roundNumber) {
        this.roundNumber = roundNumber;
    }

    Long roundNumber;

    public HistoryGameConcise(Long roomID, Long roundNumber) {
        this.roomID = roomID;
        this.roundNumber = roundNumber;
    }

    public HistoryGameConcise() {
    }
}
