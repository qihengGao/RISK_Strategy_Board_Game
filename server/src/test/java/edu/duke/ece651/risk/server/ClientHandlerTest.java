package edu.duke.ece651.risk.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ClientHandlerTest {

    @Mock
    ClientHandler clientHandler;
    Socket socket;

    LinkedList<Client> clientList;
    HashMap<Long, GameHandler> roomMap;
    Map<Long, Client> idToClient;

    @BeforeAll
    void testHelper(){
        clientHandler = mock(ClientHandler.class);
        socket = mock(Socket.class);
        clientList = new LinkedList<>();
        roomMap = new HashMap<>();
        idToClient = new HashMap<>();
    }

    @Test
    void run() {
    }

    @Test
    void doRestorePhase() {
    }

    @Test
    void tryJoinGameRoom() {
    }

    @Test
    void createNewGameRoom() {
    }

    @Test
    void addNewClient() {
    }

    @Test
    void tryRestoreClient() {
    }
}