package edu.duke.ece651.risk.shared;

import java.io.*;
import java.net.Socket;


public class InitiateSocketState extends State {



    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        //TODO
        //Create a socket

//        String serverAddress = readServerAddress("",contex.getBufferedReader(),contex.getOut());
//        int serverPortNumber = readServerPort("",contex.getBufferedReader(),contex.getOut());
        
        // 1. set sockets
        String serverAddress = readServerAddress(contex,"Please type in server port number");
        int serverPortNumber = readServerPort(contex,"Please type in server address");
        contex.setServerAddress(serverAddress);

        //For integration test, we try to run the test with a random port, and we already set the port number.
        //So if port number is already set, skip this process.
        if(contex.getPortNumber()==0)
            contex.setPortNumber(serverPortNumber);
        Long clientID = -1L;
        try {
            clientID = readClientID(contex, "Please type in you client ID to restore previous game, or Enter to start a new game.");
        }catch (NumberFormatException | IOException exception){

        }
//        Socket socket = new Socket(serverAddress, serverPortNumber);
//        contex.setSocket(socket);
//        // 2. try connect to server
//
//        contex.setSocket(socket);
//        contex.setOis(new ObjectInputStream(socket.getInputStream()));
//        contex.setOos(new ObjectOutputStream(socket.getOutputStream()));
        initConnectToServer(contex);
        
        // 2. wait for server for context and parse
        RiskGameMessage messageReceived = (RiskGameMessage) contex.getOis().readObject();
        contex.setPlayerID(messageReceived.getClientid());
        contex.getOut().println(messageReceived.getPrompt());
        contex.setGameState(contex.getGameState());

        // 3. execute the next state instructed by the server's context
        messageReceived.getCurrentState().doAction(contex);
    }
}
