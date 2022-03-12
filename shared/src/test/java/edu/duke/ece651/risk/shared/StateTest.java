package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class StateTest {
  @Test
  public void test_readServerPort() throws IOException{
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
  public void test_readServerAddress() throws IOException{
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

}
