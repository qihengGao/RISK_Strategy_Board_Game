package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;

import java.io.IOException;
import java.util.regex.Pattern;


public class InitiateSocketState extends State {


    /**
     * Set the server address and port number to default server in context.
     * Currently, we have a default server at vcm-25035.vm.duke.edu:1777.
     *
     * @param context ClientContext which store all the client information.
     */
    public void useDefaultServerAddress(ClientContext context) {
        context.setServerAddress("vcm-25035.vm.duke.edu");
        context.setPortNumber(1777);
    }


    /**
     * Read server address and port from input.
     * Then set them to the context.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void useCustomServerAddress(ClientContext context) throws IOException {
        String serverAddress = readChoice(context, "Please type in server address:",
                "Invalid address.", Pattern.compile("^.+$", Pattern.CASE_INSENSITIVE));
        int serverPort = Math.abs(Integer.parseInt(readChoice(context, "Please type in server port:",
                "Invalid port number.", Pattern.compile("^\\d+$", Pattern.CASE_INSENSITIVE))));

        context.setServerAddress(serverAddress);

        //Here is a workaround to support random port number for integration test.
        //In integration test, we already set the port number to context.
        //So if the port number in context is not 0, use that one.
        if (context.getPortNumber() == 0)
            context.setPortNumber(serverPort);

    }


    /**
     * This method ask user to switch between default server and customer server, then using the information to connect
     * to server.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException Any problem related to the input/output stream.
     */
    public void initiateSocket(ClientContext context) throws IOException {
        String command = readChoice(context, "Which server would you like to connect?\n (D)efault server\n (C)ustom server",
                "Invalid Command", Pattern.compile("^D$|^Default$|^C$|^Custom$", Pattern.CASE_INSENSITIVE));

        switch (command.substring(0, 1).toUpperCase()) {
            case "D":
                useDefaultServerAddress(context);
                break;
            case "C":
                useCustomServerAddress(context);
                break;

        }

        //Using the information we collect above to connect to server.
        connectToServer(context);
    }


    /**
     * This is the first state of our client.
     * In this State, we allow user to decide which server to connect with.
     * Currently, we have a default server at vcm-25035.vm.duke.edu:1777.
     * User can also connect to a custom server.
     *
     * @param context ClientContext which store all the client information.
     * @throws IOException            Any problem related to the input/output stream.
     * @throws ClassNotFoundException If the Object we receive from server is not an instance of RISKGameMessage.
     */
    @Override
    public void doAction(ClientContext context) throws IOException, ClassNotFoundException {
        boolean done = false;

        //Loop until successfully connect to server.
        while (!done) {

            try {
                initiateSocket(context);
                done = true;
            } catch (IOException e) {

                //Here we handle the exception for socket connection failed.
                context.println("Connection Failed!");
            }
        }

        //Go to next state
        RestoreState restoreState = context.getStateFactory().createRestoreState();
        restoreState.doAction(context);

    }
}
