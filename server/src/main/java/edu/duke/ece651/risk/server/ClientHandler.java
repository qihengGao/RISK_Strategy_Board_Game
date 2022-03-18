package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;
import edu.duke.ece651.risk.shared.state.WaitingState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final Map<Long, GameHandler> roomMap;
    private final long clientIDCounter;
    private final Map<Long, Client> idToClient;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    boolean finishGameInitiatePhase = false;
    Client client;

    /**
     * @param socket          The socket we need to handle with.
     * @param clientIDCounter client ID assign to this socket. Warning: may be depreciated when the client want to restore previous game.
     * @param roomMap         A Map which store all the game room.
     * @param idToClient      A mao between client ID and client itself.
     */
    public ClientHandler(Client client,Socket socket, Long clientIDCounter, HashMap<Long, GameHandler> roomMap, Map<Long, Client> idToClient) {
        this.socket = socket;
        this.roomMap = roomMap;
        this.clientIDCounter = clientIDCounter;
        this.idToClient = idToClient;
        this.client = client;
    }

    /**
     * Create Object Stream with the socket. And handle all the state transaction before game start.
     */
    @Override
    public void run() {

        System.out.println("Client Handler start with socket: " + socket.getInetAddress().toString());
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            client.setOos(objectOutputStream);
            client.setOis(objectInputStream);

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


    /**
     * Do restore connection logic, based on the RiskGameMessage send from client.
     *
     * @param riskGameMessage RiskGameMessage which send from the client contains the InitGame flag and optional clientID to restore with.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void doRestorePhase(RiskGameMessage riskGameMessage) throws IOException {
        if (riskGameMessage.isInitGame()) {
            addNewClient();
        } else {

            if (tryRestoreClient(riskGameMessage)) {

                //TODO Considering the situation that loop in restoreState.
                finishGameInitiatePhase = true;
            } else {
                objectOutputStream.writeObject(RiskGameMessageFactory.createRestoreStateMessage("Invalid client ID. Restore failed!"));
            }
        }
    }


    /**
     * Do room related logic, based on the RiskGameMessage send from client.
     *
     * @param riskGameMessage RiskGameMessage which send from the client contains the createAGameRoom flag and optional room ID to join.
     * @throws IOException Any problem related to the input/output stream.
     */
    void doSelectRoomPhase(RiskGameMessage riskGameMessage) throws IOException {
        if (riskGameMessage.isCreateAGameRoom()) {
            createNewGameRoom(riskGameMessage);
            finishGameInitiatePhase = true;
        } else {
            if (tryJoinGameRoom(riskGameMessage)) {
                finishGameInitiatePhase = true;
            } else {
                client.writeObject(RiskGameMessageFactory.createSelectRoomState("Invalid game room ID or room is full. Join failed!"));
            }
        }
    }

    /**
     * Try to join an existed game room.
     *
     * @param riskGameMessage RiskGameMessage which send from the client contains the room ID which client want to join.
     * @return True if successfully join the specified room, otherwise false.
     * @throws IOException Any problem related to the input/output stream.
     */
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
                    //If the game room is full, start the game.
                    //TODO Start the game handler in other thread, it is not proper to run game handler on a ClientHandler thread.
                    if (roomToJoin.getCurrentPlayersSize() == roomToJoin.getRoomSize())
                        roomToJoin.start();

                }
            } else {
                System.out.println("Room ID not found! RoomID: " + roomIDToJoin);
                return false;
            }
        }
        return true;
    }


    /**
     * Create a new game room, set the game room size based on the message we receive from client.
     *
     * @param riskGameMessage RiskGameMessage which send from the client contains the roomSize.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void createNewGameRoom(RiskGameMessage riskGameMessage) throws IOException {

        //Here is a naive solution for multi-thread locking.
        //Need better solution.
        //We lock the roomMap object until we add a new pair(roomID, GameHandler) to it.
        synchronized (roomMap) {
            long roomID = roomMap.size();
            System.out.println("Create New Game room! Room ID: " + roomID);
            GameHandler gameHandler = createGameHandler(client, riskGameMessage.getRoomSize(), roomID);
            roomMap.put(roomID, gameHandler);

            //Require client go to WaitingState.
            client.writeObject(new RiskGameMessage(client.getClientID(), new WaitingState(), null,
                    String.format("RoomID: %d Waiting for game to start. Still need %d player!", gameHandler.getRoomID(), gameHandler.getRoomSize() - gameHandler.getCurrentPlayersSize())));
        }
    }

    protected GameHandler createGameHandler(Client client, Integer roomSize, Long roomID){
        return new GameHandler(client, roomSize, roomID);
    }

    /**
     * Create a new client from this socket, then add it to the clientList.
     *
     * @throws IOException Any problem related to the input/output stream.
     */
    public void addNewClient() throws IOException {
        System.out.println("New client! Client id: " + clientIDCounter);
        Client tmp = createNewClient(socket, clientIDCounter, objectInputStream, objectOutputStream);
        client = tmp;
        synchronized (idToClient) {
            idToClient.put(clientIDCounter, tmp);

            //Require client go to SelectRoomState.
            tmp.writeObject(RiskGameMessageFactory.createSelectRoomState(""));


        }
    }

    public Client createNewClient(Socket socket, Long clientIDCounter, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        return new Client(socket, clientIDCounter, objectInputStream, objectOutputStream);
    }


    /**
     * Try to restore a client based on the information client provide in riskGameMessage.
     *
     * @param riskGameMessage RiskGameMessage which send from the client contains the previous client ID.
     * @return True if successfully restore the client, otherwise false.
     * @throws IOException Any problem related to the input/output stream.
     */
    public boolean tryRestoreClient(RiskGameMessage riskGameMessage) throws IOException {
        long oriClientID = riskGameMessage.getClientid();
        synchronized (idToClient) {
            Client oriClient = idToClient.get(oriClientID);
            //If we successfully find a client object with specified client ID, and the oriClient and this client
            //have the same ip address.
            if (oriClient != null && oriClient.getSocket().getInetAddress().equals(socket.getInetAddress())) {
                System.out.println("Successfully restore a socket connection, client id = " + oriClientID);

                //Reset the oriClient's socket.
                oriClient.setSocket(socket);
                oriClient.setOis(objectInputStream);
                oriClient.setOos(objectOutputStream);

                //If the oriClient in InitiateSocketState( getPreviousRiskGameMessage()==null ), server need redirect client to SelectRoomState.
                //IF the oriClient in WaitingState, server need redirect client to WaitingState again to update the prompt(How many player still need to start the game).
                //If the oriClient in any other State, redirect the client to the state of previous RiskGameMessage(May need more handle in the feature).
                //TODO
                if (oriClient.getPreviousRiskGameMessage() == null || oriClient.getPreviousRiskGameMessage().getCurrentState() instanceof WaitingState)
                    oriClient.writeObject(new RiskGameMessage(oriClient.getClientID(), new WaitingState(), null,
                            String.format("Successfully restore a socket connection, client id = %d\n" +
                                    "Waiting for game to start. Still need %d player!", -1)));
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
