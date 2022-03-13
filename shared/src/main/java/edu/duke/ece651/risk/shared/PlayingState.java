package edu.duke.ece651.risk.shared;

import java.io.IOException;

public class PlayingState extends State {
  @Override
    /**
     * Do placement action
     */
  public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {      
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
        contex.getOut().println(mapTextView.displayMap());
        contex.getOut().println("You are: " + contex.getClientColor().getColorName());
        contex.getOut().println("What would you ")
  }
}
