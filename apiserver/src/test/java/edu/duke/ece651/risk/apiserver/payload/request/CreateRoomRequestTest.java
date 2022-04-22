package edu.duke.ece651.risk.apiserver.payload.request;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.apiserver.models.HistoryGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateRoomRequestTest {
    @Test
    void allTests() {
        CreateRoomRequest createRoomRequest = new CreateRoomRequest();
        createRoomRequest.setRoomSize(5);
        assertEquals(createRoomRequest.getRoomSize(), 5);

        createRoomRequest.setCompetitive(true);
        assertTrue(createRoomRequest.isCompetitive());

    }

}