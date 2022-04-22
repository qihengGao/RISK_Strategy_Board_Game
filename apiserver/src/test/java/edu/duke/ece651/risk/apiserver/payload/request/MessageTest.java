package edu.duke.ece651.risk.apiserver.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void allTests() {
        Message message = new Message();
        message.setFrom("user1");
        assertEquals(message.getFrom(), "user1");
        message.setText("Hello");
        assertEquals(message.getText(), "Hello");
    }
}