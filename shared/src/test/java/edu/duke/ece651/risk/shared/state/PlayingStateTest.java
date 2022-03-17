package edu.duke.ece651.risk.shared.state;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.Color;

public class PlayingStateTest {

    @Test
    public void test_doAction() throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = mock(PrintStream.class);
        Color color = new Color("red");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
        PlayingState playingState = mock(PlayingState.class);

        doCallRealMethod().when(playingState).doAction(clientContext);
        doReturn(printStream).when(clientContext).getOut();
        doReturn(color).when(clientContext).getClientColor();
        doReturn(objectOutputStream).when(clientContext).getOos();

        playingState.doAction(clientContext);
        verify(printStream).println("");
        verify(printStream).println("You are: red");
        verify(printStream).println("What would you");
    }
}
