package edu.duke.ece651.risk.apiserver.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageResponseTest {

    @Test
    public void test_getSet(){
        MessageResponse response = new MessageResponse("");
        response.setMessage("message");
        assertEquals("message", response.getMessage());
    }
}
