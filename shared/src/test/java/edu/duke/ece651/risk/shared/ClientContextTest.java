package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertInstanceOf;
// import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TreeMap;

import edu.duke.ece651.risk.shared.factory.RiskGameMessageFactory;
import edu.duke.ece651.risk.shared.factory.SocketFactory;
import edu.duke.ece651.risk.shared.factory.StateFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.state.InitiateSocketState;
import edu.duke.ece651.risk.shared.state.State;
import org.junit.jupiter.api.Test;

public class ClientContextTest {
   @Test
   static void assertEqualsIgnoreLineSeparator(String expected, String actual) {
      assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
            actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
   }

   /**
    * test for default constructor
    * and getRiskGameMessageFactory and getSocketFactory
    */
   @Test
   public void test_defaultConstructor() {
      ClientContext contex = new ClientContext();
      assertInstanceOf(RiskGameMessageFactory.class, contex.getRiskGameMessageFactory());
      assertInstanceOf(SocketFactory.class, contex.getSocketFactory());
   }

   /**
    * test for getServerAddress and setServerAddress
    */
   @Test
   public void test_getSetServerAddress() {
      String serverAddress = "whatever";
      ClientContext contex = new ClientContext();
      contex.setServerAddress(serverAddress);
      assertEquals(serverAddress, contex.getServerAddress());
   }

   /**
    * test for getPortNumber and setPortNumber
    */
   @Test
   public void test_getSetPortNumber() {
      ClientContext contex = new ClientContext();
      contex.setPortNumber(123);
      assertEquals(123, contex.getPortNumber());
   }

   /**
    * test for getIdToColor and setIdToColor
    */
   @Test
   public void test_getSetIdToColor() {
      TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
      idToColor.put(Long.valueOf(0), new Color("red"));
      idToColor.put(Long.valueOf(1), new Color("yellow"));

      ClientContext contex = new ClientContext();
      contex.setIdToColor(idToColor);

      assertEquals(idToColor, contex.getIdToColor());
   }

   /**
    * test for getPlayerId and setPlayerId
    */
   @Test
   public void test_getSetPlayerID() {
      ClientContext contex = new ClientContext();
      assertEquals(0, contex.getPlayerID());

      contex.setPlayerID(5);
      assertEquals(5, contex.getPlayerID());
   }

   /**
    * test for getClientColor
    */
   @Test
   public void test_getClientColor() {
      TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
      idToColor.put(Long.valueOf(0), new Color("red"));
      idToColor.put(Long.valueOf(1), new Color("yellow"));

      ClientContext contex = new ClientContext();
      contex.setIdToColor(idToColor);
      contex.setPlayerID(0);

      assertEquals(new Color("red"), contex.getClientColor());
   }

   /**
    * test for getOos and setOos
    * 
    * @throws UnknownHostException
    * @throws IOException
    */
   @Test
   public void test_getSetOos() throws UnknownHostException, IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      ObjectOutputStream objectOutput = new ObjectOutputStream(buffer);

      ClientContext contex = new ClientContext();
      contex.setOos(objectOutput);

      assertSame(objectOutput, contex.getOos());
   }

   /**
    * test for getOis and setOis
    * 
    * @throws IOException
    */
   @Test
   public void test_getSetOis() throws IOException {
      ObjectInputStream in = mock(ObjectInputStream.class);
      ClientContext contex = new ClientContext();
      contex.setOis(in);
      assertSame(in, contex.getOis());
   }

   /**
    * test for getGameState and setGameState
    */
   @Test
   public void test_getSetGameState() {
      State state = new InitiateSocketState();
      ClientContext contex = new ClientContext();
      contex.setGameState(state);

      assertSame(state, contex.getGameState());
   }

   /**
    * test for getSocket and setSocket
    */
   @Test
   public void test_getSetSocket() {
      Socket socket = mock(Socket.class);
      ClientContext contex = new ClientContext();
      contex.setSocket(socket);
      assertSame(socket, contex.getSocket());
   }

   /**
    * test for getRiskMap and setRiskMap
    */
   @Test
   public void test_getSetRiskMap() {
      RISKMap map = mock(RISKMap.class);
      ClientContext contex = new ClientContext();
      contex.setRiskMap(map);
      assertSame(map, contex.getRiskMap());
   }

   /**
    * test for getBufferedReader and setBufferedReader
    */
   @Test
   public void test_getSetBufferedReader() {
      BufferedReader bufferedReader = mock(BufferedReader.class);
      ClientContext contex = new ClientContext();
      contex.setBufferedReader(bufferedReader);
      assertSame(bufferedReader, contex.getBufferedReader());
   }

   @Test
   public void test_getSetOut() {
      PrintStream out = mock(PrintStream.class);
      ClientContext contex = new ClientContext();
      contex.setOut(out);
      assertSame(out, contex.getOut());
   }

   @Test
   public void test_writeObject() throws IOException, ClassNotFoundException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      ObjectOutputStream objectOutput = new ObjectOutputStream(buffer);

      ClientContext contex = new ClientContext();
      contex.setOos(objectOutput);

      String expected = "hello";
      contex.writeObject(expected);

      byte[] bytes = buffer.toByteArray();

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
      String output = (String) objectInputStream.readObject();

      assertEquals(expected, output);
   }

   @Test
   public void test_keepWatch() {
      ClientContext c = new ClientContext();
      assertFalse(c.isKeepWatchResult());
      c.setKeepWatchResult(true);
      assertTrue(c.isKeepWatchResult());
   }

   @Test
   public void test_out() {
      ClientContext c = new ClientContext();
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      PrintStream p = new PrintStream(b);
      c.setOut(p);
      c.println("hello");
      assertEqualsIgnoreLineSeparator("hello\n", b.toString());
   }

   @Test
   public void test_readMsg() throws IOException, ClassNotFoundException {
      ObjectInput ois = mock(ObjectInput.class);
      ClientContext c = spy(new ClientContext());
      c.setOis(ois);
      // doCallRealMethod().when(c).readMessage();
      doReturn(new RiskGameMessage()).when(ois).readObject();
      c.readMessage();
      verify(ois).readObject();
   }

   @Test
   public void test_setStateFac() {
      ClientContext c = new ClientContext();
      assertInstanceOf(StateFactory.class, c.getStateFactory());
   }
}
