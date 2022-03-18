package edu.duke.ece651.risk.shared.factory;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.state.RestoreState;

public class StateFactoryTest {

    @Test
    public void test_createRestoreState(){
        StateFactory factory = new StateFactory();
        assertInstanceOf(RestoreState.class, factory.createRestoreState());
    }
}
