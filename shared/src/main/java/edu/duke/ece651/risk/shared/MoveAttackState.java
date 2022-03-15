package edu.duke.ece651.risk.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MoveAttackState extends State {
  private final String stateName;

  // contructors for Move/Attack order
  public MoveAttackState(String stateName) {
    this.stateName = stateName;
  }

  @Override
  /**
   * Do placement action
   */
  public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
    ArrayList<Order> orders = orderPhase(contex.getRiskMap(), contex.getBufferedReader(), contex.getOut(), contex.getPlayerID(), contex.getIdToColor());
    contex.getOos().writeObject(orders);

    //wait server for next state
    contex.setGameState(new WaitingState());
    contex.getGameState().doAction(contex);
  }

  public ArrayList<Order> orderPhase(RISKMap riskMap, BufferedReader input, PrintStream output, long ID, TreeMap<Long,Color> idToColor ) throws IOException {
    displayMap(riskMap, output, ID, idToColor);
    output.println("You can place "+ this.stateName + " orders in this phase.");
    output.println("Format: SourceTerrirotyName,DestTerritoryName,UnitType,UnitAmount");
    output.println("Type D to commit all your orders");
    output.println("Please place your Orders, Commander:");
    return fillInOrders(riskMap, input, output, ID, this.stateName);
  }

  public ArrayList<Order> fillInOrders(RISKMap riskMap, BufferedReader input, PrintStream output, long ID, String orderName) throws IOException{
    ArrayList<Order> orders = new ArrayList<>();
    while(true){
      try{
        String userInput = input.readLine(); 
        if (userInput.equals("D")) {
          //
          return orders;
        }
        String[] inputs = checkFormatAndSplit(userInput);
        int amountUnderOrder = readOrderUnitAmount(inputs);
        Order tryMove = new MoveOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
        if (orderName.equals("Attack")){
          tryMove = new AttackOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
        }
        
        String check_message = tryMove.executeOrder(riskMap);
        if (check_message!=null){throw new IllegalArgumentException(check_message);}
        orders.add(tryMove);
      }
      catch (IllegalArgumentException e){
        int offset = e.toString().indexOf(":")+2;
        output.println(e.toString().substring(offset));
      }
    }
  }

  private static int readOrderUnitAmount (String[] inputs){
    int ans;
    try{
      ans = Integer.parseInt(inputs[3]);
    }
    catch (NumberFormatException e){
      throw new IllegalArgumentException("Unit Amount must be an integer!");
    }
    return ans;
  }

  private static String[] checkFormatAndSplit(String userInput) throws IllegalArgumentException{
    String[] ans = userInput.split(",");
    if (ans.length != 4) {
      throw new IllegalArgumentException("Your input " + userInput + " is not following the format!\nFormat: SourceTerrirotyName,DestTerritoryName,UnitType,UnitAmount");
    }
    return ans;
  }
  
  private void displayMap(RISKMap riskMap, PrintStream output, long ID, TreeMap<Long, Color> idToColor) {
    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    output.println(mapTextView.displayMap());
    output.println("You are: " + idToColor.get(ID).getColorName());
  }
}
