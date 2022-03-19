package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;
import edu.duke.ece651.risk.shared.factory.SocketFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StateTest {

    private void assertEqualsIgnoreLineSeparator(String expected, String actual) {
        assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
                actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
    }

    @Test
    public void test_readServerPort() throws IOException {
        State testState = new WaitingState();

        //Success
        BufferedReader input = new BufferedReader(new StringReader("1777\n"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        ClientContext context = new ClientContext();
        context.setOut(output);
        context.setBufferedReader(input);
        assertEquals(testState.readServerPort(context,"Please type in server port number"
), 1777);
        assertEquals(bytes.toString(), "Please type in server port number\n");

        //Exceptions
        /**empty input
         */
        BufferedReader empty_input = new BufferedReader(new StringReader(""));
        assertThrows(EOFException.class, () -> testState.readServerPort(context,"Please type in server port number"
));
        /**invalid input format
         */
        BufferedReader invalid_input = new BufferedReader(new StringReader("abcd"));
        context.setBufferedReader(invalid_input);
        assertThrows(NumberFormatException.class, () -> testState.readServerPort(context,"Please type in server port number"));
    }

    @Test
    public void test_readServerAddress() throws IOException {
        State testState = new WaitingState();

        //Success
        BufferedReader input = new BufferedReader(new StringReader("localhost"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        ClientContext context = new ClientContext();
        context.setOut(output);
        context.setBufferedReader(input);
        assertEquals(testState.readServerAddress(context,"Please type in server address"), "localhost");
        assertEquals(bytes.toString(), "Please type in server address\n");

        //Exceptions
        /**empty input
         */
        BufferedReader empty_input = new BufferedReader(new StringReader(""));
        assertThrows(EOFException.class, () -> testState.readServerAddress(context,"Please type in server address"));
    }

    /**
     * test for readClientId
     * @throws NumberFormatException
     * @throws IOException
     */
    @Test
    public void test_readClientID() throws NumberFormatException, IOException{
        State testState = new WaitingState();
        ClientContext clientContext = new ClientContext();
        BufferedReader bufferedReader = new BufferedReader(new StringReader("1"));
        PrintStream printStream = new PrintStream(System.out);
        clientContext.setBufferedReader(bufferedReader);
        clientContext.setOut(printStream);
        String prompt = "prompt";
        Long result = testState.readClientID(clientContext, prompt);
        assertEquals(1L, result);   
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


        State state = spy(new InitiateSocketState());
        state.connectToServer(mockClientContext);

        verify(mockClientContext,times(2)).setSocket(any(Socket.class));
        verify(mockClientContext).setOis(objectInputStream);
        verify(mockClientContext).setOos(objectOutputStream);

        when(socket.isConnected()).thenReturn(false);
        assertThrows(IOException.class, () -> state.connectToServer(mockClientContext));
    }

    @Test
    public void test_reconnectToServer() throws IOException{
        ClientContext clientContext = mock(ClientContext.class);
        RiskGameMessageFactory factory = mock(RiskGameMessageFactory.class);
        RiskGameMessage message = mock(RiskGameMessage.class);
        long playerId = 0L;

        InitiateSocketState state = mock(InitiateSocketState.class);
        
        doCallRealMethod().when(state).reconnectToServer(clientContext);
        doReturn(factory).when(clientContext).getRiskGameMessageFactory();

        doReturn(playerId).when(clientContext).getPlayerID();
        doReturn(message).when(factory).createReconnectMessage(clientContext, playerId);
        
        boolean result = state.reconnectToServer(clientContext);
        assertEquals(true, result);

        doThrow(new IOException("test")).when(clientContext).writeObject(any());
        result = state.reconnectToServer(clientContext);
        assertEquals(false, result);
    }

    @Test
    public void test_initConnectToServer() throws IOException{
        ClientContext clientContext = mock(ClientContext.class);
        RiskGameMessageFactory factory = mock(RiskGameMessageFactory.class);
        RiskGameMessage message = mock(RiskGameMessage.class);

        InitiateSocketState state = mock(InitiateSocketState.class);

        doCallRealMethod().when(state).initConnectToServer(clientContext);
        doReturn(factory).when(clientContext).getRiskGameMessageFactory();
        doReturn(message).when(factory).createInitMessage();

        boolean result = state.initConnectToServer(clientContext);
        assertTrue(result);

        doThrow(new IOException("test")).when(clientContext).writeObject(any());
        result = state.initConnectToServer(clientContext);
        assertFalse(result);
    }

    @Test
    void writeObject() throws IOException {
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = mock(PrintStream.class);
        when(clientContext.getOut()).thenReturn(printStream);
        doThrow(IOException.class).doThrow(IOException.class).doNothing().when(clientContext).writeObject(any(Object.class));


        InitiateSocketState state = mock(InitiateSocketState.class);

        doCallRealMethod().when(state).writeObject(any(ClientContext.class),any(Object.class));
        when(state.reconnectToServer(clientContext)).thenReturn(Boolean.FALSE, Boolean.TRUE);
        assertThrows(IOException.class,()->state.writeObject(clientContext,new Object()));


        state.writeObject(clientContext,new Object());
        verify(clientContext,times(3)).writeObject(any(Object.class));

    }

    @Test
    public void test_readChoice() throws IOException{
        InitiateSocketState state = new InitiateSocketState();
        ClientContext clientContext = new ClientContext();
        BufferedReader bufferedReader = new BufferedReader(new StringReader("patter\npattern\n"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bytes, true);        
        String prompt = "prompt";
        String invalidPrompt = "invalid prompt";
        Pattern pattern = Pattern.compile("pattern", Pattern.CASE_INSENSITIVE);

        clientContext.setBufferedReader(bufferedReader);
        clientContext.setOut(printStream);
        String actual = state.readChoice(clientContext, prompt, invalidPrompt, pattern);
        assertEquals("PATTERN", actual);
        this.assertEqualsIgnoreLineSeparator("prompt\ninvalid prompt\n", bytes.toString());
    }

    @Test
    public void test_updateContextWithMessage(){
        ClientContext clientContext = new ClientContext();
        RiskGameMessage message = new RiskGameMessage();
        State state = new InitiateSocketState();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bytes, true);   
        clientContext.setOut(printStream);

        String prompt = "prompt\n";
        message.setPrompt(prompt);
        message.setClientid(0L);
        message.setCurrentState(state);

        state.updateContextWithMessage(clientContext, message);
        assertEquals(0L, clientContext.getPlayerID());
        assertNull(clientContext.getRiskMap());
        assertNull(clientContext.getIdToColor());

        prompt = "";
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());
        clientContext.setRiskMap(riskMap);
        state.updateContextWithMessage(clientContext, message);
        assertEquals(0L, clientContext.getPlayerID());
        assertSame(riskMap, clientContext.getRiskMap());
        assertNull(clientContext.getIdToColor());

        Color color = new Color("red");
        TreeMap<Long, Color> idToColor = new TreeMap<>();
        idToColor.put(0L, color);
        clientContext.setIdToColor(idToColor);
        state.updateContextWithMessage(clientContext, message);
        assertEquals(0L, clientContext.getPlayerID());
        assertSame(riskMap, clientContext.getRiskMap());
        assertSame(idToColor, clientContext.getIdToColor());
    }

    @Test
    public void test_updateContextWithMessage_nullCases(){
        ClientContext clientContext = mock(ClientContext.class);
        RiskGameMessage message = mock(RiskGameMessage.class);
        RISKMap riskMap = mock(RISKMap.class);
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
        idToColor.put(0L, new Color("red"));

        State thisState = mock(InitiateSocketState.class);

        doCallRealMethod().when(thisState).updateContextWithMessage(clientContext, message);
        doReturn(null).when(message).getPrompt();
        doReturn(riskMap).when(message).getRiskMap();
        doReturn(idToColor).when(message).getIdToColor();

        thisState.updateContextWithMessage(clientContext, message);
        verify(clientContext).setGameState(any());
        verify(clientContext).setPlayerID(anyLong());
        verify(clientContext).setRiskMap(any());
        verify(clientContext).setIdToColor(any());
    }
}
