package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.APIGameHandler;

import java.util.Comparator;

public class APIGameHandlerComparator implements Comparator<APIGameHandler> {

    public int compare(APIGameHandler a, APIGameHandler b){
        if (a.getAverageElo()==b.getAverageElo()){
            return (int)a.getRoomID() - (int)b.getRoomID();
        }
        return (int)b.getAverageElo() - (int)a.getAverageElo();
    }

}
