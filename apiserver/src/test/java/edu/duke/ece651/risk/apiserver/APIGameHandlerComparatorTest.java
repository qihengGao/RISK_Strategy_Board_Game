package edu.duke.ece651.risk.apiserver;

import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class APIGameHandlerComparatorTest {

    @Test
    void compare() {
        APIGameHandlerComparator gameCompare = new APIGameHandlerComparator(300);

        APIGameHandler room1 = new APIGameHandler(3, 1, 0l);
        APIGameHandler room2 = new APIGameHandler(3, 2, 0l);
        APIGameHandler room3 = new APIGameHandler(3, 3, 0l);
        APIGameHandler room4 = new APIGameHandler(3, 4, 0l);

        room1.setAverageElo(500L);
        room2.setAverageElo(100L);
        room3.setAverageElo(500L);
        room4.setAverageElo(304L);

        List<APIGameHandler> roomList = new ArrayList<APIGameHandler>(Arrays.asList(room1, room2, room3, room4));

        Collections.sort(roomList, gameCompare);

        List<Long> sortedID = new ArrayList<Long>(Arrays.asList(4L,1L,3L,2L));

        int i = 0;
        for (APIGameHandler room : roomList){
            assertEquals(room.getRoomID(), sortedID.get(i));
            i++;
        }

    }
}