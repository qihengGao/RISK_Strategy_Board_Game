package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.models.HistoryGameConcise;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryRoomsAvailableResponseTest {
    @Test
    void all_test() {
        HistoryGameConcise room0 = new HistoryGameConcise(0L, 0L);
        List<HistoryGameConcise> newRooms = new ArrayList<HistoryGameConcise>(Arrays.asList(room0,
                new HistoryGameConcise(1L, 0L)));
        HistoryRoomsAvailableResponse rooms = new HistoryRoomsAvailableResponse(newRooms);
        assertTrue(rooms.getRooms().contains(room0));
    }

}