package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectOutputStream oos;
    private RiskGameMessage previousRiskGameMessage;

    public Client() {
        this.clientID = -1;
    }

    public RiskGameMessage getPreviousRiskGameMessage() {
        return previousRiskGameMessage;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    private ObjectInputStream ois;
    private final long clientID;

    public void setSocket(Socket socket) {
        this.socket = socket;

    }

    public Socket getSocket() {
        return socket;
    }


    public Long getClientID() {
        return clientID;
    }

    public Client(Socket socket, long clientID, ObjectInputStream objectInputStream,
            ObjectOutputStream objectOutputStream) {
        this.clientID = clientID;
        this.oos = objectOutputStream;
        this.ois = objectInputStream;
        this.socket = socket;
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void writeObject(RiskGameMessage o) throws IOException {
        previousRiskGameMessage = o;
        oos.reset();
        oos.writeObject(o);
    }

}
