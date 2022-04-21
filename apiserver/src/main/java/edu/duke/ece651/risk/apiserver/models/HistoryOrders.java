package edu.duke.ece651.risk.apiserver.models;


import edu.duke.ece651.risk.shared.order.Order;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HistoryOrders")
public class HistoryOrders {
    Long roomID;

    public HistoryOrders() {
    }

    public HistoryOrders(Long roomID, Long roundNumber, Order order) {
        this.roomID = roomID;
        this.roundNumber = roundNumber;
        this.order = order;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    Long roundNumber;
    Order order;

}
