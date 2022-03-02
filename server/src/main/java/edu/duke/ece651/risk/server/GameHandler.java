package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.PlayingState;
import edu.duke.ece651.risk.shared.RISKMap;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.Territory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class GameHandler extends Thread {

    private final Set<Client> players;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #Thread(ThreadGroup, Runnable, String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public GameHandler(Set<Client> players) {
        this.players = players;
    }

    public void run() {
        System.out.println("Game start. Sending map to client.");

        TreeMap<String, Territory> continent = new TreeMap<String, Territory>();
        continent.put("Test1", new Territory("Test1"));
        continent.put("Test2", new Territory("Test2"));
        continent.put("Test3", new Territory("Test3"));
        continent.put("Test4", new Territory("Test4"));
        RISKMap riskMap = new RISKMap(continent);

        for (Client client: players) {

            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new PlayingState(), riskMap, "Placing order!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
