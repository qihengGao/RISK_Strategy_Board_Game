package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.models.State;
import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.checker.PlaceRuleChecker;
import edu.duke.ece651.risk.shared.checker.PlaceTerrExistChecker;
import edu.duke.ece651.risk.shared.checker.PlaceTerrIDChecker;
import edu.duke.ece651.risk.shared.checker.PlaceUnitAmountChecker;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;

import java.util.*;

public class APIGameHandler {

    private ArrayList<Color> predefineColorList = new ArrayList<>();
    private Set<Long> players;
    private TreeMap<Long, Color> idToColor;

    private String currentState;

    private Set<Long> commitedPlayer;

    private RISKMap riskMap;

    public ArrayList<Color> getPredefineColorList() {
        return predefineColorList;
    }

    public void setPredefineColorList(ArrayList<Color> predefineColorList) {
        this.predefineColorList = predefineColorList;
    }

    public Set<Long> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Long> players) {
        this.players = players;
    }

    public TreeMap<Long, Color> getIdToColor() {
        return idToColor;
    }

    public void setIdToColor(TreeMap<Long, Color> idToColor) {
        this.idToColor = idToColor;
    }

    public RISKMap getRiskMap() {
        return riskMap;
    }

    public void setRiskMap(RISKMap riskMap) {
        this.riskMap = riskMap;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public long getRoomID() {
        return roomID;
    }

    private final int roomSize;

    private final long roomID;

    public APIGameHandler(int roomSize, long roomID, Long hostID) {
        this.roomSize = roomSize;
        this.roomID = roomID;
        predefineColorList.add(new Color("Red"));
        predefineColorList.add(new Color("Green"));
        predefineColorList.add(new Color("Blue"));
        predefineColorList.add(new Color("Yellow"));
        predefineColorList.add(new Color("Purple"));
        idToColor = new TreeMap<Long, Color>();
        riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(roomSize);
        players = new HashSet<>();
        players.add(hostID);
        commitedPlayer = new HashSet<>();
    }

    public boolean tryAddPlayer(Long clientID) {
        if (players.size() == roomSize || players.contains(clientID)) {
            return false;
        } else {
            players.add(clientID);
            if (players.size() == roomSize) {

                unitPlacementPhase(3);

            }
            return true;
        }
    }


    public boolean tryPlaceUnit(Long clientID, Map<String, Integer> unitPlaceOrders) {
        //Rule Checker
        //1.Check if territory exist.
        //2.Check if total amount valid.
        PlaceRuleChecker placeRuleChecker =
                new PlaceTerrExistChecker(
                        new PlaceTerrIDChecker(
                                new PlaceUnitAmountChecker(null, 30)));
        try {
            placeRuleChecker.checkPlace(riskMap, unitPlaceOrders, clientID);
        } catch (IllegalArgumentException e) {
            return false;
        }

        for (String territoryName : unitPlaceOrders.keySet()) {
            riskMap.getTerritoryByName(territoryName).tryAddUnit(new BasicUnit("Soldier", unitPlaceOrders.get(territoryName)));
        }
        commitedPlayer.add(clientID);
        if(commitedPlayer.size() == roomSize){
            orderingPhase();
        }
        return true;
    }

    public void assignColorToPlayers() {
        for (Long clientID : players) {
            idToColor.put(clientID, predefineColorList.remove(0));
        }
    }

    /**
     * Randomly initialize the territories with client ID.
     */
    public void assignTerritoriesToPlayers(int n_Terr_per_player) {
        ArrayList<Territory> randomized = new ArrayList<>();
        for (Territory territory : riskMap.getContinent()) {
            randomized.add(territory);
        }
        Collections.shuffle(randomized, new Random(1777));
        int count = 0;
        ArrayList<Long> clientIDList = new ArrayList<>();
        for (Long clientID : players)
            clientIDList.add(clientID);
        for (Territory territory : randomized) {
            territory.tryChangeOwnerTo(clientIDList.get(count++ / n_Terr_per_player));

        }
    }

    public String getPlayerState(Long clientID) {

        if (commitedPlayer.contains(clientID))
            return State.WaitingState.name();
        else
            return currentState;
    }

    public Long getWinner() {
        return null;
    }

    public void unitPlacementPhase(int n_Terr_per_player) {
        currentState = State.PlacingState.name();
        commitedPlayer.clear();
        assignColorToPlayers();
        assignTerritoriesToPlayers(n_Terr_per_player);
    }

    public void orderingPhase(){
        currentState = State.OrderingState.name();
        commitedPlayer.clear();
    }


//    public void unitPlacementPhase()
//            throws ClassCastException {
//        for (Client client : players) {
//            try {
//                client.writeObject(new RiskGameMessage(client.getClientID(), new UnitPlaceState(), riskMap,
//                        "Placing order!", idToColor));
//            } catch (IOException e) {
//                System.out.println("socket closed");
//            }
//        }
//        for (Client client : players) {
//
//            ArrayList<Territory> receive = null;
//            try {
//                receive = (ArrayList<Territory>) client.readObject();
//                updateMap(riskMap, receive);
//            } catch (IOException | ClassNotFoundException e) {
//
//            }
//
//
//        }
//    }

}
