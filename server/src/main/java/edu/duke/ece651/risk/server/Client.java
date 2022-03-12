package edu.duke.ece651.risk.server;

import java.io.*;
import java.net.Socket;

public class Client {


    private final long clientID;
    private final Socket socket;
    private  ObjectOutput oos;
    private  ObjectInput ois;

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
        oos.writeObject(o);
    }


}
