package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MoveAttackState extends State {
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
    displayMapAndOptions(riskMap, output, ID, idToColor);

//    output.println("You can place "+ this.stateName + " orders in this phase.");
//    output.println("Format: SourceTerritoryName,DestTerritoryName,UnitType,UnitAmount");
//    output.println("Type D to commit all your orders");
//    output.println("Please place your Orders, Commander:");
    return fillInOrders(input, output, ID);
  }

  public ArrayList<Order> fillInOrders(BufferedReader input, PrintStream output, long ID) throws IOException{
    ArrayList<Order> orders = new ArrayList<>();
    while(true){
      String chosenOrder = chooseOrder(input, output);
      if (chosenOrder.equals("D")) {
        return orders;
      }
      String commit_message = readOrderFromUser(input, output, ID, orders, chosenOrder);
      if (commit_message!=null) {
        return orders;
      }
    }
  }

  private String readOrderFromUser(BufferedReader input, PrintStream output, long ID, ArrayList<Order> orders, String chosenOrder) throws IOException {
    String userInput = input.readLine();
    if (chosenOrder.equals("D") || userInput.equals("D")){
      return "D";
    }
    try{
      String[] inputs = checkFormatAndSplit(userInput);
      int amountUnderOrder = readOrderUnitAmount(inputs);

      Order tryMove = new MoveOrder(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
      if (chosenOrder.equals("A")){
        tryMove = new AttackOrderSimple(ID, inputs[0], inputs[1], inputs[2], amountUnderOrder);
      }
      orders.add(tryMove);
      return null;
    }
    catch (IllegalArgumentException e){
      int offset = e.toString().indexOf(":")+2;
      output.println(e.toString().substring(offset));
      return readOrderFromUser(input, output, ID, orders, chosenOrder);
    }
  }

  private String chooseOrder(BufferedReader input, PrintStream output) throws IOException {
    output.println("Please enter your action choice: [(M)ove, (A)ttack, (D)one]");
    try {
      String userInput = input.readLine().toUpperCase();
      if (userInput.equals("MOVE") || userInput.equals("ATTACK") || userInput.equals("DONE")){
        userInput = userInput.substring(0,1);
      }
      if (userInput.equals("M") || userInput.equals("A") || userInput.equals("D")){
        if (userInput.equals("M") || userInput.equals("A")){
          output.println("Action Order Format: (SourceTerritoryName),(DestTerritoryName),(UnitType),(UnitAmount)");
        }
        return userInput;
      }
      else{throw new IllegalArgumentException("Can't find order! Please choose from (M)ove, (A)ttack, (D)one");}
    } catch (IllegalArgumentException e) {
      int offset = e.toString().indexOf(":") + 2;
      output.println(e.toString().substring(offset));
      return chooseOrder(input, output);
    }
  }

  private int readOrderUnitAmount (String[] inputs){
    int ans;
    try{
      ans = Integer.parseInt(inputs[3]);
    }
    catch (NumberFormatException e){
      throw new IllegalArgumentException("Unit Amount must be an integer!");
    }
    return ans;
  }

  private String[] checkFormatAndSplit(String userInput) throws IllegalArgumentException{
    String[] ans = userInput.split(",");
    if (ans.length != 4) {
      throw new IllegalArgumentException("Your input " + userInput + " is not following the format!");
    }
    return ans;
  }
  
  private void displayMapAndOptions(RISKMap riskMap, PrintStream output, long ID, TreeMap<Long, Color> idToColor) {
    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    output.println(mapTextView.displayMap());
    output.println("You are the " + idToColor.get(ID).getColorName() + " Player, what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
  }
}
