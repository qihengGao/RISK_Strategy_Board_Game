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


    private void assertEqualsIgnoreLineSeparator(String expected, String actual) {
        assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
                actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
    }

    private void helper_doAction(String placementCommand) throws ClassNotFoundException, IOException{
        ClientContext clientContext = mock(ClientContext.class);
        // PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        PrintStream printStream = System.out;
        State currstate = mock(UnitPlaceState.class);
        State nextState = mock(ShowGameResultState.class);
        Color color = new Color("red");
        GameMap riskGameMap = mock(RISKMap.class);

        long ownerId = 0L;
        Territory territory1 = new BasicTerritory("territory1");
        territory1.tryChangeOwnerTo(ownerId);
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(territory1);

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
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo, true));

        this.helper_doAction("10\n10\n10\n");

        System.setOut(System.out);

        String expected = "You are: red\n" + 
            "You have 30 to put\n" + 
            "How many Soldiers you want to put in territory1\n" + 
            "You have 20 to put\n" + 
            "How many Soldiers you want to put in territory1\n" + 
            "You have 10 to put\n" + 
            "How many Soldiers you want to put in territory1\n";

        this.assertEqualsIgnoreLineSeparator(expected, bo.toString());      
    }

    @Test
    public void test_doAction_noEnoughUnitsForAllTerritories() throws ClassNotFoundException, IOException {
        // PrintStream printStream = this.helper_doAction("20\n10\n");
    }
}
