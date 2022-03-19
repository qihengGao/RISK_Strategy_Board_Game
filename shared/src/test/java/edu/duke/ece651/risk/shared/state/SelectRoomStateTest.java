package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class SelectRoomStateTest {

    @Test
    void doAction() throws IOException, ClassNotFoundException {

        ClientContext context = mock(ClientContext.class);

        SelectRoomState selectRoomState = mock(SelectRoomState.class);
        RiskGameMessage riskGameMessage = mock(RiskGameMessage.class);

        RestoreState restoreState1 = mock(RestoreState.class);

        doReturn(riskGameMessage).when(context).readMessage();
        doCallRealMethod().when(selectRoomState).doAction(context);
        doReturn(restoreState1).when(riskGameMessage).getCurrentState();
        //doNothing().when(restoreState).readUserOptionThenNotifyServer(any());


        selectRoomState.doAction(context);
        verify(riskGameMessage).getCurrentState();
        verify(restoreState1).doAction(any());
    }

    @Test
    void createAGameRoom() throws IOException {
        SelectRoomState selectRoomState = mock(SelectRoomState.class);
        ClientContext context = mock(ClientContext.class);
        doCallRealMethod().when(selectRoomState).createAGameRoom(any());
        doReturn("3").when(selectRoomState).readChoice(any(),any(),any(),any());

        RiskGameMessageFactory riskGameMessageFactory = mock(RiskGameMessageFactory.class);

        doReturn(riskGameMessageFactory).when(context).getRiskGameMessageFactory();

        selectRoomState.createAGameRoom(context);

        verify(riskGameMessageFactory).createCreateGameRoomMessage(3);

    }

    @Test
    void joinAGameRoom() throws IOException {
        SelectRoomState selectRoomState = mock(SelectRoomState.class);
        ClientContext context = mock(ClientContext.class);
        doCallRealMethod().when(selectRoomState).joinAGameRoom(any());
        doReturn("3").when(selectRoomState).readChoice(any(),any(),any(),any());

        RiskGameMessageFactory riskGameMessageFactory = mock(RiskGameMessageFactory.class);

        doReturn(riskGameMessageFactory).when(context).getRiskGameMessageFactory();

        selectRoomState.joinAGameRoom(context);

        verify(riskGameMessageFactory).createJoinGameRoomMessage(3);

    }

    @Test
    void readUserOptionThenNotifyServer() throws IOException {

        SelectRoomState selectRoomState = mock(SelectRoomState.class);
        ClientContext context = new ClientContext();


        doCallRealMethod().when(selectRoomState).readUserOptionThenNotifyServer(any());
        doReturn("C").when(selectRoomState).readChoice(any(),any(),any(),any());

        selectRoomState.readUserOptionThenNotifyServer(context);
        //verify(restoreState).restoreGame(any(),any());
        verify(selectRoomState).createAGameRoom(any());

        doReturn("J").when(selectRoomState).readChoice(any(),any(),any(),any());

        selectRoomState.readUserOptionThenNotifyServer(context);
        //verify(restoreState).restoreGame(any(),any());
        verify(selectRoomState).joinAGameRoom(any());
    }
}