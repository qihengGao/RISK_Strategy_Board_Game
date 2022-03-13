package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.*;

import java.io.IOException;
import java.util.*;

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
        playingPhase(riskMap, idToColor);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Client client : players) {
            try {
                ArrayList<Territory> receive = (ArrayList<Territory>) client.readObject();
                for (Territory t : receive) {
                    riskMap.tryAddTerritory(t);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void playingPhase(RISKMap riskMap, TreeMap<Long, Color> idToColor)
            throws ClassCastException {
        for (Client client : players) {
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new MoveAttackState(), riskMap,
                        "Placement Phase finished, now start playing!", idToColor));
                        
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Queue<Client> tmpClient = new LinkedList<>();
        // for(Client client : players){
        //     tmpClient.add(client);
        // }
        // while(!tmpClient.isEmpty()){
        //     ArrayList<Order> orders = (ArrayList<Order>)tmpClient.peek().readObject();
        //     if(isValidOrder(orders)){

        //         //TODO add order to somewhere
        //         tmpClient.poll();
        //     }else{

        //     }
        // }
        
    }
}