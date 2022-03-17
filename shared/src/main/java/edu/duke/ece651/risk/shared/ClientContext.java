package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.state.State;

import java.io.*;
import java.net.Socket;
import java.util.TreeMap;

public class ClientContext {
    private State gameState;
    private Socket socket;
    private RISKMap riskMap;
    private long playerID;
    private TreeMap<Long,Color> idToColor;
    private int portNumber;
    private String serverAddress;
    private final SocketFactory socketFactory;
    private final StateFactory stateFactory;

    public StateFactory getStateFactory() {
        return stateFactory;
    }

    private final RiskGameMessageFactory riskGameMessageFactory;



    public ClientContext() {
        socketFactory = new SocketFactory();
        riskGameMessageFactory = new RiskGameMessageFactory();
        stateFactory = new StateFactory();
    }
    public RiskGameMessageFactory getRiskGameMessageFactory() {
        return riskGameMessageFactory;
    }
    public SocketFactory getSocketFactory() {
        return socketFactory;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public TreeMap<Long, Color> getIdToColor() {
        return idToColor;
    }

    public Color getClientColor() {
        return this.idToColor.get(this.playerID);
    }

    public void setIdToColor(TreeMap<Long, Color> idToColor) {
        this.idToColor = idToColor;
    }

    BufferedReader bufferedReader;
    PrintStream out;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public ObjectOutput getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInput getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public State getGameState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public RISKMap getRiskMap() {
        return riskMap;
    }

    public void setRiskMap(RISKMap riskMap) {
        this.riskMap = riskMap;
    }

    public long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
    public void writeObject(Object o) throws IOException {
        oos.reset();
        oos.writeObject(o);

    }

    public void println(String s){
        out.println(s);
    }

    public RiskGameMessage readMessage() throws IOException, ClassNotFoundException {
        return (RiskGameMessage) ois.readObject();
    }
//Also the action list;
}
