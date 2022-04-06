package edu.duke.ece651.risk.apiserver.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class PlaceUnitRequestTest {
    @Test
    public void test_getSet(){
        PlaceUnitRequest request = new PlaceUnitRequest();
        assertNull(request.getUnitPlaceOrders());
        assertNull(request.getRoomID());
    }
}
