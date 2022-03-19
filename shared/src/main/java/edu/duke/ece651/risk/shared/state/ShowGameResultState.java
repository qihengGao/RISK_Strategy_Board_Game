package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.map.MapTextView;

import java.io.IOException;

public class ShowGameResultState extends State {
    @Override
    public void doAction(ClientContext contex) throws IOException, ClassNotFoundException {
//        MapTextView mapTextView = new MapTextView(contex.getRiskMap(), contex.getIdToColor());
//        contex.getOut().println(mapTextView.displayMap());

        contex.println("Press any key to exit...");
        try
        {
            contex.getBufferedReader().read();
        }
        catch(IOException ignored){

        }

    }
}
