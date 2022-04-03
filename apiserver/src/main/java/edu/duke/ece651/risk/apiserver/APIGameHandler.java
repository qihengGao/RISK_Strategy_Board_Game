package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.models.State;
import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.Owner;
import edu.duke.ece651.risk.shared.checker.PlaceRuleChecker;
import edu.duke.ece651.risk.shared.checker.PlaceTerrExistChecker;
import edu.duke.ece651.risk.shared.checker.PlaceTerrIDChecker;
import edu.duke.ece651.risk.shared.checker.PlaceUnitAmountChecker;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class APIGameHandler {
    Logger logger;
    private ArrayList<Color> predefineColorList = new ArrayList<>();
    private Set<Long> players;
    private TreeMap<Long, Color> idToColor;

    private String currentState;

    private Set<Long> commitedPlayer;
    private Set<Long> lostPlayer;

    private RISKMap riskMap;

    private Integer InitUnitAmountPerPlayer;

    public String getCurrentState() {
        return currentState;
    }

    private ArrayList<Order> temporaryOrders;

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

    public RISKMap getRiskMapByState() {
        if (currentState.equals(State.PlacingState.name())) {
            RISKMap cloneMap = (RISKMap) SerializationUtils.clone(riskMap);

            for (Territory t : cloneMap.getContinent()) {
                t.setUnits(new TreeSet<Unit>());
            }

            return cloneMap;
        } else {
            return riskMap;
        }
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
        temporaryOrders = new ArrayList<>();
        logger = LoggerFactory.getLogger(APIGameHandler.class);
        this.currentState = State.WaitingToStartState.name();
        InitUnitAmountPerPlayer = 30;
        lostPlayer = new HashSet<>();
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
        //Check for valid place
        //1.Check for current State == PlacingState and clientID not in committedPlayer.
        if (Objects.equals(currentState, State.PlacingState.name()) && !commitedPlayer.contains(clientID)) {

            //2. Check unit validation.

            //Rule Checker
            //1.Check if territory exist.
            //2.Check if total amount valid.
            PlaceRuleChecker placeRuleChecker =
                    new PlaceTerrExistChecker(
                            new PlaceTerrIDChecker(
                                    new PlaceUnitAmountChecker(null, InitUnitAmountPerPlayer)));
            try {
                placeRuleChecker.checkPlace(riskMap, unitPlaceOrders, clientID);
            } catch (IllegalArgumentException e) {
                return false;
            }

            for (String territoryName : unitPlaceOrders.keySet()) {
                riskMap.getTerritoryByName(territoryName).tryAddUnit(new BasicUnit("Soldier", unitPlaceOrders.get(territoryName)));
            }
            commitedPlayer.add(clientID);
            if (commitedPlayer.size() == roomSize) {
                increaseTechFoodForAll();
                orderingPhase();
            }
            return true;
        } else
            return false;
    }


    public boolean tryPreProcessOrder(Long clientID, ArrayList<Order> orders) {
        if (Objects.equals(currentState, State.OrderingState.name()) && !commitedPlayer.contains(clientID)) {

            System.out.println("in tryPreProcessOrder");
            RISKMap cloneMap = (RISKMap) SerializationUtils.clone(riskMap);

            System.out.println("number of orders:" + orders.size());

            if (orders == null || tryExecuteOrder(orders, cloneMap)) {
                temporaryOrders.addAll(orders);
                commitedPlayer.add(clientID);
                if (commitedPlayer.size() == roomSize) {
                    tryExecuteOrder(temporaryOrders, riskMap);
                    for (Territory t : riskMap.getContinent()) {
                        //todo: uncomment this line for simple add or minus fight resolver
                        //t.getBattleField().setAttackResolver(new SimpleAttackResolver());
                        //
                        t.getBattleField().fightBattle(t, "Soldier");
                        t.getBattleField().resetAttackersList();
                    }
                    increaseOneInAllTerritory();
                    increaseTechFoodForAll();

                    if (checkWinner() == null) {
                        orderingPhase();
                    } else {
                        showGameResultPhase();
                    }
                }
                return true;
            }
            return false;
        } else
            return false;
    }


    public boolean tryExecuteOrder(ArrayList<Order> orders, RISKMap tmpRiskMap) {
        ArrayList<Order> moveOrUpgradeOrder = new ArrayList<>();
        ArrayList<Order> attackOrder = new ArrayList<>();
        ArrayList<Order> upgradeMaxTechOrder = new ArrayList<>();
        System.out.println("in tryExecuteOrder");
        for (Order order : orders) {
            if (Objects.equals(order.getOrderType(), "Move") || Objects.equals(order.getOrderType(), "Upgrade Unit"))
                moveOrUpgradeOrder.add(order);
            if (Objects.equals(order.getOrderType(), "Attack"))
                attackOrder.add(order);
            if (order.getOrderType().equals("Upgrade Tech Level"))
                upgradeMaxTechOrder.add(order);
        }
        System.out.println("number of move/upgrade orders:" + moveOrUpgradeOrder.size());
        System.out.println("number of attack orders:" + attackOrder.size());
        System.out.println("number of upgrade orders:" + upgradeMaxTechOrder.size());
        StringBuilder moveErrorMessage = new StringBuilder();
        for (Order order : moveOrUpgradeOrder) {
            String errorMessage = order.executeOrder(tmpRiskMap);
            if (errorMessage != null)
                moveErrorMessage.append(errorMessage);
        }

        StringBuilder attackErrorMessage = new StringBuilder();
        for (Order order : attackOrder) {
            String errorMessage = order.executeOrder(tmpRiskMap);
            if (errorMessage != null)
                attackErrorMessage.append(errorMessage);
        }
        System.out.println(moveErrorMessage.toString() + "  \n" + attackErrorMessage.toString());

        StringBuilder upgradeMaxTechMessage = new StringBuilder();
        if (!upgradeMaxTechOrder.isEmpty()) {
            if (upgradeMaxTechOrder.size() > 1) {
                upgradeMaxTechMessage.append("You cannot upgrade your tech level more than once");
            } else {
                String errorMessage = upgradeMaxTechOrder.get(0).executeOrder(tmpRiskMap);
                if (errorMessage != null) {
                    upgradeMaxTechMessage.append(errorMessage);
                }
            }
        }
        return moveErrorMessage.toString().equals("") && attackErrorMessage.toString().equals("")
                && upgradeMaxTechMessage.toString().equals("");

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
        for (Long clientID : players) {
            clientIDList.add(clientID);
            riskMap.tryAddOwner(new Owner(clientID));
        }
        for (Territory territory : randomized) {
            territory.tryChangeOwnerTo(clientIDList.get(count++ / n_Terr_per_player));
        }
        for (Long clientID : clientIDList) {
            int totalRest = 10;
            Random random = new Random();
            while (totalRest > 0) {
                for (Territory t : riskMap.getTerritoriesByOwnerID(clientID)) {
                    if (random.nextBoolean()) {
                        t.increaseSize(2);
                        totalRest -= 2;
                    }
                }
            }
        }
    }

    public String getPlayerState(Long clientID) {


        if (isPlayerLost(clientID))
            return State.LostState.name();
        else {
            if (commitedPlayer.contains(clientID))
                return State.WaitingState.name();
            else
                return currentState;
        }
    }


    public void unitPlacementPhase(int n_Terr_per_player) {
        currentState = State.PlacingState.name();
        commitedPlayer.clear();

        assignColorToPlayers();
        assignTerritoriesToPlayers(n_Terr_per_player);
    }

    public void orderingPhase() {
        for (Long id : players) {
            isPlayerLost(id);
        }
        currentState = State.OrderingState.name();
        commitedPlayer.clear();
        commitedPlayer.addAll(lostPlayer);
        temporaryOrders.clear();
    }

    public void showGameResultPhase() {
        currentState = State.EndState.name();
        commitedPlayer.clear();

    }

    public void increaseTechFoodForAll() {
        for (Long id : players) {
            Owner o = riskMap.getOwners().get(id);
            for (Territory t : riskMap.getTerritoriesByOwnerID(id)) {
                o.tryAddOrRemoveFoodResource(t.getFoodProduction());
                o.tryAddOrRemoveTechResource(t.getTechProduction());
            }
        }
    }

    public void increaseOneInAllTerritory() {
        for (Territory t : riskMap.getContinent()) {
            t.tryAddUnit(new BasicUnit("Soldier", 1));
        }
    }

    /**
     * Check if any player wins.
     *
     * @return Client if this player is the only player left, otherwise null.
     */
    public Long checkWinner() {
        HashSet<Long> IDset = new HashSet<>();
        for (Territory t : riskMap.getContinent()) {
            IDset.add(t.getOwnerID());
        }

        logger.info(String.format("RoomID:%d Current IDset.size()=%d", roomID, IDset.size()));
        if (IDset.size() == 1) {
            return IDset.iterator().next();
        }
        return null;
    }


    /**
     * Check if a player is lost.
     *
     * @param clientID The player we want to check with.
     * @return True if this player is lost, otherwise false.
     */
    public boolean isPlayerLost(Long clientID) {
        //If this player still have any territory,
        //means that this player is not lost.
        if (lostPlayer.contains(clientID))
            return true;
        if (currentState.equals(State.WaitingToStartState.name())) {
            return false;
        }

        for (Territory territory : riskMap.getContinent()) {
            if (territory.getOwnerID().equals(clientID))
                return false;
        }
        lostPlayer.add(clientID);
        return true;
    }
}
