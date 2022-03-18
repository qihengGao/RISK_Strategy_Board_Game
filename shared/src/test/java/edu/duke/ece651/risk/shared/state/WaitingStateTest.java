package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class WaitingStateTest {

    @Test
    void doAction() throws IOException, ClassNotFoundException {

        ClientContext context = mock(ClientContext.class);

        WaitingState waitingState = mock(WaitingState.class);
        RiskGameMessage riskGameMessage = mock(RiskGameMessage.class);

        RestoreState restoreState1 = mock(RestoreState.class);

        doReturn(riskGameMessage).when(context).readMessage();
        doCallRealMethod().when(waitingState).doAction(context);
        doReturn(restoreState1).when(riskGameMessage).getCurrentState();
        //doNothing().when(restoreState).readUserOptionThenNotifyServer(any());


        waitingState.doAction(context);
        verify(riskGameMessage).getCurrentState();
        verify(restoreState1).doAction(any());
    }
}