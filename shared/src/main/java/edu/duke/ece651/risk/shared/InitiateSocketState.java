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
        String serverAddress = "localhost";
        int serverPortNumber = 1777;
        Socket socket = new Socket(serverAddress, serverPortNumber);

        contex.setOut(System.out);
        contex.setSocket(socket);
        contex.setOis(new ObjectInputStream(socket.getInputStream()));
        contex.setOos(new ObjectOutputStream(socket.getOutputStream()));



        RiskGameMessage messageReceived = (RiskGameMessage) contex.getOis().readObject();
        contex.setPlayerID(messageReceived.getClientid());
        contex.getOut().println(messageReceived.getPrompt());
        contex.setGameState(contex.getGameState());


        messageReceived.getCurrentState().doAction(contex);
    }
}
