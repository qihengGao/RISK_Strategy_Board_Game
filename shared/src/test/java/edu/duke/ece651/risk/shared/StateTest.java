package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StateTest {
    @Test
    public void test_readServerPort() throws IOException {
        State testState = new WaitingState();

        //Success
        BufferedReader input = new BufferedReader(new StringReader("1777\n"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        assertEquals(testState.readServerPort("Please type in server port number",
                input,
                output), 1777);
        assertEquals(bytes.toString(), "Please type in server port number\n");

        //Exceptions
        /**empty input
         */
        BufferedReader empty_input = new BufferedReader(new StringReader(""));
        assertThrows(EOFException.class, () -> testState.readServerPort("Please type in server port number",
                empty_input,
                output));
        /**invalid input format
         */
        BufferedReader invalid_input = new BufferedReader(new StringReader("abcd"));
        assertThrows(NumberFormatException.class, () -> testState.readServerPort("Please type in server port number",
                invalid_input,
                output));
    }

    @Test
    public void test_readServerAddress() throws IOException {
        State testState = new WaitingState();

        //Success
        BufferedReader input = new BufferedReader(new StringReader("localhost"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        assertEquals(testState.readServerAddress("Please type in server address",
                input,
                output), "localhost");
        assertEquals(bytes.toString(), "Please type in server address\n");

        //Exceptions
        /**empty input
         */
        BufferedReader empty_input = new BufferedReader(new StringReader(""));
        assertThrows(EOFException.class, () -> testState.readServerAddress("Please type in server address",
                empty_input,
                output));
    }

    @Test
    void connectToServer() throws IOException {

        final String hostname = "127.0.0.1";
        final int port = 1777;
        SocketFactory socketFactory = mock(SocketFactory.class);
        ClientContext mockClientContext = mock(ClientContext.class);
        when(mockClientContext.getServerAddress()).thenReturn(hostname);
        when(mockClientContext.getPortNumber()).thenReturn(port);
        when(mockClientContext.getSocketFactory()).thenReturn(socketFactory);

        Socket socket = mock(Socket.class);
        when(socketFactory.createSocket()).thenReturn(socket);


        when(socket.isConnected()).thenReturn(false, false,true,true);

        //when(socket.connect(any(SocketAddress.class))).thenThrow(new IOException());
        doThrow(new IOException("test")).doNothing().when(socket).connect(any(SocketAddress.class));

        when(socket.getInputStream()).thenReturn(mock(InputStream.class));
        when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));

        SocketAddress socketAddress = mock(SocketAddress.class);
        when(socketFactory.createSocketAddress(hostname, port)).thenReturn(socketAddress);

        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        when(socketFactory.createObjectInputStream(any(InputStream.class))).thenReturn(objectInputStream);

        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        when(socketFactory.createObjectOutputStream(any(OutputStream.class))).thenReturn(objectOutputStream);

        when(mockClientContext.getSocket()).thenReturn(socket);


        State state = new InitiateSocketState();
        assertTrue(state.connectToServer(mockClientContext));

        verify(mockClientContext).setSocket(any(Socket.class));
        verify(mockClientContext).setOis(objectInputStream);
        verify(mockClientContext).setOos(objectOutputStream);


    }


    @Test
    void writeObject() throws IOException {
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = mock(PrintStream.class);
        when(clientContext.getOut()).thenReturn(printStream);
        doThrow(IOException.class).doThrow(IOException.class).doNothing().when(clientContext).writeObject(any(Object.class));


        InitiateSocketState state = mock(InitiateSocketState.class);

        doCallRealMethod().when(state).writeObject(any(ClientContext.class),any(Object.class));
        when(state.connectToServer(clientContext)).thenReturn(Boolean.FALSE, Boolean.TRUE);
        assertThrows(IOException.class,()->state.writeObject(clientContext,new Object()));


        state.writeObject(clientContext,new Object());
        verify(clientContext,times(3)).writeObject(any(Object.class));

    }
}
