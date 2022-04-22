package edu.duke.ece651.risk.apiserver.models;

import edu.duke.ece651.risk.apiserver.APIGameHandler;
import edu.duke.ece651.risk.shared.order.AttackOrder;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryOrdersTest {
    @Test
    void allTests() {
        HistoryOrders historyOrders = new HistoryOrders();
        Order order1 = new MoveOrder();
        historyOrders = new HistoryOrders(100L, 100L,  order1);
        assertEquals(historyOrders.getRoomID(), 100L);
        historyOrders.setRoomID(0L);
        assertEquals(historyOrders.getRoomID(), 0L);
        assertEquals(historyOrders.getRoundNumber(), 100L);
        historyOrders.setRoundNumber(0L);
        assertEquals(historyOrders.getRoundNumber(), 0L);

        Order order2 = new AttackOrder();
        assertSame(historyOrders.getOrder(), order1);
        historyOrders.setOrder(order2);
        assertSame(historyOrders.getOrder(), order2);

    }
}