package edu.duke.ece651.risk.server;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RiskGameServerTest {


    @Test
    void test_(){


    }

//    @Test
//    void createServerSocket() throws IOException {
//        final int testPort = 9001;
//        RiskGameServer riskGameServer = new RiskGameServer(3);
//        assertNotNull(riskGameServer);
//
//        ServerSocket testServerSocket = riskGameServer.createServerSocket();
//        assertEquals(testServerSocket.getLocalPort(), testPort);
//    }

    @Test
    void acceptANewClient() throws IOException {




        ServerSocket mockServerSocket = mock(ServerSocket.class);
        Socket mockSocket = mock(Socket.class);
        InputStream mockInputStream = mock(InputStream.class, Mockito.CALLS_REAL_METHODS);
        OutputStream mockOutputStream = mock(OutputStream.class);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        when(mockServerSocket.accept()).thenReturn(mockSocket);




        RiskGameServer riskGameServer = new RiskGameServer(3,mockServerSocket);

        Client client = riskGameServer.acceptANewClient();

        assertEquals(client.getClientID(),0);



    }

    @Test
    void run() throws IOException {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        Socket mockSocket = mock(Socket.class);
        InputStream mockInputStream = mock(InputStream.class, Mockito.CALLS_REAL_METHODS);
        OutputStream mockOutputStream = mock(OutputStream.class);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        when(mockServerSocket.accept()).thenReturn(mockSocket);




        RiskGameServer riskGameServer = new RiskGameServer(3,mockServerSocket);
        riskGameServer.start();
        verify(mockServerSocket).accept();

    }
}