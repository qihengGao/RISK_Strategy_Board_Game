package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;

import java.io.*;
import java.net.Socket;

public class Client {


    private Socket socket;
    private  ObjectOutputStream oos;
    private RiskGameMessage previousRiskGameMessage;

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

    private  ObjectInputStream ois;
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



    public Client(long clientID, Socket socket) throws IOException {
        this.clientID = clientID;
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }catch (StreamCorruptedException ignored){
            //When we mock InputStream, the default header is 000000, which will cause ObjectInputStream throw
            //StreamCorruptedException.
        }
    }
    public Client(Socket socket, long clientID, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream){
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
