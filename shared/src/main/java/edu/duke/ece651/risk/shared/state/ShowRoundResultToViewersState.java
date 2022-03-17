package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.map.MapTextView;

import java.io.IOException;

public class ShowRoundResultToViewersState extends State {
    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
        contex.getOut().println(mapTextView.displayMap());

        //wait server for next state
        contex.setGameState(new WaitingState());
        contex.getGameState().doAction(contex);
    }
}
