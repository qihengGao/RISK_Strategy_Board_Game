package edu.duke.ece651.risk.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class RiskGameServer extends Thread {
    private long clientIDCounter;

    private final ServerSocket serverSocket;

    private final Map<Long, Client> idToClient;
    private final HashMap<Long, GameHandler> roomMap;

    /**
     *
     * @param serverSocket The ServerSocket we want to listen on.
     */
    public RiskGameServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clientIDCounter = 0;
        this.idToClient = new HashMap<>();
        roomMap = new HashMap<>();
    }


    /**
     * Keep listen on the serverSocket, if there is a socket connection, create a new client handler to handle that.
     * Add 1 to clientIDCounter in each time we accept a new socket.
     */
    public void run() {

        System.out.printf("Server up. Listening on port: %d.\n", serverSocket.getLocalPort());
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ClientHandler(new Client(),socket,clientIDCounter++, roomMap, idToClient).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
