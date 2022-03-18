package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.*;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.state.*;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.io.IOException;
import java.util.*;

public class GameHandler extends Thread {
    private final ArrayList<Color> predefineColorList = new ArrayList<>();
    private final Set<Client> players;
    private final TreeMap<Long, Color> idToColor;
    private final RISKMap riskMap;
    private final int roomSize;
    private long roomID;

    public int getRoomSize() {
        return roomSize;
    }

    public long getRoomID() {
        return roomID;
    }

    public int getCurrentPlayersSize() {
        return players.size();
    }

    public void addPlayer(Client client) {
        players.add(client);
    }


    public GameHandler(Client host, int roomSize, long roomID) {
        players = new TreeSet<>(new Comparator<Client>() {

            /**
             * Compares its two arguments for order.  Returns a negative integer,
             * zero, or a positive integer as the first argument is less than, equal
             * to, or greater than the second.<p>
             * <p>
             * The implementor must ensure that {@link Integer#signum
             * signum}{@code (compare(x, y)) == -signum(compare(y, x))} for
             * all {@code x} and {@code y}.  (This implies that {@code
             * compare(x, y)} must throw an exception if and only if {@code
             * compare(y, x)} throws an exception.)<p>
             * <p>
             * The implementor must also ensure that the relation is transitive:
             * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
             * {@code compare(x, z)>0}.<p>
             * <p>
             * Finally, the implementor must ensure that {@code compare(x,
             * y)==0} implies that {@code signum(compare(x,
             * z))==signum(compare(y, z))} for all {@code z}.
             *
             * @param o1 the first object to be compared.
             * @param o2 the second object to be compared.
             * @return a negative integer, zero, or a positive integer as the
             * first argument is less than, equal to, or greater than the
             * second.
             * @throws NullPointerException if an argument is null and this
             *                              comparator does not permit null arguments
             * @throws ClassCastException   if the arguments' types prevent them from
             *                              being compared by this comparator.
             * @apiNote It is generally the case, but <i>not</i> strictly required that
             * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
             * any comparator that violates this condition should clearly indicate
             * this fact.  The recommended language is "Note: this comparator
             * imposes orderings that are inconsistent with equals."
             */
            @Override
            public int compare(Client o1, Client o2) {
                return (int) (o1.getClientID() - o2.getClientID());
            }


        });
        players.add(host);
        predefineColorList.add(new Color("Red"));
        predefineColorList.add(new Color("Green"));
        predefineColorList.add(new Color("Blue"));
        predefineColorList.add(new Color("Yellow"));
        predefineColorList.add(new Color("Purple"));
        idToColor = new TreeMap<Long, Color>();
        this.roomSize = roomSize;
        riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(roomSize);
        this.roomID = roomID;


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
        System.out.println("IDset"+IDset.size());
        if (IDset.size()==1){
            for (long id : IDset) {
                return findClientByID(id);
            }
        }
        return null;
    }

    public void run() throws ClassCastException {

//            public int compare(Client o1, Client o2) {
//                return (int) (o1.getClientID() - o2.getClientID());
//            }


        System.out.println("Game start. Sending map to client.");

        assignColorToPlayers();
        assignTerritoriesToPlayers();
        try {
            unitPlacementPhase();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            //todo: update in battlefield instead of execute order
            for (Territory t : riskMap.getContinent()){
                //default = rolling 20 sided dice. simple add and minus rules
                t.getBattleField().setAttackResolver(new SimpleAttackResolver());
                //
                t.getBattleField().fightBattle(t, "Soldier");
                t.getBattleField().resetAttackersList();
            }

            increaseOneInAllTerritory();

            //todo: remove this round limit break;

            //-----
            roundNumber++;

            //check player win and end game
            winner = checkWinner();
        }

        //send winner message to all clients
        if (winner==null) {
            for (Client c : players) {
                winner = c;
                break;
            }
        }
        showGameResult("Resolved Game Outcome!", winner);

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        System.out.println(mapTextView.displayMap());
    }

    public void increaseOneInAllTerritory(){
        for (Territory t : riskMap.getContinent()){
            t.getUnitByType("Soldier").tryIncreaseAmount(1);
        }
    }

    public void showGameResult(String prompt, Client winner) {
        for (Client client : players) {
            String customized_prompt = "You Lose! Conquer more next time!";
            if (client.getClientID() == winner.getClientID()) {
                customized_prompt = "Congratulations! This continent is yours!";
            }
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new ShowGameResultState(), riskMap, prompt + "\n" + customized_prompt, idToColor));
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
            throws ClassCastException, IOException {
        for (Client client : players){
            client.writeObject(new RiskGameMessage(client.getClientID(), new UnitPlaceState(), riskMap,
                    "Placing order!", idToColor));
        }
        for (Client client : players) {
            try {

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
            if (isPlayerLost(client)) {
                sendUpdateToLOSERS(prompt, client);
            }
            else{
                readAndWriteOrders(riskMap, idToColor, client, prompt, orderToList);
            }
        }
        return orderToList;
    }

    private void sendUpdateToLOSERS(String prompt, Client client) {
        try {
            client.writeObject(new RiskGameMessage(client.getClientID(), new ShowRoundResultToViewersState(), riskMap, prompt, idToColor));
        }
        catch (IOException e) {
            System.out.println("Client socket closed, id :" + client.getClientID());
        }
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
                String check_message = order.executeOrder(riskMap);
                //server check
                if (check_message!=null){
                    try {
                        System.out.println(order.toString());
                        System.out.println(check_message);
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
