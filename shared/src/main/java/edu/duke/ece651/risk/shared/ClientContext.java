package edu.duke.ece651.risk.shared;

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
    ObjectOutput oos;
    ObjectInput ois;

    public ObjectOutput getOos() {
        return oos;
    }

    public void setOos(ObjectOutput oos) {
        this.oos = oos;
    }

    public ObjectInput getOis() {
        return ois;
    }

    public void setOis(ObjectInput ois) {
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
//Also the action list;
}
