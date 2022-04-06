package edu.duke.ece651.risk.apiserver.payload.request;

import edu.duke.ece651.risk.shared.order.Order;

import java.util.ArrayList;

public class PlaceOrderRequest {
    private Long roomID;
    private ArrayList<Order> orders;

    /**
     * get room id
     * @return room id
     */
    public Long getRoomID() {
        return roomID;
    }

    /**
     * set room id
     * @param roomID
     */
    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    /**
     * get orders
     * @return orders
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * set orders
     * @param orders
     */
    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
