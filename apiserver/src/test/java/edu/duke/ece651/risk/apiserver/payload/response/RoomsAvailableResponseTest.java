package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomsAvailableResponseTest {

    @Test
    public void test_setGet(){
        List<APIGameHandler> handlers = new LinkedList<>();
        RoomsAvailableResponse response = new RoomsAvailableResponse(handlers);
        assertEquals(handlers, response.getRooms());
    }
}
