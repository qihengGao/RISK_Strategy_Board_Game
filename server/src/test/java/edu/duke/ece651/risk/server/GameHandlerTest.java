package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.MoveOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameHandlerTest {

    @Test
    void findClientByID() {
        Client client = mock(Client.class);
        doReturn(1L).when(client).getClientID();
        GameHandler gameHandler = spy(new GameHandler(client,1,0L));
        doCallRealMethod().when(gameHandler).findClientByID(0L);
        assertNull(gameHandler.findClientByID(0L));
    }

    @Test
    void letClientReOrder() throws IOException, ClassNotFoundException {
        Client client = mock(Client.class);
        doThrow(IOException.class).doThrow(NullPointerException.class).when(client).writeObject(any());
        GameHandler gameHandler = spy(new GameHandler(client,1,0L));
        MoveOrder order = mock(MoveOrder.class);
        doCallRealMethod().when(gameHandler).letClientReOrder(order,"");

        gameHandler.letClientReOrder(order,"");
        gameHandler.letClientReOrder(order,"");
    }
}