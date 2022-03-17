package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.mockito.Mockito.*;

class RestoreStateTest {
//
//    InputStream is = new ByteArrayInputStream( "".getBytes() );
//        context.setBufferedReader(new BufferedReader(new InputStreamReader(is)));
    @Test
    void doAction() throws IOException, ClassNotFoundException {

        ClientContext context = mock(ClientContext.class);

        RestoreState restoreState = mock(RestoreState.class);
        RiskGameMessage riskGameMessage = mock(RiskGameMessage.class);

        RestoreState restoreState1 = mock(RestoreState.class);

        doReturn(riskGameMessage).when(context).readMessage();
        doCallRealMethod().when(restoreState).doAction(context);
        doReturn(restoreState1).when(riskGameMessage).getCurrentState();
        //doNothing().when(restoreState).readUserOptionThenNotifyServer(any());


        restoreState.doAction(context);
        verify(riskGameMessage).getCurrentState();
        verify(restoreState1).doAction(any());





    }

    @Test
    void initGame() throws IOException {
        ClientContext context = mock(ClientContext.class);

        RiskGameMessageFactory riskGameMessageFactory = mock(RiskGameMessageFactory.class);

        doReturn(riskGameMessageFactory).when(context).getRiskGameMessageFactory();

        new RestoreState().initGame(context);

        verify(context).writeObject(any());
        verify(riskGameMessageFactory).createInitMessage();
    }

    @Test
    void restoreGame() throws IOException {

        ClientContext context = mock(ClientContext.class);

        RiskGameMessageFactory riskGameMessageFactory = mock(RiskGameMessageFactory.class);

        doReturn(riskGameMessageFactory).when(context).getRiskGameMessageFactory();

        new RestoreState().restoreGame(context,1L);

        verify(context).writeObject(any());
        verify(riskGameMessageFactory).createReconnectMessage(context,1L);
    }

    @Test
    void readUserOptionThenNotifyServer() throws IOException {
        RestoreState restoreState = mock(RestoreState.class);
        ClientContext context = new ClientContext();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        context.setOut(out);

        doCallRealMethod().when(restoreState).readUserOptionThenNotifyServer(any());
        doReturn("").when(restoreState).readChoice(any(),any(),any(),any());

        restoreState.readUserOptionThenNotifyServer(context);
        //verify(restoreState).restoreGame(any(),any());
        verify(restoreState).initGame(any());

        doReturn("1").when(restoreState).readChoice(any(),any(),any(),any());

        restoreState.readUserOptionThenNotifyServer(context);
        //verify(restoreState).restoreGame(any(),any());
        verify(restoreState).restoreGame(any(),any());
    }
}