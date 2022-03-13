package edu.duke.ece651.risk.shared;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

public class PlayingState extends State {
  @Override
  /**
   * Do placement action
   */
  public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
    MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
    contex.getOut().println(mapTextView.displayMap());
    contex.getOut().println("You are: " + contex.getClientColor().getColorName());
    ArrayList<Order> orders = new ArrayList<>();
    boolean playerCommit = false;
    while (!playerCommit) {
      playerCommit = readPlayerOrder(orders, contex);
    }
  }

  private boolean readPlayerOrder(ArrayList<Order> orders, ClientContext contex) throws IOException {
    contex.getOut().println("What would you like to do?");
    HashSet<String> supportActions = new HashSet<>();
    supportActions.add("M");
    supportActions.add("A");
    supportActions.add("D");
    contex.getOut().println("(M)ove\n(A)ttack\n(D)one\n");
    String userInput = contex.getBufferedReader().readLine();
    userInput = userInput.toUpperCase();
    switch (userInput) {
      case "M":
        readMoveOrder(orders, contex);
        return false;
      case "A":
        readAttackOrder(orders, contex);
        return false;
      case "D":
        return true;
      default:
        contex.getOut().println("You input is not a supported action.");
        return false;
    }
  }

  private String tryReadLineFromBufferReader(BufferedReader bufferedReader, PrintStream printStream) throws IOException{
    String line = null;
    while(line == null){
      line = bufferedReader.readLine();
      if(line == null){
        printStream.println("error: input cannot be empty, please enter a territory name");
        throw new EOFException();
      }
    }
    return line;
  }

  private void readMoveOrder(ArrayList<Order> orders, ClientContext contex) throws IOException {
    BufferedReader bufferReader = contex.getBufferedReader();
    PrintStream printStream = contex.getOut();

    contex.getOut().println("Please enter your source territory name: ");
    String sourceTerritoryName = this.tryReadLineFromBufferReader(bufferReader, printStream);
    contex.getOut().println("Please enter your destination territory name: ");
    String destinationTerritoryName = this.tryReadLineFromBufferReader(bufferReader, printStream);

    MoveOrder newMoveOrder = new MoveOrder();

  }

  private void readAttackOrder(ArrayList<Order> orders, ClientContext contex) throws IOException {
    BufferedReader bufferReader = contex.getBufferedReader();
    PrintStream printStream = contex.getOut();

    contex.getOut().println("Please enter your source territory name: ");
    String sourceTerritoryName = tryReadLineFromBufferReader(bufferReader, printStream);
    contex.getOut().println("Please enter your destination territory name: ");
    String destinationTerritoryName = tryReadLineFromBufferReader(bufferReader, printStream);
    AttackOrder newAttackOrder;
  }
}
