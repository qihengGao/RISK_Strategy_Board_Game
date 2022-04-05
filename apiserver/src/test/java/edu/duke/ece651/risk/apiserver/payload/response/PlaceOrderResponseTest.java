package edu.duke.ece651.risk.apiserver.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaceOrderResponseTest {

    @Test
    public void test_setGet(){
        PlaceOrderResponse response = new PlaceOrderResponse();
        response.setPrompt("prompt");
        assertEquals("prompt", response.getPrompt());
    }
}
