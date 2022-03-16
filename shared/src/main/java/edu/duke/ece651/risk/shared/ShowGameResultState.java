package edu.duke.ece651.risk.shared;

import java.io.IOException;

public class ShowGameResultState extends State {
    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
        contex.getOut().println(mapTextView.displayMap());
    }
}
