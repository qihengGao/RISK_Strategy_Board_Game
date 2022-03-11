package edu.duke.ece651.risk.shared;

import java.io.IOException;

public class UnitPlacementState extends State {


    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(),contex.getIdToColor());

        System.out.println(mapTextView.displayMapInit());
        System.out.println(mapTextView.displayMap());
        System.out.println("You are: " + contex.getClientColor().getColorName());
//        System.out.println("Closing socket and terminating program.");
    }
}
