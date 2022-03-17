package edu.duke.ece651.risk.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RiskGameServerTest {


    @Test
    void run() throws IOException, InterruptedException {
        ServerSocket serverSocket = mock(ServerSocket.class);
        Socket socket = mock(Socket.class);

        doReturn(socket).doThrow(IOException.class).when(serverSocket).accept();

        new RiskGameServer(serverSocket).start();
        TimeUnit.MILLISECONDS.sleep(10);





    }
}