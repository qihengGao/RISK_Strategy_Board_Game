package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.state.State;

import java.io.IOException;

public class WaitingState extends State {


    /**
     * This is the state for any waiting propose.
     * In this state, just wait to receive next message from server.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     * @throws ClassNotFoundException If the Object we receive from server is not an instance of RISKGameMessage.
     */
    @Override
    public void doAction(ClientContext context) throws IOException, ClassNotFoundException {

        //contex.getOut().println("Waiting for server to resolve your orders...\n");

        //Read a message from server
        RiskGameMessage riskGameMessage = context.readMessage();

        //Update the context with message
        updateContextWithMessage(context, riskGameMessage);

        //Go to next state.
        riskGameMessage.getCurrentState().doAction(context);
    }
}
