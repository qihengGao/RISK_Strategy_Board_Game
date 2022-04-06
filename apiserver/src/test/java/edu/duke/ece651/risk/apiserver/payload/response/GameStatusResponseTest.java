package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStatusResponseTest {

    @Test
    public void test_setGet(){
        Territory territory = new BasicTerritory("test1");
        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory);
        RISKMap riskMap = new RISKMap(territories);
        Color color = new Color("red");
        TreeMap<Long, Color> idToColor = new TreeMap<>();
        idToColor.put(0L, color);

        GameStatusResponse  response = new GameStatusResponse();
        response.setState("state");
        assertEquals("state", response.getState());

        response.setRiskMap(riskMap);
        assertEquals(riskMap, response.getRiskMap());

        response.setWinner(10L);
        assertEquals(10L, response.getWinner());

        response.setIdToColor(idToColor);
        assertEquals(idToColor, response.getIdToColor());

        Set<Long> lostPlayers = new HashSet<>();
        lostPlayers.add(15L);
        lostPlayers.add(10L);
        response.setLostPlayers(lostPlayers);
        assertEquals(lostPlayers, response.getLostPlayers());
    }
}
