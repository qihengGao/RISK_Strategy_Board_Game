package edu.duke.ece651.risk.server;

import java.io.IOException;
import java.util.*;
import edu.duke.ece651.risk.shared.*;

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

    public void run() throws ClassCastException {
        System.out.println("Game start. Sending map to client.");
        AbstractMapFactory tmf = new RandomMapFactory();
        RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();

        assignColorToPlayers(idToColor);
        assignTerritoriesToPlayers(riskMap);
        unitPlacementPhase(riskMap, idToColor);
        System.out.println("Placement Phase finish");
        movePhase(riskMap, idToColor);
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
        Collections.shuffle(randomized, new Random(1777));
        int count = 0;
        for (Territory territory : randomized) {
            territory.tryChangeOwnerTo(count / 3);
            count++;
        }
    }

    public void unitPlacementPhase(RISKMap riskMap, TreeMap<Long, Color> idToColor)
            throws ClassCastException {
        for (Client client : players) {
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new UnitPlaceState(), riskMap,
                        "Placing order!", idToColor));
                ArrayList<Territory> receive = (ArrayList<Territory>) client.readObject();
                updateMap(riskMap, receive);
            } catch (IOException|ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMap(RISKMap riskMap, ArrayList<Territory> receive) {
      for (Territory t : receive) {
          riskMap.tryAddTerritory(t);
      }
    }

    public void movePhase(RISKMap riskMap, TreeMap<Long, Color> idToColor)
            throws ClassCastException {
        ArrayList<Order> moveOrders = new ArrayList<>();
        for (Client client : players) {
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new MoveAttackState("Move"), riskMap,
                        "Placement Phase finished, now start playing!", idToColor));
<<<<<<< HEAD
                ArrayList<Order> orders = (ArrayList<Order>) client.readObject();
                for (Order o : orders){
                  System.out.println(o.toString());
                }
            } catch (IOException|ClassNotFoundException e) {
=======
                        
            } catch (IOException e) {
>>>>>>> 196c9620eb0f8372f7f1ddd72277f7d1402fa6a2
                e.printStackTrace();
            } 
        }
    
        
    }
}
