package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;
import edu.duke.ece651.risk.shared.state.WaitingState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final LinkedList<Client> clientList;
    private final Map<Long, GameHandler> roomMap;
    private final long clientIDCounter;
    private final Map<Long, Client> idToClient;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    boolean finishGameInitiatePhase = false;
    Client client;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup, Runnable, String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public ClientHandler(Socket socket, Long clientIDCounter, LinkedList<Client> clientList, HashMap<Long, GameHandler> roomMap, Map<Long, Client> idToClient) {
        this.socket = socket;
        this.clientList = clientList;
        this.roomMap = roomMap;
        this.clientIDCounter = clientIDCounter;
        this.idToClient = idToClient;
    }

    /**
     * If this thread was constructed using a separate
     * {@code Runnable} run object, then that
     * {@code Runnable} object's {@code run} method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of {@code Thread} should override this method.
     *
     * @see #start()
     * @see #stop()
     * @see #Thread(ThreadGroup, Runnable, String)
     */
    @Override
    public void run() {

        System.out.println("Client Handler start with socket: " + socket.getInetAddress().toString());
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            do {
                RiskGameMessage riskGameMessage = (RiskGameMessage) objectInputStream.readObject();
                switch (riskGameMessage.getClientCurrentStateName()) {
                    case "RestoreState":
                        doRestorePhase(riskGameMessage);
                        break;
                    case "SelectRoomState":
                        doSelectRoomPhase(riskGameMessage);
                        break;
                }
            } while (!finishGameInitiatePhase);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doRestorePhase(RiskGameMessage riskGameMessage) throws IOException {
        if (riskGameMessage.isInitGame()) {
            addNewClient();
            //finishGameInitiatePhase = true;
        } else {

            if (tryRestoreClient(riskGameMessage)) {
                finishGameInitiatePhase = true;
            } else {
                objectOutputStream.writeObject(RiskGameMessageFactory.createRestoreStateMessage("Invalid client ID. Restore failed!"));
            }
        }
    }

    private void doSelectRoomPhase(RiskGameMessage riskGameMessage) throws IOException {
        if (riskGameMessage.isCreateAGameRoom()) {
            createNewGameRoom(riskGameMessage);
            finishGameInitiatePhase = true;
        } else {
            if (tryJoinGameRoom(riskGameMessage)) {
                finishGameInitiatePhase = true;
            } else {
                objectOutputStream.writeObject(RiskGameMessageFactory.createSelectRoomState("Invalid game room ID or room is full. Join failed!"));
            }
        }
    }

    public boolean tryJoinGameRoom(RiskGameMessage riskGameMessage) throws IOException {
        long roomIDToJoin = riskGameMessage.getRoomID();
        synchronized (roomMap) {
            GameHandler roomToJoin = roomMap.get(roomIDToJoin);
            if (roomToJoin != null) {
                if (roomToJoin.getCurrentPlayersSize() == roomToJoin.getRoomSize()) {
                    return false;
                } else {
                    roomToJoin.addPlayer(client);
                    client.writeObject(new RiskGameMessage(client.getClientID(), new WaitingState(), null,
                            String.format("RoomID: %d Waiting for game to start. Still need %d player!", roomToJoin.getRoomID(), roomToJoin.getRoomSize() - roomToJoin.getCurrentPlayersSize())));
                    if (roomToJoin.getCurrentPlayersSize() == roomToJoin.getRoomSize())
                        roomToJoin.start();
                    return true;
                }
            } else {
                System.out.println("Room ID not found! RoomID: " + roomIDToJoin);
                return false;
            }
        }
    }

    public void createNewGameRoom(RiskGameMessage riskGameMessage) throws IOException {
        synchronized (roomMap) {
            long roomID = roomMap.size();
            System.out.println("Create New Game room! Room ID: " + roomID);
            GameHandler gameHandler = new GameHandler(client, riskGameMessage.getRoomSize(), roomID);
            roomMap.put(roomID, gameHandler);
            client.writeObject(new RiskGameMessage(client.getClientID(), new WaitingState(), null,
                    String.format("RoomID: %d Waiting for game to start. Still need %d player!", gameHandler.getRoomID(), gameHandler.getRoomSize() - gameHandler.getCurrentPlayersSize())));
        }
    }

    public void addNewClient() throws IOException {
        System.out.println("New client! Client id: " + clientIDCounter);
        Client tmp = new Client(socket, clientIDCounter, objectInputStream, objectOutputStream);
        client = tmp;
        synchronized (clientList) {
            clientList.add(tmp);
            idToClient.put(clientIDCounter, tmp);


//            tmp.writeObject(new RiskGameMessage(tmp.getClientID(), new WaitingState(), null,
//                    String.format("Waiting for game to start. Still need %d player!", 3 - clientList.size())));
            tmp.writeObject(RiskGameMessageFactory.createSelectRoomState(""));

//            if (clientList.size() >= 3) {
//                Set<Client> players = new LinkedHashSet<>();
//                for (int i = 0; i < 3; i++) {
//
//                    players.add(clientList.poll());
//                }
//                new GameHandler(players).start();
//            }
        }
    }


    public boolean tryRestoreClient(RiskGameMessage riskGameMessage) throws IOException {
        long oriClientID = riskGameMessage.getClientid();
        synchronized (idToClient) {
            Client oriClient = idToClient.get(oriClientID);
            if (oriClient != null && oriClient.getSocket().getInetAddress().equals(socket.getInetAddress())) {
                System.out.println("Successfully restore a socket connection, client id = " + oriClientID);
                oriClient.setSocket(socket);
                oriClient.setOis(objectInputStream);
                oriClient.setOos(objectOutputStream);
                if (oriClient.getPreviousRiskGameMessage() == null || oriClient.getPreviousRiskGameMessage().getCurrentState() instanceof WaitingState)
                    oriClient.writeObject(new RiskGameMessage(oriClient.getClientID(), new WaitingState(), null,
                            String.format("Successfully restore a socket connection, client id = %d\n" +
                                    "Waiting for game to start. Still need %d player!", oriClientID, 3 - clientList.size())));
                else
                    oriClient.writeObject(oriClient.getPreviousRiskGameMessage());
                return true;
            } else {
                if (oriClient == null)
                    System.out.println("Client try to restore a socket connection, but client id not found." + oriClientID);
                else
                    System.out.println("Client try to restore a socket connection, but client address didn't match.");
                return false;

            }
        }
    }
}
