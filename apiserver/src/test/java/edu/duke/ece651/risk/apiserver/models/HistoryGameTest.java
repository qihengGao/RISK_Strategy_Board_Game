package edu.duke.ece651.risk.apiserver.models;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryGameTest {

    @Test
    void allTests() {
        HistoryGame game = new HistoryGame();
        APIGameHandler gameHandler1 = new APIGameHandler();
        game = new HistoryGame(100L, 100L,  gameHandler1);
        assertEquals(game.getRoomID(), 100L);
        game.setRoomID(0L);
        assertEquals(game.getRoomID(), 0L);
        assertEquals(game.getRoundNumber(), 100L);
        game.setRoundNumber(0L);
        assertEquals(game.getRoundNumber(), 0L);
        APIGameHandler gameHandler = new APIGameHandler();
        assertSame(game.getApiGameHandler(), gameHandler1);
        game.setApiGameHandler(gameHandler);
        assertSame(game.getApiGameHandler(), gameHandler);

    }
}