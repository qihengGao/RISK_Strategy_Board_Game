package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.*;

import java.io.IOException;
import java.util.*;

public class GameHandler extends Thread {
    private final ArrayList<Color> predefineColorList = new ArrayList<>();
    private final Set<Client> players;
    private final TreeMap<Long, Color> idToColor;
    private final RISKMap riskMap;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain Thread(ThreadGroup, Runnable, String) Thread}
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
        idToColor = new TreeMap<Long, Color>();
        riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(3);
    }

    public Client findClientByID(long id){
        for (Client c : players){
            if (c.getClientID()==id){
                return c;
            }
        }
        return null;
    }


    /**
     * Check if a player is lost.
     * @param client The player we want to check with.
     * @return True if this player is lost, otherwise false.
     */
    public boolean isPlayerLost(Client client){
        for( Territory territory : riskMap.getContinent()){
            if(territory.getOwnerID() == client.getClientID())
                return false;
        }
        return true;
    }

    /**
     * Check if any player wins.
     * @return Client if this player is the only player left, otherwise null.
     */
    public Client checkWinner(){
        HashSet<Long> IDset = new HashSet<>();
        for (Territory t:riskMap.getContinent()){
            IDset.add(t.getOwnerID());
        }
        if (IDset.size()==1){
            for (long id : IDset) {
                return findClientByID(id);
            }
        }
        return null;
    }

    public void run() throws ClassCastException {
        System.out.println("Game start. Sending map to client.");

        assignColorToPlayers();
        assignTerritoriesToPlayers();
        unitPlacementPhase();
        System.out.println("Placement Phase finish");

        Client winner = null;

        int roundNumber = 0;
        while (winner == null) {
            String prompt = "Placement phase finished!";
            if (roundNumber!=0){
                prompt = "Resolved Round " + roundNumber + " Outcome! Now start placing orders!";
            }
            HashMap<String, ArrayList<Order>> ordersToList = actionPhase(prompt);
            System.out.println("action phase finished");

            //EXECUTE ORDERS: sending order related units into related territories or battlefields
            executeOrdersAndCheckLegal(ordersToList, "Move", "Attack");
            //todo: update in battlefield

            increaseOneInAllTerritory();

            //todo: remove this break;
            if (roundNumber==3) {
                break;
            }
            roundNumber++;
            //-----

            //check player win and end game
            //Client winner = checkWinner();
        }

        //send winner message to all clients
        for (Client c : players) {
            winner = c;
            break;
        }
        resolveRound("Resolved Game Outcome!", winner);

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        System.out.println(mapTextView.displayMap());
    }

    public void increaseOneInAllTerritory(){
        for (Territory t : riskMap.getContinent()){
            t.getUnitByType("Soldier").tryIncreaseAmount(1);
        }
    }

    public void resolveRound(String prompt, Client winner) {
        for (Client client : players) {
            String customized_prompt = "You Lose! Conquer more next time!";
            if (client.getClientID() == winner.getClientID()) {
                customized_prompt = "Congratulations! This continent is yours!";
            }
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new ShowRoundResultState(), riskMap, prompt + "\n" + customized_prompt, idToColor));
            } catch (IOException e) {
                System.out.println("Client socket closed, id :" + client.getClientID());
            }
        }
    }

    public void assignColorToPlayers() {
        for (Client client : players) {
            idToColor.put(client.getClientID(), predefineColorList.remove(0));
        }
    }

    /**
     * Randomly initialize the territories with client ID.
     *
     */
    public void assignTerritoriesToPlayers() {
        ArrayList<Territory> randomized = new ArrayList<>();
        for (Territory territory : riskMap.getContinent()) {
            randomized.add(territory);
        }
        Collections.shuffle(randomized, new Random(1777));
        int count = 0;
        ArrayList<Long> clientIDList = new ArrayList<>();
        for (Client client : players)
            clientIDList.add(client.getClientID());
        for (Territory territory : randomized) {
            territory.tryChangeOwnerTo(clientIDList.get(count++ / 3));

        }
    }

    //unit placement phase
    public void unitPlacementPhase()
            throws ClassCastException {
        for (Client client : players) {
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new UnitPlaceState(), riskMap,
                        "Placing order!", idToColor));
                ArrayList<Territory> receive = (ArrayList<Territory>) client.readObject();
                updateMap(riskMap, receive);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client socket closed, id :" + client.getClientID());
            }
        }
    }

    private void updateMap(RISKMap riskMap, ArrayList<Territory> receive) {
        for (Territory t : receive) {
            riskMap.tryAddTerritory(t);
        }
    }

    //action phase: move/attack
    public HashMap<String, ArrayList<Order>> actionPhase(String prompt)
            throws ClassCastException {
        HashMap<String, ArrayList<Order>> orderToList = createEmptyOrderTypeToOrders("Move", "Attack");
        for (Client client : players) {
            readAndWriteOrders(riskMap, idToColor, client, prompt, orderToList);
        }
        return orderToList;
    }

    private HashMap<String, ArrayList<Order>> createEmptyOrderTypeToOrders(String... orderTypes) {
        HashMap<String, ArrayList<Order>> ans = new HashMap<>();
        for (String type : orderTypes) {
            ans.put(type, new ArrayList<Order>());
        }
        return ans;
    }

    private void readAndWriteOrders(RISKMap riskMap, TreeMap<Long, Color> idToColor, Client client, String prompt,
                                    HashMap<String, ArrayList<Order>> orderToList) {
        try {
            client.writeObject(new RiskGameMessage(client.getClientID(), new MoveAttackState(), riskMap, prompt, idToColor));
            ArrayList<Order> orders = (ArrayList<Order>) client.readObject();
            for (Order order : orders) {
                orderToList.get(order.getOrderType()).add(order);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client socket closed, id :" + client.getClientID());
        }
    }

    //resolve round result phase: compute outcome of all attacks
    private void executeOrdersAndCheckLegal(HashMap<String, ArrayList<Order>> ordersToList, String... orderTypes) {
        for (String type : orderTypes) {
            for (Order order : ordersToList.get(type)) {
                //
                MapTextView mapTextView = new MapTextView(riskMap, idToColor);
                System.out.println(mapTextView.displayMap());
                System.out.println(order.getPlayerID() + ":" + order.toString());
                String check_message = order.executeOrder(riskMap);
                //server check
                if (check_message!=null){
                    System.out.println(mapTextView.displayMap());
                    System.out.println(order.getPlayerID() + ":" + order.toString());
                    try {
                        letClientReOrder(order, check_message);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Client socket closed, id :" + order.getPlayerID());
                    }
                }
            }
        }
    }

    private void letClientReOrder(Order order, String check_message) throws IOException, ClassNotFoundException {
        Client client = findClientByID(order.getPlayerID());
        try {
            client.writeObject(new RiskGameMessage(client.getClientID(), new ReEnterOrderState(order), riskMap,
                    "Your order (" + order.toString() + ") did not pass our rules", idToColor));
            Order reorder = (Order) client.readObject();
            if (reorder == null){
                return;
            }
            String Recheck_message = reorder.executeOrder(riskMap);
        }catch (IOException | ClassNotFoundException e){
            System.out.println("Client socket closed, id :" + order.getPlayerID());
        }catch (NullPointerException e){
            int offset = e.toString().indexOf(":") + 2;
            System.out.println(e.toString().substring(offset));
        }
    }

}
