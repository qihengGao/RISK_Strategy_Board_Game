package edu.duke.ece651.risk.shared.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;

public class UnitPlaceStateTest {

    private void run_test_routine(String placementCommand, String expected) throws ClassNotFoundException, IOException{
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo, true));
        this.helper_doAction(placementCommand);
        System.setOut(System.out);
        this.assertEqualsIgnoreLineSeparator(expected, bo.toString());      
    }

    private void assertEqualsIgnoreLineSeparator(String expected, String actual) {
        assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
                actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
    }

    private void helper_doAction(String placementCommand) throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        PrintStream printStream = System.out;
        State currstate = mock(UnitPlaceState.class);
        State nextState = mock(ShowGameResultState.class);
        Color color = new Color("red");
        GameMap riskGameMap = mock(RISKMap.class);

        long ownerId = 0L;
        Territory territory1 = new BasicTerritory("territory1");
        Territory territory2 = new BasicTerritory("territory2");
        Territory territory3 = new BasicTerritory("territory3");

        ArrayList<Territory> territories = new ArrayList<>();
        territory1.tryChangeOwnerTo(ownerId);
        territories.add(territory1);

        territory2.tryChangeOwnerTo(ownerId);
        territories.add(territory2);

        territory3.tryChangeOwnerTo(ownerId);
        territories.add(territory3);

        doCallRealMethod().when(currstate).doAction(clientContext);
        doReturn(printStream).when(clientContext).getOut();
        doReturn(color).when(clientContext).getClientColor();
        doReturn(ownerId).when(clientContext).getPlayerID();
        doReturn(riskGameMap).when(clientContext).getRiskMap();
        doReturn(ownerId).when(clientContext).getPlayerID();
        doReturn(territories).when(riskGameMap).getTerritoriesByOwnerID(ownerId);

        InputStream inputStream = new ByteArrayInputStream(placementCommand.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());

        doReturn(bufferedReader).when(clientContext).getBufferedReader();
        doReturn(objectOutputStream).when(clientContext).getOos();
        doReturn(nextState).when(clientContext).getGameState();

        currstate.doAction(clientContext);
    }

    @Test
    public void test_doAction_normal() throws ClassNotFoundException, IOException{
        String expected = "\nYou are: red\n" + 
            "You have 30 to put\n" + 
            "How many Soldiers you want to put in territory1\n" + 
            "You have 20 to put\n" + 
            "How many Soldiers you want to put in territory2\n" + 
            "You have 10 to put\n" + 
            "How many Soldiers you want to put in territory3\n";

        this.run_test_routine("10\n10\n10\n", expected); 
    }

    @Test
    public void test_doAction_noEnoughUnitsForAllTerritories() throws ClassNotFoundException, IOException {
        String expected = "\nYou are: red\n" + 
            "You have 30 to put\n" + 
            "How many Soldiers you want to put in territory1\n" + 
            "You have 20 to put\n" + 
            "How many Soldiers you want to put in territory2\n" + 
            "You have no more available units to put in territory territory3, adding 0 units\n";

        this.run_test_routine("10\n20\n10\n", expected);
    }

    @Test
    public void test_doAction_putMoreUnitThanRemained() throws ClassNotFoundException, IOException{
        String expected = "\nYou are: red\n" +
        "You have 30 to put\n"  +
        "How many Soldiers you want to put in territory1\n" +
        "You have 20 to put\n" +
        "How many Soldiers you want to put in territory2\n" +
        "You have 10 to put\n" +
        "How many Soldiers you want to put in territory3\n" +
        "Amount to put must not exceed 10\n" +
        "You have 10 to put\n" +
        "How many Soldiers you want to put in territory3\n" +
                "Your input unit number is negative, please try again!\n" +
        "You have 10 to put\n" +
                "How many Soldiers you want to put in territory3\n";
        
        this.run_test_routine("10\n10\n20\n-10\n10\n", expected);
    }

    @Test
    public void test_doAction_invalidInput() throws ClassNotFoundException, IOException{
        String expected = "\nYou are: red\n" +
        "You have 30 to put\n" +
        "How many Soldiers you want to put in territory1\n" +
        "You have 20 to put\n" +
        "How many Soldiers you want to put in territory2\n" +
        "You have 10 to put\n" +
        "How many Soldiers you want to put in territory3\n" +
        "x is an Invalid input! Try again!\n" +
        "You have 10 to put\n" +
        "How many Soldiers you want to put in territory3\n";

        this.run_test_routine("10\n10\nx\n10\n", expected);
    }

    @Test
    public void test_doAction_placeFewEachTime() throws ClassNotFoundException, IOException{
        String expected = "\nYou are: red\n" +
        "You have 30 to put\n" +
        "How many Soldiers you want to put in territory1\n" +
        "You have 20 to put\n" +
        "How many Soldiers you want to put in territory2\n" +
        "You have 10 to put\n" +
        "How many Soldiers you want to put in territory3\n" +
        "You have 5 to put\n" +
        "How many Soldiers you want to put in territory1\n" +
        "You have no more available units to put in territory territory2, adding 0 units\n" +
        "You have no more available units to put in territory territory3, adding 0 units\n";

        this.run_test_routine("10\n10\n5\n5\n", expected);
    }
}