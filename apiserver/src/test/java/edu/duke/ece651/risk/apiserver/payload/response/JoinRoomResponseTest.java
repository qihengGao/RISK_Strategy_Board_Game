package edu.duke.ece651.risk.apiserver.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JoinRoomResponseTest {

    @Test
    public void test_getSet(){
        JoinRoomResponse response = new JoinRoomResponse("", 0L);
        response.setMessage("prompt");
        response.setRoomIDJoined(1L);
        assertEquals("prompt", response.getMessage());
        assertEquals(1L, response.getRoomIDJoined());
    }
}
