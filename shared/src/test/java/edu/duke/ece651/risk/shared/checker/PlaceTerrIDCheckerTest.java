package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlaceTerrIDCheckerTest {
    @Test
    public void test_checkMyRule(){
        Territory territory1 = new BasicTerritory("test1");
        Territory territory2 = new BasicTerritory("test2");
        Territory territory3 = new BasicTerritory("test3");

        territory1.tryChangeOwnerTo(0L);
        territory2.tryChangeOwnerTo(0L);
        territory3.tryChangeOwnerTo(0L);

        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);
        territories.add(territory3);

        RISKMap riskmap = new RISKMap(territories);

        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 10);
        placeOrders.put("test2", 20);

        PlaceRuleChecker checker = new PlaceTerrIDChecker(null);

        // success
        assertDoesNotThrow(()->checker.checkMyRule(riskmap, placeOrders,0L));

        // error: placing in other player's territory
        territory2.tryChangeOwnerTo(1L);
        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskmap, placeOrders,0L));
    }
}
