package edu.duke.ece651.risk.shared;

import java.io.IOException;

public class WaitingState extends State{
    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        RiskGameMessage messageReceived = (RiskGameMessage) contex.getOis().readObject();

        contex.getOut().println(messageReceived.getPrompt());
        contex.setGameState(messageReceived.getCurrentState());
        contex.setRiskMap(messageReceived.getRiskMap());
        messageReceived.getCurrentState().doAction(contex);
    }
}
