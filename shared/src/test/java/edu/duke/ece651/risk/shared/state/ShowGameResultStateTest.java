package edu.duke.ece651.risk.shared.state;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.ClientContext;

public class ShowGameResultStateTest {
 
    /**
     * test for doAction
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Test
    public void test_doAction() throws ClassNotFoundException, IOException{
//        ClientContext clientContext = mock(ClientContext.class);
//        PrintStream printStream = mock(PrintStream.class);
//        ShowGameResultState showGameResultState = mock(ShowGameResultState.class);
//
//        doCallRealMethod().when(showGameResultState).doAction(clientContext);
//        doReturn(printStream).when(clientContext).getOut();
//
//        showGameResultState.doAction(clientContext);



    }

    @Test
    void doAction() throws IOException, ClassNotFoundException {
        ClientContext clientContext = mock(ClientContext.class);

        ShowGameResultState showGameResultState = new ShowGameResultState();

        BufferedReader bufferedReader = mock(BufferedReader.class);

        doReturn(bufferedReader).when(clientContext).getBufferedReader();
        doThrow(IOException.class).doReturn(1).when(bufferedReader).read();
        showGameResultState.doAction(clientContext);
        showGameResultState.doAction(clientContext);

    }
}
