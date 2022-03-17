package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.MapTextView;
import edu.duke.ece651.risk.shared.Order;

import java.io.IOException;
import java.util.ArrayList;

public class PlayingState extends State {
  @Override
    /**
     * Do placement action
     */
  public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
        contex.getOut().println(mapTextView.displayMap());
        contex.getOut().println("You are: " + contex.getClientColor().getColorName());
        contex.getOut().println("What would you");
        ArrayList<Order> orders = new ArrayList<>();
        // orders.add(new Order("Terr1", "Terr2", "Soldier", 3));
        contex.getOos().writeObject(orders);
  }
}
