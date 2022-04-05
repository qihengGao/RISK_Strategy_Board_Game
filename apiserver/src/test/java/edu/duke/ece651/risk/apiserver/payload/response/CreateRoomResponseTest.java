package edu.duke.ece651.risk.apiserver.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateRoomResponseTest {

    @Test
    public void test_setGet(){
        CreateRoomResponse response = new CreateRoomResponse("", 0L);
        response.setMessage("prompt");
        response.setRoomIDCreated(1L);

        assertEquals("prompt", response.getMessage());
        assertEquals(1L, response.getRoomIDCreated());
    }
}
