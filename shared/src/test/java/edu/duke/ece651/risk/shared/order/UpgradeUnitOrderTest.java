package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.map.TerritoryNameComparator;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeUnitOrderTest {
    @Test
    public void test_negative() {
        Territory t1 = new BasicTerritory("Test1");
        HashSet<Territory> terrs = new HashSet<Territory>();
        terrs.add(t1);
        RISKMap map = new RISKMap(terrs);
        UpgradeUnitOrder order = new UpgradeUnitOrder(0, "Test1", "Soldier", 10, -1);
        assertEquals("You must place orders from your own territories!", order.executeOrder(map));

    }
}