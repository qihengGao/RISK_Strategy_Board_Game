package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.WaitingState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class RiskGameServer extends Thread {
    private long clientIDCounter;

    private final ServerSocket serverSocket;
    private final LinkedList<Client> clientList;
    private Map<Long,Client> idToClient;

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
        this.idToClient = new HashMap<>();
    }

    /**
     * Listen on the serverSocket and waiting for a new client.
     *
     * @throws IOException Any of the usual Input/Output related exceptions.
     */
    Client acceptANewClient() throws IOException {
        Client tmpClient = new Client(clientIDCounter++, serverSocket.accept());
        System.out.println("New client! Client id: " + tmpClient.getClientID());
        return tmpClient;
    }

    void addNewClient(Client tmpClient){
        System.out.println("New client! Client id: " + tmpClient.getClientID());
        clientList.add(tmpClient);
        idToClient.put(tmpClient.getClientID(), tmpClient);
        try {
            tmpClient.writeObject(new RiskGameMessage(tmpClient.getClientID(), new WaitingState(), null,
                    String.format("Waiting for game to start. Still need %d player!", roomSize - clientList.size())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {

        System.out.printf("Server up. Listening on port: %d.\n", serverSocket.getLocalPort());
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Client tmpClient = new Client(clientIDCounter++, socket);
                RiskGameMessage riskGameMessage = (RiskGameMessage) tmpClient.readObject();
                if (riskGameMessage.isInitGame()) {
                    addNewClient(tmpClient);
                } else {
                    long oriClientID = riskGameMessage.getClientid();
                    Client oriClient = idToClient.get(oriClientID);
                    if(oriClient!=null && oriClient.getSocket().getInetAddress().equals(socket.getInetAddress())){
                        clientIDCounter--;
                        System.out.println("Successfully restore a socket connection，client id = "+oriClientID);
                        oriClient.setSocket(socket);
                        oriClient.setOis(tmpClient.getOis());
                        oriClient.setOos(tmpClient.getOos());
                        try {
                            oriClient.writeObject(new RiskGameMessage(oriClient.getClientID(), new WaitingState(), null,
                                    String.format("Successfully restore a socket connection，client id = %d\n" +
                                            "Waiting for game to start. Still need %d player!", oriClientID,roomSize - clientList.size())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(oriClient == null)
                            System.out.println("Client try to restore a socket connection, but client id not found."+idToClient.keySet());
                        else
                            System.out.println("Client try to restore a socket connection, but client address didn't match.");
                        addNewClient(tmpClient);

                    }
                }
                if (clientList.size() >= roomSize) {
                    Set<Client> players = new LinkedHashSet<>();
                    for (int i = 0; i < roomSize; i++) {

                        players.add(clientList.poll());
                    }
                    new GameHandler(players).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
