package edu.duke.ece651.risk.apiserver.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinRoomRequestTest {
    @Test
    void allTests() {
        JoinRoomRequest joinRoomRequest = new JoinRoomRequest();
        joinRoomRequest.setRoomID(5L);
        assertEquals(joinRoomRequest.getRoomID(), 5L);
    }
}