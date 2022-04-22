package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.APIGameHandler;

import java.util.Comparator;

public class APIGameHandlerComparator implements Comparator<APIGameHandler> {
    private long currElo;

    public APIGameHandlerComparator(long currElo) {
        this.currElo = currElo;
    }

    public int compare(APIGameHandler a, APIGameHandler b){
        if (a.getAverageElo()==b.getAverageElo()){
            return (int)a.getRoomID() - (int)b.getRoomID();
        }
        int diff_a = Math.abs((int)a.getAverageElo() - (int)currElo);
        int diff_b = Math.abs((int)b.getAverageElo() - (int)currElo);
        if (diff_a == diff_b){
            return (int)b.getAverageElo() - (int)a.getAverageElo();
        }
        return diff_a - diff_b;
    }

}
