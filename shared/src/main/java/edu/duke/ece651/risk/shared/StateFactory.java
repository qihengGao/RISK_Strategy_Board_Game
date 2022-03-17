package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.state.RestoreState;

public class StateFactory {
    public RestoreState createRestoreState(){
        return new RestoreState();
    }
}
