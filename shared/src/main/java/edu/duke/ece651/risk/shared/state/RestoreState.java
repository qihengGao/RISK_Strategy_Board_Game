package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;

import java.io.IOException;
import java.util.regex.Pattern;

public class RestoreState extends State {


    /**
     * In this state, user make decision to start a new game or restore a previous game.
     * If user want to restore the game, user must provide a valid client ID of previous game.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     * @throws ClassNotFoundException If the Object we receive from server is not an instance of RISKGameMessage.
     */
    @Override
    public void doAction(ClientContext context) throws IOException, ClassNotFoundException {
        readUserOptionThenNotifyServer(context);

        //Read a message from server
        RiskGameMessage riskGameMessage = context.readMessage();

        //Update the context with message
        updateContextWithMessage(context, riskGameMessage);

        //Go to next state.
        riskGameMessage.getCurrentState().doAction(context);
    }

    /**
     * Read user's choice from input, then notify server with corresponding message.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     */
    protected void readUserOptionThenNotifyServer(ClientContext context) throws IOException {
        String command = readChoice(context, "Please type in you client ID to restore previous game, or Enter to start a new game.",
                "Invalid command!", Pattern.compile("^([1-9]\\d*|0)$|^$",Pattern.CASE_INSENSITIVE));

        if(command.equals("")){
            initGame(context);
        }else{
            Long oriClientID = Long.parseLong(command);
            restoreGame(context, oriClientID);
        }
    }


    /**
     * Notify server that user want to start a new game.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void initGame(ClientContext context) throws IOException {
        RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
        context.writeObject(riskGameMessageFactory.createInitMessage());
    }


    /**
     * Notify server that user want to restore a previous game with specified client id.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void restoreGame(ClientContext context, Long oriClientID) throws IOException {
        RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
        context.writeObject(riskGameMessageFactory.createReconnectMessage(context,oriClientID));
    }
}
