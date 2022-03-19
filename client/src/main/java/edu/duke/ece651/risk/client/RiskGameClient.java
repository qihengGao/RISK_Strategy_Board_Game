package edu.duke.ece651.risk.client;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.state.InitiateSocketState;

import java.io.IOException;

public class RiskGameClient extends Thread{
    private ClientContext clientContext;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup, Runnable, String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public RiskGameClient(ClientContext clientContext,int portNumber) {
        this.clientContext = clientContext;
        this.clientContext.setPortNumber(portNumber);
    }


    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {

                clientContext.println("Shutting down ...");
                try {
                    clientContext.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //some cleaning up code...

            }
        });
        try {
            new InitiateSocketState().doAction(clientContext);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
