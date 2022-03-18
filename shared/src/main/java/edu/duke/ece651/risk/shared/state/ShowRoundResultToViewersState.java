package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.map.MapTextView;

import java.io.IOException;
import java.util.regex.Pattern;

public class ShowRoundResultToViewersState extends State {
    @Override
    public void doAction(ClientContext context) throws IOException, ClassNotFoundException {

        if(context.isKeepWatchResult()){
            keepWatching(context);
        }else{
            String command = readChoice(context,"\nYou Lose! Conquer more next time!\nWhat do you like to do next?\n (D)isconnect\n (K)eep Watching",
                    "Invalid command!", Pattern.compile("^D$|^K$",Pattern.CASE_INSENSITIVE));
            switch(command){
                case "D":
                    context.println("Have a nice day!");
                    break;
                case "K":
                    context.setKeepWatchResult(true);
                    keepWatching(context);
                    break;

            }
        }

    }

    public void keepWatching(ClientContext context) throws IOException, ClassNotFoundException {
        MapTextView mapTextView = new MapTextView(context.getRiskMap(), context.getIdToColor());
        context.getOut().println(mapTextView.displayMap());

        //wait server for next state
        context.setGameState(new WaitingState());
        context.getGameState().doAction(context);
    }
}
