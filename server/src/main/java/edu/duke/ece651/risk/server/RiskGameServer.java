package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.WaitingState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RiskGameServer extends Thread {
    private long clientIDCounter;

    private final ServerSocket serverSocket;
    private final LinkedList<Client> clientList;
    private final Map<Long, Client> idToClient;

    private final int roomSize;

    private final HashMap<Integer, GameHandler> roomMap;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain Thread(ThreadGroup, Runnable, String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public RiskGameServer(int roomSize, ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.roomSize = roomSize;
        clientList = new LinkedList<>();
        this.clientIDCounter = 0;
        this.idToClient = new HashMap<>();
        roomMap = new HashMap<>();
    }

    public void run() {

        System.out.printf("Server up. Listening on port: %d.\n", serverSocket.getLocalPort());
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket,clientIDCounter++,clientList, roomMap, idToClient).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
