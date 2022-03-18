package edu.duke.ece651.risk.shared.state;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.ClientContext;

public class ShowRoundResultToViewersStateTest {

    @Test
    public void test_doAction() throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = mock(PrintStream.class);
        ShowRoundResultToViewersState thisState = mock(ShowRoundResultToViewersState.class);
        ShowGameResultState endState = mock(ShowGameResultState.class);

        doCallRealMethod().when(thisState).doAction(clientContext);
        doReturn(printStream).when(clientContext).getOut();
        doReturn(endState).when(clientContext).getGameState();

//        thisState.doAction(clientContext);
//        verify(clientContext, times(1)).getOut();
//        verify(endState, times(1)).doAction(clientContext);
    }
}
