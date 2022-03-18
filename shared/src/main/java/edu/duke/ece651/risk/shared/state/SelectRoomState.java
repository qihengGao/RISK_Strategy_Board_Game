package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

public class SelectRoomState extends State {


    /**
     * In this state, user make decision to create a new game room or join an existed game room.
     * If user want to create a new room, user must provide the room size(Currently limit to 2-5).
     * If user want to join an existed game room, user must specify the room ID.
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
        String command = readChoice(context, "What would you like to do?\n (C)reate a new game room.\n (J)oin a existing game room.\n",
                "Invalid command!", Pattern.compile("^C$|^J$",Pattern.CASE_INSENSITIVE));
        command = command.toUpperCase(Locale.ROOT);
        switch (command) {
            case "C":
                createAGameRoom(context);

                break;
            case "J":
                joinAGameRoom(context);
                break;
        }
    }

    /**
     * Send message to server indicate that player want to create a new game room with specified room size.
     * @param context ClientContext which contains the input and output stream.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void createAGameRoom(ClientContext context) throws IOException {
        int roomSize = Integer.parseInt(readChoice(context, "How many players in this room?[2-5]",
                "Invalid command!", Pattern.compile("[2-5]",Pattern.CASE_INSENSITIVE)));
        RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
        context.writeObject(riskGameMessageFactory.createCreateGameRoomMessage(roomSize));
    }


    /**
     * Send message to server indicate that player want to join an exist game room with specified room ID.
     * @param context ClientContext which contains the input and output stream.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void joinAGameRoom(ClientContext context) throws IOException {
        int roomID = Integer.parseInt(readChoice(context, "Which game room you want to join?",
                "Invalid command!", Pattern.compile("^([1-9]\\d*|0)$",Pattern.CASE_INSENSITIVE)));

        RiskGameMessageFactory riskGameMessageFactory = context.getRiskGameMessageFactory();
        context.writeObject(riskGameMessageFactory.createJoinGameRoomMessage(roomID));
    }
}
