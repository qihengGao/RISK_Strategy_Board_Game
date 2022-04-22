package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.apiserver.models.ChatRoomMessage;
import edu.duke.ece651.risk.apiserver.payload.request.Message;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutputMessageTest {

    @Test
    void allTests() {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        OutputMessage message = new OutputMessage(100L,
                "do not say hello", "unfriendly", localDateTime1);

        assertEquals(message.getFrom(), 100L);
        message.setFrom(0L);
        assertEquals(message.getFrom(), 0L);

        assertEquals(message.getMessage(), "do not say hello");
        message.setMessage("hello");
        assertEquals(message.getMessage(), "hello");

        assertEquals(message.getTopic(), "unfriendly");
        message.setTopic("friendly");
        assertEquals(message.getTopic(), "friendly");

        LocalDateTime localDateTime2 = LocalDateTime.of(2015,
                Month.JULY, 29, 19, 30, 40);
        assertSame(message.getTimestamp(), localDateTime1);
        message.setTimestamp(localDateTime2);
        assertSame(message.getTimestamp(), localDateTime2);

    }
}