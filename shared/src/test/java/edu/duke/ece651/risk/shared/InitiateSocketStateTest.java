package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InitiateSocketStateTest {

    @Test
    public void test_useCustomServerAddress() throws IOException {
        ClientContext context = new ClientContext();

        InputStream is = new ByteArrayInputStream( "127.0.0.1\n1777\n".getBytes() );
        context.setBufferedReader(new BufferedReader(new InputStreamReader(is)));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        context.setOut(out);


        InitiateSocketState initiateSocketState = new InitiateSocketState();
        initiateSocketState.useCustomServerAddress(context);
        assertEquals("127.0.0.1",context.getServerAddress());
        assertEquals(1777,context.getPortNumber());

    }

    @Test
    public void test_useDefaultServerAddress() throws IOException {
        ClientContext context = new ClientContext();


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        context.setOut(out);


        InitiateSocketState initiateSocketState = new InitiateSocketState();
        initiateSocketState.useDefaultServerAddress(context);
        assertEquals("vcm-25035.vm.duke.edu",context.getServerAddress());
        assertEquals(1777,context.getPortNumber());

    }

    @Test
    void doAction() throws IOException, ClassNotFoundException {
        ClientContext context = mock(ClientContext.class);

        InitiateSocketState initiateSocketState = mock(InitiateSocketState.class);

        doCallRealMethod().when(initiateSocketState).doAction(context);
        doThrow(IOException.class).doNothing().when(initiateSocketState).initiateSocket(any());
        doNothing().when(context).println(any());

        StateFactory stateFactory = mock(StateFactory.class);

        doReturn(stateFactory).when(context).getStateFactory();

        RestoreState restoreState = mock(RestoreState.class);
        doReturn(restoreState).when(stateFactory).createRestoreState();

        initiateSocketState.doAction(context);

        verify(context).println("Connection Failed!");
        verify(stateFactory).createRestoreState();
        verify(restoreState).doAction(context);



    }

    @Test
    void initiateSocket() throws IOException {
        ClientContext context = mock(ClientContext.class);

        InitiateSocketState initiateSocketState = mock(InitiateSocketState.class);

        doCallRealMethod().when(initiateSocketState).initiateSocket(context);
        doReturn("D").when(initiateSocketState).readChoice(any(),any(),any(),any());
        initiateSocketState.initiateSocket(context);
        verify(initiateSocketState).useDefaultServerAddress(context);


        doReturn("C").when(initiateSocketState).readChoice(any(),any(),any(),any());
        initiateSocketState.initiateSocket(context);
        verify(initiateSocketState).useCustomServerAddress(context);
    }
}