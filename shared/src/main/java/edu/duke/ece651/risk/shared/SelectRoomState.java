package edu.duke.ece651.risk.shared;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

public class SelectRoomState extends State {


    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {

        String command = readChoice(contex, "What would you like to do?\n (C)reate a new game room.\n (J)oin a existing game room.\n",
                "Invalid command!", Pattern.compile("^C$|^J$",Pattern.CASE_INSENSITIVE));
        command = command.toUpperCase(Locale.ROOT);
        switch (command) {
            case "C":
                createAGameRoom(contex);

                break;
            case "J":
                joinAGameRoom(contex);
                break;
            default:
                break;
        }
        RiskGameMessage riskGameMessage = (RiskGameMessage) contex.getOis().readObject();
        contex.getOut().println(riskGameMessage.getPrompt());
        contex.setGameState(riskGameMessage.getCurrentState());
        riskGameMessage.getCurrentState().doAction(contex);

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
