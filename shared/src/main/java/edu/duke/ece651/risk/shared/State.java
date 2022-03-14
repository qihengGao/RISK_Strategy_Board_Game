package edu.duke.ece651.risk.shared;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import edu.duke.ece651.risk.shared.SocketFactory.*;

public abstract class State implements Serializable {

    public abstract void doAction(ClientContext contex) throws IOException, ClassNotFoundException;

    /**
     * Read server port number from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Port number read from input source.
     * @throws IOException           if read an empty line or the input source in null.
     * @throws NumberFormatException if the line we read from input source is not an integer.
     */
    public static int readServerPort(String prompt, BufferedReader inputReader, PrintStream out) throws IOException, NumberFormatException {
        out.print(prompt + "\n");
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("Invalid input: Empty line\n");
        }
        return Integer.parseInt(s);

    }

    /**
     * Read server address from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Server address read from input source.
     * @throws IOException if read an empty line or the input source in null.
     */
    public static String readServerAddress(String prompt, BufferedReader inputReader, PrintStream out) throws IOException {
        out.print(prompt + "\n");
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("Invalid input: Empty line\n");
        }
        return s;

    }




    /**
     * Connect to Risk Game server with specified server address, port number in ClientContext. Then set the ObjectOutputStream
     * and ObjectInputStream for ClientContext.
     * Max retry 10 times if connection false.
     *
     * @param context Client's context.
     * @return true if the connected, otherwise false.
     */
    public boolean connectToServer(ClientContext context) {

        int timeoutCount = 10;
        SocketFactory socketFactory = context.getSocketFactory();
        Socket socket = socketFactory.createSocket();
        SocketAddress socketAddress = socketFactory.createSocketAddress(context.getServerAddress(), context.getPortNumber());
        while (!socket.isConnected() && timeoutCount-- > 0) {
            try {
                socket.connect(socketAddress);
                context.setSocket(socket);
                context.setOis(socketFactory.createObjectInputStream(socket.getInputStream()));
                context.setOos(socketFactory.createObjectOutputStream(socket.getOutputStream()));
            } catch (IOException ignored) {
            }
        }

        return context.getSocket() != null && context.getSocket().isConnected();

    }




}
