package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.*;

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
        idToColor = new TreeMap<>();
        roomSize = players.size();
        riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(roomSize);
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
        HashMap<String, ArrayList<Order>> ordersToList = actionPhase("Placement Phase finished, now start placing orders!");//first move

        resolveRound("Resolved Round Outcome!", ordersToList, "Move", "Attack");//compute the result of this round

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        System.out.println(mapTextView.displayMap());
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
        HashMap<String, ArrayList<Order>> orderToList = extracted("Move", "Attack");
        for (Client client : players) {
            readAndWriteOrders(riskMap, idToColor, client, prompt, orderToList);
        }
        return orderToList;
    }

    private HashMap<String, ArrayList<Order>> extracted(String... orderTypes) {
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
                System.out.println(order.toString());
                orderToList.get(order.getOrderType()).add(order);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client socket closed, id :" + client.getClientID());
        }
    }

    //resolve round result phase: compute outcome of all attacks
    public void resolveRound(String prompt, HashMap<String,
            ArrayList<Order>> ordersToList, String... orderTypes) {
        for (String type : orderTypes) {
            for (Order order : ordersToList.get(type)) {
                String check_message = order.executeOrder(riskMap);
                //server check
                //if (check_message!=null){
                //client.writeObject(new RiskGameMessage(client.getClientID(), new ReEnterOrderState(), riskMap,
                // "Your order "+order.toString() + " did not pass our rules, please reenter your order", idToColor));
                //Order reorder = (Order) client.readObject();
                //}
            }
        }

        for (Client client : players) {
            try {
                client.writeObject(new RiskGameMessage(client.getClientID(), new ShowRoundResultState(), riskMap, prompt, idToColor));
            } catch (IOException e) {
                System.out.println("Client socket closed, id :" + client.getClientID());
            }
        }
    }


}
