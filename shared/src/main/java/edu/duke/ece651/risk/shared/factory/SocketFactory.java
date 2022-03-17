package edu.duke.ece651.risk.shared.factory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketFactory {
    /**
     * Create a new Socket instance.
     *
     * @return New Socket instance.
     */
    public Socket createSocket() {
        return new Socket();
    }

    /**
     * Create a new SocketAddress instance with specified hostname and port.
     * @param hostname Server hostname.
     * @param port Server port number.
     * @return An InetSocketAddress created by specified hostname and port.
     */
    public SocketAddress createSocketAddress(String hostname, int port){
        return new InetSocketAddress(hostname, port);
    }


    /**
     * Create a new ObjectInputStream instance with specified inputStream.
     * @param inputStream InputStream.
     * @return An ObjectInputStream created by specified InputStream.
     * @throws IOException Any of the usual Input/Output related exceptions.
     */
    public ObjectInputStream createObjectInputStream(InputStream inputStream) throws IOException {
        return new ObjectInputStream(inputStream);
    }

    /**
     * Create a new ObjectOutputStream instance with specified outputStream.
     * @param outputStream OutputStream.
     * @return An ObjectOutputStream created by specified outputStream.
     * @throws IOException Any of the usual Input/Output related exceptions.
     */
    public ObjectOutputStream createObjectOutputStream(OutputStream outputStream) throws IOException {
        return new ObjectOutputStream(outputStream);
    }
}
