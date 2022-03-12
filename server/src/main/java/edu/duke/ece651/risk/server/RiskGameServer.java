package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.WaitingState;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class RiskGameServer extends Thread {
    private long clientIDCounter;

    private ServerSocket serverSocket;
    private final LinkedList<Client> clientList;




    private final int roomSize;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup, Runnable, String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public RiskGameServer(int roomSize, ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.roomSize = roomSize;
        clientList = new LinkedList<>();
        this.clientIDCounter = 0;
    }

    /**
     * Listen on the serverSocket and waiting for a new client.
     * @throws IOException  Any of the usual Input/Output related exceptions.
     */
    Client acceptANewClient() throws IOException {
        Client tmpClient = new Client(clientIDCounter++, serverSocket.accept());
        System.out.println("New client! Client id: " + tmpClient.getClientID());
        return tmpClient;
    }

    public void run() {

        System.out.printf("Server up. Listening on port: %d.\n",serverSocket.getLocalPort());
        while (true) {
            try {
                Client tmpClient = new Client(clientIDCounter++, serverSocket.accept());
                System.out.println("New client! Client id: " + tmpClient.getClientID());
                clientList.add(tmpClient);
                tmpClient.writeObject(new RiskGameMessage(tmpClient.getClientID(), new WaitingState(), null,
                        String.format("Waiting for game to start. Still need %d player!", roomSize - clientList.size())));

                if (clientList.size() >= roomSize) {
                    Set<Client> players = new LinkedHashSet<>();
                    for (int i = 0; i < roomSize; i++) {
                        players.add(clientList.poll());
                    }
                    new GameHandler(players).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
