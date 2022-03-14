package edu.duke.ece651.risk.server;

import java.io.*;
import java.net.Socket;

public class Client {


    private final long clientID;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    private Socket socket;
    private  ObjectOutputStream oos;
    private  ObjectInputStream ois;

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

    public Object readObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void writeObject(Object o) throws IOException {
        oos.reset();
        oos.writeObject(o);
    }


}
