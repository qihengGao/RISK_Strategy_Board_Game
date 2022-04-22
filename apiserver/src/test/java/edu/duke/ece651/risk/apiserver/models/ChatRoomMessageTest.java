package edu.duke.ece651.risk.apiserver.models;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomMessageTest {
    @Test
    void allTests() {
        ChatRoomMessage roomMessage = new ChatRoomMessage();
        LocalDateTime localDateTime1 = LocalDateTime.now();
        roomMessage = new ChatRoomMessage(100L,
                "do not say hello", 20L, "Player", localDateTime1);
        assertEquals(roomMessage.getRoomID(), 100L);
        roomMessage.setRoomID(0L);
        assertEquals(roomMessage.getRoomID(), 0L);

        assertEquals(roomMessage.getMessage(), "do not say hello");
        roomMessage.setMessage("hello");
        assertEquals(roomMessage.getMessage(), "hello");

        assertEquals(roomMessage.getSenderID(), 20L);
        roomMessage.setSenderID(0L);
        assertEquals(roomMessage.getSenderID(), 0L);

        assertEquals(roomMessage.getSendTo(), "Player");
        roomMessage.setSendTo("not player");
        assertEquals(roomMessage.getSendTo(), "not player");

        LocalDateTime localDateTime2 = LocalDateTime.of(2015,
                Month.JULY, 29, 19, 30, 40);
        assertSame(roomMessage.getMessageTime(), localDateTime1);
        roomMessage.setMessageTime(localDateTime2);
        assertSame(roomMessage.getMessageTime(), localDateTime2);

    }
}