package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.state.State;

import java.io.IOException;

public class WaitingState extends State {
    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        contex.getOut().println("Waiting for server to resolve your orders...\n");
        RiskGameMessage messageReceived = (RiskGameMessage) contex.getOis().readObject();

        contex.getOut().println(messageReceived.getPrompt());
        contex.setPlayerID(messageReceived.getClientid());
        contex.setGameState(messageReceived.getCurrentState());
        contex.setRiskMap(messageReceived.getRiskMap());
        contex.setIdToColor(messageReceived.getIdToColor());
        messageReceived.getCurrentState().doAction(contex);
    }
}
