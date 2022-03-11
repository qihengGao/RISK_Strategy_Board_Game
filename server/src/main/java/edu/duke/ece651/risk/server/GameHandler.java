package edu.duke.ece651.risk.server;

import com.sun.source.tree.Tree;
import edu.duke.ece651.risk.shared.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class GameHandler extends Thread {
    private final ArrayList<Color> predefineColorList = new ArrayList<>();
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
        predefineColorList.add(new Color("Red"));
        predefineColorList.add(new Color("Green"));
        predefineColorList.add(new Color("Blue"));
        predefineColorList.add(new Color("Yellow"));
        predefineColorList.add(new Color("Purple"));
    }

    public void run() {
        System.out.println("Game start. Sending map to client.");
        AbstractMapFactory tmf = new RandomMapFactory();
        RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();

        assignColorToPlayers(idToColor);
        assignTerritoriesToPlayers(riskMap);
        unitPlacementPhase(riskMap, idToColor);

    }

    public void assignColorToPlayers(TreeMap<Long, Color> idToColor) {
        for (Client client : players) {
            idToColor.put(client.getClientID(), predefineColorList.remove(0));
        }
    }

    public void assignTerritoriesToPlayers(GameMap riskMap) {
        ArrayList<Territory> randomized = new ArrayList<>();
        for (Territory territory : riskMap.getContinent()) {
            randomized.add(territory);
        }
        Collections.shuffle(randomized);
        int count = 0;
        for (Territory territory : randomized) {
            territory.tryChangeOwnerTo(count / 3);
            count++;
        }
    }

    public void unitPlacementPhase(RISKMap riskMap, TreeMap<Long, Color> idToColor) {
        for (Client client : players) {
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new UnitPlaceState(), riskMap,
                        "Placing order!", idToColor));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
