package edu.duke.ece651.risk.shared;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public abstract class State implements Serializable {

    public abstract void doAction(ClientContext contex) throws IOException, ClassNotFoundException;

    /**
     * Read line from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return A line read from input source.
     * @throws IOException           if read an empty line or the input source in null.

     */
    public String readLine(ClientContext context, String prompt) throws IOException{
        BufferedReader inputReader = context.getBufferedReader();
        PrintStream out = context.getOut();
        out.print(prompt + "\n");
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException("Invalid input: Empty line\n");
        }
        return s;
    }

    /**
     * Read server port number from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Port number read from input source.
     * @throws IOException           if read an empty line or the input source in null.
     * @throws NumberFormatException if the line we read from input source is not an integer.
     */
    public int readServerPort(ClientContext context, String prompt) throws IOException, NumberFormatException {

        return Integer.parseInt(readLine(context,prompt));

    }

    /**
     * Read server address from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Server address read from input source.
     * @throws IOException if read an empty line or the input source in null.
     */
    public String readServerAddress(ClientContext context, String prompt) throws IOException {
        return readLine(context,prompt);

    }

    /**
     * Read client id from input source.
     *
     * @param prompt is a friendly advise for user.
     * @return Client ID read from input source.
     * @throws IOException           if read an empty line or the input source in null.
     * @throws NumberFormatException if the line we read from input source is not a Long.
     */
    public Long readClientID(ClientContext context, String prompt) throws IOException, NumberFormatException {

        return Long.parseLong(readLine(context,prompt));

    }


    /**
     * Connect to Risk Game server with specified server address, port number in ClientContext. Then set the ObjectOutputStream
     * and ObjectInputStream for ClientContext.
     * Max retry 10 times if connection false.
     *
     * @param context Client's context.
     * @throws IOException Any of the usual Input/Output related exceptions.
     */
    public void connectToServer(ClientContext context) throws IOException {

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

        if(!context.getSocket().isConnected()){
            throw  new IOException("Connection failed.");
        }

    }

    /**
     * Reconnect to server and send reconnect message.
     * @param context Client's context.
     * @return true if the connected, otherwise false.
     */
    public boolean reconnectToServer(ClientContext context) {
        try {
            connectToServer(context);
            RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
            context.writeObject(riskGameMessageFactory.createReconnectMessage(context));

        }catch (IOException ignored){
            return false;
        }
        return true;
    }


    /**
     * Init socket then send the init connection message to server.
     *
     * @param context Client's context.
     * @return true if the connected, otherwise false.
     */
    public boolean initConnectToServer(ClientContext context) {

        try {
            connectToServer(context);
            RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
            context.writeObject(riskGameMessageFactory.createInitMessage());

        }catch (IOException ignored){
            return false;
        }
        return true;
    }


    /**
     * Write object to server using the ObjectOutPutStream in ClientContext.
     *
     * @param context The ClientContext which contain the ObjectOutPutStream.
     * @param object  The object which send to the server.
     * @throws IOException Any of the usual Input/Output related exceptions.
     */
    public void writeObject(ClientContext context, Object object) throws IOException {
        try {
            context.writeObject(object);
        } catch (IOException e) {
            if (!reconnectToServer(context)) {
                throw new IOException("Socket closed and reconnect failed.");
            } else {
                writeObject(context, object);
            }
        }


    }


}
