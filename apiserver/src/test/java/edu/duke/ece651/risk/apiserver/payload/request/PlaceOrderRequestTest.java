package edu.duke.ece651.risk.apiserver.payload.request;

import edu.duke.ece651.risk.shared.order.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaceOrderRequestTest {

    @Test
    public void test_setGet(){
        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setRoomID(0L);
        assertEquals(0L, request.getRoomID());

        ArrayList<Order> orders= new ArrayList<>();
        request.setOrders(orders);
        assertEquals(orders, request.getOrders());
    }
}
