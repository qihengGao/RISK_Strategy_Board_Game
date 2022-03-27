package edu.duke.ece651.risk.apiserver.payload.request;

import edu.duke.ece651.risk.shared.order.Order;

import java.util.ArrayList;

public class PlaceOrderRequest {
    private Long roomID;

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    private ArrayList<Order> orders;

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
