package edu.duke.ece651.risk.server;

import java.io.*;
import java.net.Socket;

public class Client {


    private final long clientID;
    private final Socket socket;
    private final ObjectOutput oos;
    private final ObjectInput ois;

    public long getClientID() {
        return clientID;
    }

    public Socket getSocket() {
        return socket;
    }

    public Client(long clientID, Socket socket) throws IOException {
        this.clientID = clientID;
        this.socket = socket;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void writeObject(Object o) throws IOException {
        oos.writeObject(o);
    }


}
