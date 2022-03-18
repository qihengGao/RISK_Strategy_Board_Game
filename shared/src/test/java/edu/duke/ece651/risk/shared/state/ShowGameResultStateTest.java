package edu.duke.ece651.risk.shared.state;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = mock(PrintStream.class);
        ShowGameResultState showGameResultState = mock(ShowGameResultState.class);

        doCallRealMethod().when(showGameResultState).doAction(clientContext);
        doReturn(printStream).when(clientContext).getOut();

        showGameResultState.doAction(clientContext);



    }
}
