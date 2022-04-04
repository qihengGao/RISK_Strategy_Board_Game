package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PlaceTerrExistCheckerTest {
    @Test
    public void test_checkMyRule(){
        Territory territory1 = new BasicTerritory("test1");
        Territory territory2 = new BasicTerritory("test2");

        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);

        Map<String, Integer> order = new HashMap<>();
        order.put("test1", 10);
        order.put("test2", 20);

        RISKMap riskMap = new RISKMap(territories);
        PlaceRuleChecker checker = new PlaceTerrExistChecker(null);

        // success
        assertDoesNotThrow(()->checker.checkMyRule(riskMap, order, 0L));

        // error: place on non-existing territory
        order.put("someRandomTerritory", 10);
        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskMap, order, 0L));
    }
}
