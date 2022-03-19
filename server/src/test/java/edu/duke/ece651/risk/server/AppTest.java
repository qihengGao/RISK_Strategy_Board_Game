/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.client.RiskGameClient;
import edu.duke.ece651.risk.shared.ClientContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {


    @Test
    static void assertEqualsIgnoreLineSeparator(String expected, String actual) {
        assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
                actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
    }


    @Disabled
    @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
    @Test

    void test_normalInput() throws IOException, ClassNotFoundException, InterruptedException {

        int randomPortNumber = (int) Math.floor(Math.random()*(9092-9080+1)+9080);
        RiskGameServer riskGameServer = new RiskGameServer( new ServerSocket(randomPortNumber));
        riskGameServer.start();

        ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
        ClientContext context1 = new ClientContext();
        PrintStream out1 = new PrintStream(bytes1, true);
        context1.setOut(out1);
        InputStream input1 = getClass().getClassLoader().getResourceAsStream("Input_Client1_v1.txt");
        context1.setBufferedReader(new BufferedReader(new InputStreamReader(input1)));
        RiskGameClient client1 = new RiskGameClient(context1,randomPortNumber);
        client1.start();
        TimeUnit.MILLISECONDS.sleep(200);
        client1.stop();
        //context1.getSocket().close();

        TimeUnit.MILLISECONDS.sleep(100);

        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        ClientContext context2 = new ClientContext();
        PrintStream out2 = new PrintStream(bytes2, true);
        context2.setOut(out2);
        InputStream input2 = getClass().getClassLoader().getResourceAsStream("Input_Client2_v1.txt");
        context2.setBufferedReader(new BufferedReader(new InputStreamReader(input2)));
        RiskGameClient client2 = new RiskGameClient(context2,randomPortNumber);
        client2.start();


        //context2.getSocket().close();


        ByteArrayOutputStream bytes4 = new ByteArrayOutputStream();
        ClientContext context4 = new ClientContext();
        InputStream input4 = getClass().getClassLoader().getResourceAsStream("Input_Client4_v1.txt");
        PrintStream out4 = new PrintStream(bytes4, true);
        context4.setBufferedReader(new BufferedReader(new InputStreamReader(input4)));
        context4.setOut(out4);
        RiskGameClient client4 = new RiskGameClient(context4,randomPortNumber);
        client4.start();


        TimeUnit.MILLISECONDS.sleep(100);

        ByteArrayOutputStream bytes3 = new ByteArrayOutputStream();
        ClientContext context3 = new ClientContext();
        InputStream input3 = getClass().getClassLoader().getResourceAsStream("Input_Client3_v1.txt");
        PrintStream out3 = new PrintStream(bytes3, true);
        context3.setBufferedReader(new BufferedReader(new InputStreamReader(input3)));
        context3.setOut(out3);
        RiskGameClient client3 = new RiskGameClient(context3,randomPortNumber);
        client3.start();

        TimeUnit.MILLISECONDS.sleep(100);


        ByteArrayOutputStream bytes5 = new ByteArrayOutputStream();
        ClientContext context5 = new ClientContext();
        InputStream input5 = getClass().getClassLoader().getResourceAsStream("Input_Client5_v1.txt");
        PrintStream out5 = new PrintStream(bytes5, true);
        context5.setBufferedReader(new BufferedReader(new InputStreamReader(input5)));
        context5.setOut(out5);
        RiskGameClient client5 = new RiskGameClient(context5,randomPortNumber);
        client5.start();






        client2.join();
        client3.join();
        client4.join();
        client5.join();

        String expected1 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client1_v1.txt").readAllBytes());
        assertEqualsIgnoreLineSeparator(expected1, bytes1.toString());

        String expected2 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client2_v1.txt").readAllBytes());
        assertEqualsIgnoreLineSeparator(expected2, bytes2.toString());

        String expected3 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client3_v1.txt").readAllBytes());
        assertEqualsIgnoreLineSeparator(expected3, bytes3.toString());

        String expected4 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client4_v1.txt").readAllBytes());
        assertEqualsIgnoreLineSeparator(expected4, bytes4.toString());

        String expected5 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client5_v1.txt").readAllBytes());
        assertEqualsIgnoreLineSeparator(expected5, bytes5.toString());
    }

//    @Test
//    @Disabled
//    public void test_multi_game() throws InterruptedException, IOException {
//        int randomPortNumber = (int) Math.floor(Math.random()*(10087-9080+1)+9093);
//        RiskGameServer riskGameServer = new RiskGameServer(new ServerSocket(randomPortNumber));
//        riskGameServer.start();
//
//        ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
//        ClientContext context1 = new ClientContext();
//        PrintStream out1 = new PrintStream(bytes1, true);
//        context1.setOut(out1);
//        InputStream input1 = getClass().getClassLoader().getResourceAsStream("Input_Client1_v1.txt");
//        context1.setBufferedReader(new BufferedReader(new InputStreamReader(input1)));
//        RiskGameClient client1 = new RiskGameClient(context1,randomPortNumber);
//        client1.start();
//        TimeUnit.MILLISECONDS.sleep(100);
//        client1.stop();
//        //context1.getSocket().close();
//
//        TimeUnit.MILLISECONDS.sleep(100);
//
//        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
//        ClientContext context2 = new ClientContext();
//        PrintStream out2 = new PrintStream(bytes2, true);
//        context2.setOut(out2);
//        InputStream input2 = getClass().getClassLoader().getResourceAsStream("Input_Client2_v1.txt");
//        context2.setBufferedReader(new BufferedReader(new InputStreamReader(input2)));
//        RiskGameClient client2 = new RiskGameClient(context2,randomPortNumber);
//        client2.start();
//
//        TimeUnit.MILLISECONDS.sleep(100);
//        //context2.getSocket().close();
//
//
//        ByteArrayOutputStream bytes4 = new ByteArrayOutputStream();
//        ClientContext context4 = new ClientContext();
//        InputStream input4 = getClass().getClassLoader().getResourceAsStream("Input_Client4_v1.txt");
//        PrintStream out4 = new PrintStream(bytes4, true);
//        context4.setBufferedReader(new BufferedReader(new InputStreamReader(input4)));
//        context4.setOut(out4);
//        RiskGameClient client4 = new RiskGameClient(context4,randomPortNumber);
//        client4.start();
//
//
//        TimeUnit.MILLISECONDS.sleep(100);
//
//        ByteArrayOutputStream bytes3 = new ByteArrayOutputStream();
//        ClientContext context3 = new ClientContext();
//        InputStream input3 = getClass().getClassLoader().getResourceAsStream("Input_Client3_v1.txt");
//        PrintStream out3 = new PrintStream(bytes3, true);
//        context3.setBufferedReader(new BufferedReader(new InputStreamReader(input3)));
//        context3.setOut(out3);
//        RiskGameClient client3 = new RiskGameClient(context3,randomPortNumber);
//        client3.start();
//
//
//
//
//
//
//
//
//
//        client2.join();
//        client3.join();
//        client4.join();
//
//        String expected1 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client1_v1.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected1, bytes1.toString());
//
//        String expected2 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client2_v1.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected2, bytes2.toString());
//
//        String expected3 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client3_v1.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected3, bytes3.toString());
//
//        String expected4 = new String(getClass().getClassLoader().getResourceAsStream("Output_Client4_v1.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected4, bytes4.toString());
//
//
//
//
//        ByteArrayOutputStream bytes5 = new ByteArrayOutputStream();
//        ClientContext context5 = new ClientContext();
//        PrintStream out5 = new PrintStream(bytes5, true);
//        context5.setOut(out5);
//        InputStream input5 = getClass().getClassLoader().getResourceAsStream("Input_MultiClient1.txt");
//        context5.setBufferedReader(new BufferedReader(new InputStreamReader(input5)));
//        RiskGameClient client5 = new RiskGameClient(context5,randomPortNumber);
//        client5.start();
//        TimeUnit.MILLISECONDS.sleep(200);
//
//        //context2.getSocket().close();
//
//
//        ByteArrayOutputStream bytes6 = new ByteArrayOutputStream();
//        ClientContext context6 = new ClientContext();
//        InputStream input6 = getClass().getClassLoader().getResourceAsStream("Input_MultiClient2.txt");
//        PrintStream out6 = new PrintStream(bytes6, true);
//        context6.setBufferedReader(new BufferedReader(new InputStreamReader(input6)));
//        context6.setOut(out6);
//        RiskGameClient client6 = new RiskGameClient(context6,randomPortNumber);
//        client6.start();
//
//
//        TimeUnit.MILLISECONDS.sleep(100);
//
//        ByteArrayOutputStream bytes7 = new ByteArrayOutputStream();
//        ClientContext context7 = new ClientContext();
//        InputStream input7 = getClass().getClassLoader().getResourceAsStream("Input_MultiClient3.txt");
//        PrintStream out7 = new PrintStream(bytes7, true);
//        context7.setBufferedReader(new BufferedReader(new InputStreamReader(input7)));
//        context7.setOut(out7);
//        RiskGameClient client7 = new RiskGameClient(context7,randomPortNumber);
//        client7.start();
//
//        TimeUnit.MILLISECONDS.sleep(100);
//        client5.join();
//        client6.join();
//        client7.join();
//        String expected5 = new String(getClass().getClassLoader().getResourceAsStream("Output_MultiClient1.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected5, bytes5.toString());
//
//
//        String expected6 = new String(getClass().getClassLoader().getResourceAsStream("Output_MultiClient2.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected6, bytes6.toString());
//
//        String expected7 = new String(getClass().getClassLoader().getResourceAsStream("Output_MultiClient3.txt").readAllBytes());
//        assertEqualsIgnoreLineSeparator(expected7, bytes7.toString());
//
//    }

    @Test
    public void testMain() throws IOException {
        System.out.println("main");
        String[] args = null;
        final InputStream original = System.in;
        App.main(args);
        System.setIn(original);
    }
}
