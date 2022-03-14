package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class ClientContextTest {

  /**
    * test for getPortNumber and setPortNumber
    */
   @Test
   public void test_getSetPortNumber(){
      ClientContext contex = new ClientContext();
      contex.setPortNumber(123);
      assertEquals(123, contex.getPortNumber());
   }
  
   /**
    * test for getIdToColor and setIdToColor
    */
   @Test
   public void test_getSetIdToColor(){
      TreeMap<Long,Color> idToColor = new TreeMap<Long, Color>();
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
   public void test_getSetPlayerID(){
      ClientContext contex = new ClientContext();
      assertEquals(0, contex.getPlayerID());

      contex.setPlayerID(5);
      assertEquals(5, contex.getPlayerID());
   }

   /**
    * test for getClientColor
    */
   @Test
   public void test_getClientColor(){
      TreeMap<Long,Color> idToColor = new TreeMap<Long, Color>();
      idToColor.put(Long.valueOf(0), new Color("red"));
      idToColor.put(Long.valueOf(1), new Color("yellow"));

      ClientContext contex = new ClientContext();
      contex.setIdToColor(idToColor);
      contex.setPlayerID(0);

      assertEquals(new Color("red"), contex.getClientColor());
   }

   /**
    * test for getOos and setOos
    * @throws UnknownHostException
    * @throws IOException
    */
   @Test
   public void test_getSetOos() throws UnknownHostException, IOException{
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      ObjectOutput objectOutput = new ObjectOutputStream(buffer);

      ClientContext contex = new ClientContext();
      contex.setOos(objectOutput);

      assertSame(objectOutput, contex.getOos());
   }

   /**
    * test for getOis and setOis
    * @throws IOException
    */
   @Test
   public void test_getSetOis() throws IOException{
      ObjectInput in = mock(ObjectInputStream.class);
      ClientContext contex = new ClientContext();
      contex.setOis(in);
      assertSame(in, contex.getOis());
   }

   /**
    * test for getGameState and setGameState
    */
   @Test
   public void test_getSetGameState(){
      State state = new InitiateSocketState();
      ClientContext contex = new ClientContext();
      contex.setGameState(state);

      assertSame(state, contex.getGameState());
   }

   /**
    * test for getSocket and setSocket
    */
   @Test
   public void test_getSetSocket(){
      Socket socket = mock(Socket.class);
      ClientContext contex = new ClientContext();
      contex.setSocket(socket);
      assertSame(socket, contex.getSocket());
   }

   /**
    * test for getRiskMap and setRiskMap
    */
   @Test
   public void test_getSetRiskMap(){
      RISKMap map = mock(RISKMap.class);
      ClientContext contex = new ClientContext();
      contex.setRiskMap(map);
      assertSame(map, contex.getRiskMap());
   }
   
   /**
    * test for getBufferedReader and setBufferedReader
    */
   @Test
   public void test_getSetBufferedReader(){
      BufferedReader bufferedReader = mock(BufferedReader.class);
      ClientContext contex = new ClientContext();
      contex.setBufferedReader(bufferedReader);
      assertSame(bufferedReader, contex.getBufferedReader());
   }

   @Test
   public void test_getSetOut(){
      PrintStream out = mock(PrintStream.class);
      ClientContext contex = new ClientContext();
      contex.setOut(out);
      assertSame(out, contex.getOut());
   }
}
