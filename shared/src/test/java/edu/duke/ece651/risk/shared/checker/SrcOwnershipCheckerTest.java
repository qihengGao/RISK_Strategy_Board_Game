package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

public class SrcOwnershipCheckerTest {
    @Test
    public void test_checkMyRule(){
        Territory territory1 = new BasicTerritory("Test1");
        territory1.tryChangeOwnerTo(0L);
        Territory territory2 = new BasicTerritory("Test2");
        territory2.tryChangeOwnerTo(1L);

        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);

        RISKMap riskMap = new RISKMap(territories);
        ActionChecker checker = new SrcOwnershipChecker(null);

        // success
        MoveOrder moveOrder = new MoveOrder(0L, "Test1", "Test2", "Soldier", 10);
        assertNull(checker.checkMyRule(riskMap, moveOrder));

        // error: place from a foreign territory
        MoveOrder moveOrder2 = new MoveOrder(0L, "Test2", "Test1", "Soldier", 10);
        assertEquals("You must place orders from your own territories or from your alliance territories!",
                checker.checkMyRule(riskMap, moveOrder2));
    }
}
