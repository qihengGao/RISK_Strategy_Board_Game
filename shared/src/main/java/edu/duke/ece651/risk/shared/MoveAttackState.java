package edu.duke.ece651.risk.shared;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

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
    MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
    contex.getOut().println(mapTextView.displayMap());
    contex.getOut().println("You are: " + contex.getClientColor().getColorName());
    ArrayList<Order> orders =  new ArrayList<>();
    readPlayerOrder(orders, contex.getBufferedReader(), contex.getOut());
    contex.getOos().writeObject(orders);
  }

  private void readPlayerOrder(ArrayList<Order> orders, BufferedReader input, PrintStream output) throws IOException {
    output.println("Please place your " + this.stateName + " instructions.");
    output.println("Format: SourceTerrirotyName,DestTerritoryName,UnitType,UnitAmount");
    boolean commit = false;
    while (!commit) {
      String userInput = input.readLine() ;
      if (userInput.equals("D")) {
        return;
      } // user commits
      String[] inputs = userInput.split(",");
      if (inputs.length != 4) {
        output.println("Your input " + userInput + " is not following the format");
        continue;
      }
      int amountUnderOrder;
      try {
        amountUnderOrder = Integer.parseInt(inputs[3]);
      } catch (NumberFormatException e) {
        output.println(inputs[3] + "is an Invalid input! Try again!");
        continue;
      }

      orders.add(new MoveOrder(inputs[0], inputs[1], inputs[2], amountUnderOrder));
    }
  }
}
