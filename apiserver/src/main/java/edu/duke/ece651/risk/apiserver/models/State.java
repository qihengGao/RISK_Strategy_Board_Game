package edu.duke.ece651.risk.apiserver.models;

import edu.duke.ece651.risk.shared.state.WaitingState;

public enum State {
    WaitingState,
    PlacingState,
    OrderingState,
    LostState,
    EndState,
    WaitingToStartState


}