package edu.duke.ece651.risk.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class SelectRoomState extends State {


    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {

        String command = readChoice(contex, "What would you like to do?\n (C)reate a new game room.\n (J)oin a existing game room.\n",
                "Invalid command!", Pattern.compile("^create|^join|^c|^j",Pattern.CASE_INSENSITIVE));
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
     * Print the prompt then read a line from input. If the line not match the pattern, print the invalidPrompt
     * then retry with the next line. Otherwise, return the string.
     * @param context ClientContext which contains the input and output stream.
     * @param prompt A friendly advise for user.
     * @param invalidPrompt A friendly advise for user when the input is invalid.
     * @param pattern The regex pattern to verify the string.
     * @return A String which match the pattern.
     * @throws IOException Any problem related to the input/output stream.
     */
    public String readChoice(ClientContext context, String prompt, String invalidPrompt, Pattern pattern) throws IOException {
        BufferedReader input = context.getBufferedReader();
        PrintStream output = context.getOut();
        output.println(prompt);

        String userInput = "";

        boolean first = true;
        do {
            if (!first) output.println(invalidPrompt);
            userInput = input.readLine().toUpperCase();
            first = false;
        } while (!pattern.matcher(userInput).find());

        return userInput;
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
