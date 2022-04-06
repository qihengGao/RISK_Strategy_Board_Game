package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.models.State;
import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.checker.PlaceRuleChecker;
import edu.duke.ece651.risk.shared.checker.PlaceTerrExistChecker;
import edu.duke.ece651.risk.shared.checker.PlaceTerrIDChecker;
import edu.duke.ece651.risk.shared.checker.PlaceUnitAmountChecker;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class APIGameHandler {
    //logger to display info in server console
    Logger logger;

    //RISK game related fields
    private ArrayList<Color> predefineColorList = new ArrayList<>();
    private Set<Long> players; //all joined players
    private TreeMap<Long, Color> idToColor; //player id to color

    private String currentState; //game's current state
    private Set<Long> commitedPlayer; //all committed players

    public void setLostPlayer(Set<Long> lostPlayer) {
        this.lostPlayer = lostPlayer;
    }

    private Set<Long> lostPlayer; //all losted players
    private RISKMap riskMap; //the map to play with
    private Integer InitUnitAmountPerPlayer; //Initial Total Unit amount available for each player
    private ArrayList<Order> temporaryOrders; // temporary order holder

    //room related fields
    private final int roomSize;
    private final long roomID;

    //getters/setters
    public String getCurrentState() {
        return currentState;
    }
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

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


    //constructor

    /**
     * create a new room
     * @param roomSize
     * @param roomID
     * @param hostID
     */
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

    /**
     * return the riskMap according to player state
     * @return RISKMap
     */
    public RISKMap getRiskMapByState() {
        if (currentState.equals(State.PlacingState.name())) {
            RISKMap cloneMap = (RISKMap) SerializationUtils.clone(riskMap);

            for (Territory t : cloneMap.getContinent()) {
                t.setUnits(new TreeSet<>());
            }

            return cloneMap;
        } else {
            return riskMap;
        }
    }

    /**
     * try to add a player into this game
     * @param clientID
     * @return
     */
    public boolean tryAddPlayer(Long clientID) {
        //check if all players joined, check if this player has joined already
        if (players.size() == roomSize || players.contains(clientID)) {
            return false;
        } else {
            players.add(clientID);
            //if have all players, start the game
            if (players.size() == roomSize) {
                unitPlacementPhase(3);
            }
            return true;
        }
    }


    /**
     * try to place unit into the map
     * @param clientID
     * @param unitPlaceOrders
     * @return null if all rules passed; error message if some rule didn't pass
     */
    public String tryPlaceUnit(Long clientID, Map<String, Integer> unitPlaceOrders) {
        //Check for valid place
        //1.Check for current State == PlacingState and clientID not in committedPlayer.
        if (!Objects.equals(currentState, State.PlacingState.name()) || commitedPlayer.contains(clientID)) {
            return "Failed to place the orders! Place action invalid right now!";
        }

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
            int offset = e.toString().indexOf(":") + 2;
            return e.toString().substring(offset);
        }

        for (String territoryName : unitPlaceOrders.keySet()) {
            riskMap.getTerritoryByName(territoryName).tryAddUnit(new BasicUnit("Soldier", unitPlaceOrders.get(territoryName)));
        }
        commitedPlayer.add(clientID);
        if (commitedPlayer.size() == roomSize) {
            increaseTechFoodForAll();
            orderingPhase();
        }
        return null;
    }


    /**
     * try to process all the orders
     * @param clientID
     * @param orders
     * @return String: null if all orders are executed successfully;
     * error message if some order is illegal
     */
    public String tryPreProcessOrder(Long clientID, ArrayList<Order> orders) {
        //check if in ordering state
        //check if player committed already
        if (!Objects.equals(currentState, State.OrderingState.name()) || commitedPlayer.contains(clientID)) {
            return "Failed to place the orders! Place action invalid right now!";
        }

        System.out.println("PreProcessing Orders...");

        //try execute orders on clone map
        RISKMap cloneMap = (RISKMap) SerializationUtils.clone(riskMap);
        String tryExecuteMessage = tryExecuteOrder(orders, cloneMap);

        //check if the execute messages is null
        if (orders == null || tryExecuteMessage == null) {
            temporaryOrders.addAll(orders);
            commitedPlayer.add(clientID);
            //if all players committed
            if (commitedPlayer.size() == roomSize) {
                tryExecuteOrder(temporaryOrders, riskMap);
                fightBattlesInAllTerritory();
                increaseOneInAllTerritory();
                increaseTechFoodForAll();

                //check if the game has a winner
                if (checkWinner() == null) {
                    //if not, go to next round
                    orderingPhase();
                } else {
                    //show the result of this game
                    showGameResultPhase();
                }
            }
        }
        return tryExecuteMessage;
    }

    /**
     * resolve all fights in the Battlefield of all territories
     */
    private void fightBattlesInAllTerritory() {
        for (Territory t : riskMap.getContinent()) {
            //todo: uncomment this line for simple add or minus fight resolver
            //t.getBattleField().setAttackResolver(new SimpleAttackResolver());
            //
            t.getBattleField().fightAllBattle(t);
            t.getBattleField().resetAttackersList();
        }
    }


    /**
     * try to execute all the orders in the cloned map
     * @param orders
     * @param tmpRiskMap
     * @return String: null if all rules passes; error message if some order is illegal
     */
    public String tryExecuteOrder(ArrayList<Order> orders, RISKMap tmpRiskMap) {
        System.out.println("try Executing Orders...");

        ArrayList<Order> moveOrUpgradeOrder = new ArrayList<>();
        ArrayList<Order> attackOrder = new ArrayList<>();
        ArrayList<Order> upgradeMaxTechOrder = new ArrayList<>();

        for (Order order : orders) {
            if (Objects.equals(order.getOrderType(), "Move") || Objects.equals(order.getOrderType(), "Upgrade Unit"))
                moveOrUpgradeOrder.add(order);
            if (Objects.equals(order.getOrderType(), "Attack"))
                attackOrder.add(order);
            if (order.getOrderType().equals("Upgrade Tech Level"))
                upgradeMaxTechOrder.add(order);
        }

        for (Order order : moveOrUpgradeOrder) {
            String errorMessage = order.executeOrder(tmpRiskMap);
            if (errorMessage != null) {
                String showError = "Your order: " + order + " is illegal!\n"
                        + errorMessage;
                return showError;
            }
        }

        for (Order order : attackOrder) {
            String errorMessage = order.executeOrder(tmpRiskMap);
            if (errorMessage != null) {
                String showError = "Your order: " + order + " is illegal!\n"
                        + errorMessage;
                return showError;
            }
        }

        if (!upgradeMaxTechOrder.isEmpty()) {
            if (upgradeMaxTechOrder.size() > 1) {
                return "You cannot upgrade your tech level more than once";
            } else {
                Order order = upgradeMaxTechOrder.get(0);
                String errorMessage = order.executeOrder(tmpRiskMap);
                if (errorMessage != null) {
                    String showError = "Your order: " + order + " is illegal!\n"
                            + errorMessage;
                    return showError;
                }
            }
        }
        return null;
    }

    /**
     * assign a color to each player
     */
    public void assignColorToPlayers() {
        for (Long clientID : players) {
            idToColor.put(clientID, predefineColorList.remove(0));
        }
    }

    /**
     * Randomly initialize the territories with client ID.
     * @param n_Terr_per_player
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

    /**
     * get the state of some player
     * @param clientID
     * @return
     */
    public String getPlayerState(Long clientID) {
        //check if this player lost
        if (isPlayerLost(clientID))
            return State.LostState.name();
        else {
            //check if this player has committed
            if (commitedPlayer.contains(clientID))
                return State.WaitingState.name();
            else
                return currentState;
        }
    }

    /**
     * going into the unitplacement phase after all players joined the room
     * @param n_Terr_per_player
     */
    public void unitPlacementPhase(int n_Terr_per_player) {
        currentState = State.PlacingState.name();
        commitedPlayer.clear();

        assignColorToPlayers();
        assignTerritoriesToPlayers(n_Terr_per_player);
    }

    /**
     * going into placing order for each player
     */
    public void orderingPhase() {
        for (Long id : players) {
            isPlayerLost(id);
        }
        currentState = State.OrderingState.name();
        commitedPlayer.clear();
        commitedPlayer.addAll(lostPlayer);
        temporaryOrders.clear();
    }

    /**
     * show the game result if this game has a winner
     */
    public void showGameResultPhase() {
        currentState = State.EndState.name();
        commitedPlayer.clear();
    }

    /**
     * increase the tech/food resource for all players
     */
    public void increaseTechFoodForAll() {
        for (Long id : players) {
            Owner o = riskMap.getOwners().get(id);
            for (Territory t : riskMap.getTerritoriesByOwnerID(id)) {
                o.tryAddOrRemoveFoodResource(t.getFoodProduction());
                o.tryAddOrRemoveTechResource(t.getTechProduction());
            }
        }
    }

    /**
     * increase one basic unit in all territories
     */
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
        if (lostPlayer.contains(clientID)) {
            return true;
        }
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
