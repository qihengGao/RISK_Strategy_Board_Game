package edu.duke.ece651.risk.shared;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class RestoreState extends State{

    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        String command = readChoice(contex, "Please type in you client ID to restore previous game, or Enter to start a new game.",
                "Invalid command!", Pattern.compile("^([1-9]\\d*|0)$|^$",Pattern.CASE_INSENSITIVE));
        if(command.equals("")){
            initGame(contex);
        }else{
            Long oriClientID = Long.parseLong(command);
            restoreGame(contex, oriClientID);
        }
        RiskGameMessage riskGameMessage = (RiskGameMessage) contex.getOis().readObject();
        contex.getOut().println(riskGameMessage.getPrompt());
        contex.setGameState(riskGameMessage.getCurrentState());
        riskGameMessage.getCurrentState().doAction(contex);
    }

    public void initGame(ClientContext context) throws IOException {
        RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
        context.writeObject(riskGameMessageFactory.createInitMessage());
    }

    public void restoreGame(ClientContext context, Long oriClientID) throws IOException {
        RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
        context.writeObject(riskGameMessageFactory.createReconnectMessage(context,oriClientID));
    }
}
