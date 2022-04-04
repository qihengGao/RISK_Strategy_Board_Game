package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

public class TerrExistCheckerTest {
    @Test
    public void test_checkMyRule(){
        Territory territory1 = new BasicTerritory("Test1");
        Territory territory2 = new BasicTerritory("Test2");
        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);

        RISKMap riskMap = new RISKMap(territories);

        // success
        MoveOrder moveOrder = new MoveOrder(0L, "Test1", "Test2", "Soldier", 10);
        ActionChecker checker = new TerrExistChecker(null);
        assertNull(checker.checkMyRule(riskMap, moveOrder));

        // error: src does not exist
        MoveOrder moveOrder2 = new MoveOrder(0L, "TestXX", "Test2", "Soldier", 10);
        assertEquals("Your Source territory does not exist!", checker.checkMyRule(riskMap, moveOrder2));

        // error: dest does not exit
        MoveOrder moveOrder3 = new MoveOrder(0L, "Test1", "TestXX", "Soldier", 10);
        assertEquals("Your Destination territory does not exist!", checker.checkMyRule(riskMap, moveOrder3));
    }
}
