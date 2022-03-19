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

//    public RiskGameMessage getPreviousRiskGameMessage() {
//        return previousRiskGameMessage;
//    }


    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public void setOis(ObjectInput ois) {
        this.ois = ois;
    }

    private ObjectInput ois;
    private final long clientID;

    public void setSocket(Socket socket) {
        this.socket = socket;

    }

    public Socket getSocket() {
        return socket;
    }


    public long getClientID() {
        return clientID;
    }


    public Client(Socket socket, long clientID, ObjectInput objectInputStream, ObjectOutputStream objectOutputStream) {
        this.clientID = clientID;
        this.oos = objectOutputStream;
        this.ois = objectInputStream;
        this.socket = socket;
    }

    public Object readObject() {
        try {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client socket closed.");
        }
        return null;
    }

    public void writeObject(RiskGameMessage o) {
        try {
            previousRiskGameMessage = o;
            oos.reset();
            oos.writeObject(o);
        } catch (IOException e) {
            System.out.println("Client socket closed.");
        }
    }


}
