package edu.duke.ece651.risk.shared.state;

import static org.mockito.ArgumentMatchers.any;
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
    public void test_doAction_notKeepWatching() throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        ShowRoundResultToViewersState thisState = mock(ShowRoundResultToViewersState.class);   

        doCallRealMethod().when(thisState).doAction(clientContext);
        doReturn(true).when(clientContext).isKeepWatchResult();

        thisState.doAction(clientContext);
        verify(clientContext, times(1)).isKeepWatchResult();
    }

    @Test
    public void test_doAction_keepWatching_D() throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        ShowRoundResultToViewersState thisState = mock(ShowRoundResultToViewersState.class);   

        doCallRealMethod().when(thisState).doAction(clientContext);
        doReturn(false).when(clientContext).isKeepWatchResult();
        doReturn("D").when(thisState).readChoice(any(), any(), any(), any());

        thisState.doAction(clientContext);
        verify(clientContext, times(1)).println("Have a nice day!");
    }

    @Test
    public void test_doAction_keepWatching_K() throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        ShowRoundResultToViewersState thisState = mock(ShowRoundResultToViewersState.class);   

        doCallRealMethod().when(thisState).doAction(clientContext);
        doReturn(false).when(clientContext).isKeepWatchResult();
        doReturn("K").when(thisState).readChoice(any(), any(), any(), any());

        thisState.doAction(clientContext);
        verify(clientContext, times(1)).setKeepWatchResult(true);
        verify(thisState, times(1)).keepWatching(clientContext);
    }

    @Test
    public void test_keepWatching() throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = mock(PrintStream.class);
        ShowRoundResultToViewersState thisState = mock(ShowRoundResultToViewersState.class);
        ShowGameResultState endState = mock(ShowGameResultState.class);

        doCallRealMethod().when(thisState).keepWatching(clientContext);;
        doReturn(printStream).when(clientContext).getOut();
        doReturn(endState).when(clientContext).getGameState();

       thisState.keepWatching(clientContext);
       verify(clientContext, times(1)).getOut();
       verify(endState, times(1)).doAction(clientContext);
    }
}
