package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.order.MoveOrder;
import org.junit.jupiter.api.Test;

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
        doThrow(NullPointerException.class).when(client).writeObject(any());
        GameHandler gameHandler = spy(new GameHandler(client,1,0L));
        MoveOrder order = mock(MoveOrder.class);
        doCallRealMethod().when(gameHandler).letClientReOrder(order);

        gameHandler.letClientReOrder(order);
        doReturn("null").doReturn(null).when(gameHandler).getRecheck_message(any());
        doNothing().when(client).writeObject(any());
        doReturn(order).when(client).readObject();
        gameHandler.letClientReOrder(order);
    }


}