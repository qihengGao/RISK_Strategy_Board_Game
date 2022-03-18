package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientHandlerTest {

    @Mock
    ClientHandler clientHandler;
    Socket socket;

    LinkedList<Client> clientList;
    HashMap<Long, GameHandler> roomMap;
    Map<Long, Client> idToClient;

    RiskGameMessage riskGameMessage;
    Client client;



    void testHelper(){

        socket = mock(Socket.class);
        clientList = new LinkedList<>();
        roomMap = spy(new HashMap<>());
        idToClient = new HashMap<>();
        riskGameMessage = mock(RiskGameMessage.class);
        client = mock(Client.class);
        clientHandler = spy(new ClientHandler(client,socket, 0L, roomMap, idToClient));
    }

    @Test
    void run() {
    }

    @Test
    void doRestorePhase() throws IOException {

    }

    @Test
    void tryJoinGameRoom() throws IOException {
        testHelper();



        GameHandler gameHandler = mock(GameHandler.class);
        doReturn(1).when(gameHandler).getRoomSize();
        doReturn(1).when(gameHandler).getCurrentPlayersSize();

        doReturn(gameHandler).when(roomMap).get(any());
        doReturn(false).when(riskGameMessage).isCreateAGameRoom();
        doReturn(1).when(riskGameMessage).getRoomID();

        assertFalse(clientHandler.tryJoinGameRoom(riskGameMessage));


        doReturn(2).when(gameHandler).getRoomSize();
        doReturn(1).doReturn(2).when(gameHandler).getCurrentPlayersSize();
        clientHandler.tryJoinGameRoom(riskGameMessage);
        verify(gameHandler).addPlayer(any());

        doReturn(null).when(roomMap).get(any());
        assertFalse(clientHandler.tryJoinGameRoom(riskGameMessage));
    }

    @Test
    void createNewGameRoom() throws IOException {
        testHelper();


        GameHandler gameHandler = mock(GameHandler.class);
        doReturn(1).when(riskGameMessage).getRoomSize();


        doReturn(gameHandler).when(clientHandler).createGameHandler(any(), any(), any());

        clientHandler.createNewGameRoom(riskGameMessage);

        assertEquals(1, roomMap.size());
        verify(client).writeObject(any());

    }

    @Test
    void addNewClient() throws IOException {

        testHelper();



        doReturn(client).when(clientHandler).createNewClient(any(), any(), any(), any());

        clientHandler.addNewClient();

        assertEquals(1, idToClient.size());
        verify(client).writeObject(any());
    }

    @Test
    void tryRestoreClient() {
    }

    @Test
    void doSelectRoomPhase() throws IOException {
        testHelper();
        doReturn(true).when(riskGameMessage).isCreateAGameRoom();
        clientHandler.doSelectRoomPhase(riskGameMessage);
        verify(clientHandler).createNewGameRoom(any());

        doReturn(false).when(riskGameMessage).isCreateAGameRoom();

        doReturn(true).when(clientHandler).tryJoinGameRoom(riskGameMessage);
        clientHandler.doSelectRoomPhase(riskGameMessage);
        verify(clientHandler).tryJoinGameRoom(any());

        doReturn(false).when(clientHandler).tryJoinGameRoom(riskGameMessage);
        clientHandler.doSelectRoomPhase(riskGameMessage);
        verify(client,atLeast(1)).writeObject(any());


    }
}