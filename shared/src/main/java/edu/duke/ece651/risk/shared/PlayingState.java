package edu.duke.ece651.risk.shared;

import java.io.IOException;

public class PlayingState extends State {


    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
//        RiskGameMessage messageReceived = (RiskGameMessage) contex.getOis().readObject();
//        contex.setPlayerID(messageReceived.getClientid());
//        contex.getOut().println(messageReceived.getPrompt());
//        contex.setGameState(contex.getGameState());
//
//        messageReceived.getCurrentState().doAction(contex);

//        System.out.println("Receiving map from server");

        MapTextView mapTextView = new MapTextView(contex.getRiskMap(),contex.getIdToColor());

        System.out.println(mapTextView.displayMapInit());
        System.out.println(mapTextView.displayMap());

//        System.out.println("Closing socket and terminating program.");
    }
}
