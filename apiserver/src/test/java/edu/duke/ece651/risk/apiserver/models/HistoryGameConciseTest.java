package edu.duke.ece651.risk.apiserver.models;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryGameConciseTest {
    @Test
    void allTests() {
        HistoryGameConcise game = new HistoryGameConcise();
        game = new HistoryGameConcise(100L, 100L);
        assertEquals(game.getRoomID(), 100L);
        game.setRoomID(0L);
        assertEquals(game.getRoomID(), 0L);
        assertEquals(game.getRoundNumber(), 100L);
        game.setRoundNumber(0L);
        assertEquals(game.getRoundNumber(), 0L);
    }
}