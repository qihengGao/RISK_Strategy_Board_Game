package edu.duke.ece651.risk.server;

import java.io.IOException;
import java.util.*;
import edu.duke.ece651.risk.shared.*;

public class GameHandler extends Thread {
  private final ArrayList<Color> predefineColorList = new ArrayList<>();
  private final Set<Client> players;

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
    HashMap<String, ArrayList<Order>> ordersToList = actionPhase(riskMap, idToColor, "Placement Phase finished, now start placing orders!");//first move

    resolveRound(riskMap, idToColor, "Resolved Round Outcome!", ordersToList, "Move", "Attack");//compute the result of this round

    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    System.out.println(mapTextView.displayMap());
  }

  public void assignColorToPlayers(TreeMap<Long, Color> idToColor) {
    for (Client client : players) {
      idToColor.put(client.getClientID(), predefineColorList.remove(0));
    }
  }

  /**
   * Randomly initialize the territories with client ID.
   *
   * @param riskMap The RISKMap where contains territories.
   */
  public void assignTerritoriesToPlayers(GameMap riskMap) {
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
  public void unitPlacementPhase(RISKMap riskMap, TreeMap<Long, Color> idToColor)
    throws ClassCastException {
    for (Client client : players) {
      try {
        client.writeObject(new RiskGameMessage(client.getClientID(), new UnitPlaceState(), riskMap,
                                               "Placing order!", idToColor));
        ArrayList<Territory> receive = (ArrayList<Territory>) client.readObject();
        updateMap(riskMap, receive);
      } catch (IOException|ClassNotFoundException e) {
        System.out.println("Client socket closed, id :"+client.getClientID());
      }
    }
  }

  private void updateMap(RISKMap riskMap, ArrayList<Territory> receive) {
    for (Territory t : receive) {
      riskMap.tryAddTerritory(t);
    }
  }

  //action phase: move/attack
  public HashMap<String, ArrayList<Order>> actionPhase(RISKMap riskMap, TreeMap<Long, Color> idToColor, String prompt)
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
      for (Order order : orders){
        System.out.println(order.toString());
        orderToList.get(order.getOrderType()).add(order);
      }
    } 
    catch (IOException|ClassNotFoundException e) {
      System.out.println("Client socket closed, id :"+client.getClientID());
    }
    catch (IllegalArgumentException e){
      int offset = e.toString().indexOf(":")+2;
      readAndWriteOrders(riskMap, idToColor, client, e.toString().substring(offset), orderToList);
    }
  }

  //resolve round result phase: compute outcome of all attacks
  public void resolveRound(RISKMap riskMap, TreeMap<Long, Color> idToColor, String prompt, HashMap<String,
          ArrayList<Order>> ordersToList, String... orderTypes){
    for (String type:orderTypes){
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

    for (Client client : players){
      try {
        client.writeObject(new RiskGameMessage(client.getClientID(), new ShowRoundResultState(), riskMap, prompt, idToColor));
      } catch (IOException e) {
        System.out.println("Client socket closed, id :"+client.getClientID());
      }
    }
  }
}
