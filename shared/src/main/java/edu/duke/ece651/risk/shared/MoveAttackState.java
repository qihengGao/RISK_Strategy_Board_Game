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
      orders.add(new Order(inputs[0], inputs[1], inputs[2], amountUnderOrder));
    }
  }

  private String tryReadLineFromContext(BufferedReader input, PrintStream output)
      throws IOException {
    String line = null;
    while (line == null) {
      line = input.readLine();
      if (line == null) {
        output.print("error: input cannot be empty");
        continue;
      }
    }
    return line;
  }

  // public static void readOrder(ArrayList<Order> orders, ClientContext contex)
  // throws IOException {
  // String userInput = contex.getBufferedReader().readLine();
  // BufferedReader bufferReader = contex.getBufferedReader();
  // PrintStream printStream = contex.getOut();

  // contex.getOut().println("Please enter your source territory name: ");
  // String sourceTerritoryName = this.tryReadLineFromBufferReader(bufferReader,
  // printStream);
  // contex.getOut().println("Please enter your destination territory name: ");
  // String destinationTerritoryName =
  // this.tryReadLineFromBufferReader(bufferReader, printStream);

  // MoveOrder newMoveOrder = new MoveOrder();

  // }

  // private void readAttackOrder(ArrayList<Order> orders, ClientContext contex)
  // throws IOException {
  // BufferedReader bufferReader = contex.getBufferedReader();
  // PrintStream printStream = contex.getOut();

  // contex.getOut().println("Please enter your source territory name: ");
  // String sourceTerritoryName = tryReadLineFromBufferReader(bufferReader,
  // printStream);
  // contex.getOut().println("Please enter your destination territory name: ");
  // String destinationTerritoryName = tryReadLineFromBufferReader(bufferReader,
  // printStream);
  // AttackOrder newAttackOrder;
  // }
}
