package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class FormAllianceOrderTest {
    private RISKMap buildTestMap() {
        AbstractMapFactory tmf = new RandomMapFactory();
        RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
        int count = 0;
        for (Territory t : riskMap.getContinent()) {
            t.tryAddUnit(new BasicUnit("Unit", 10));
            t.tryChangeOwnerTo((long) (count / 3));
            count++;
        }
        riskMap.getOwners().put(0L, new Owner(0, 6, 100, 100));
        riskMap.getOwners().put(1L, new Owner(0, 6, 100, 100));
        riskMap.getOwners().put(2L, new Owner(0, 6, 100, 100));
        return riskMap;
    }


    @Test
    void executeOrder() {
        RISKMap map = buildTestMap();
        Order allianceOrder = new FormAllianceOrder(0L,1L);
        assertNull(allianceOrder.executeOrder(map));
        assertTrue(map.getOwners().get(0L).getAlliance().contains(1L));
        assertFalse(map.getOwners().get(1L).getAlliance().contains(0L));

        allianceOrder.setAllianceID(0L);
        assertEquals(allianceOrder.getAllianceID(), 0L);
        assertEquals(allianceOrder.executeOrder(map), "Invalid target alliance!");

        allianceOrder.setAllianceID(1L);
        assertEquals(allianceOrder.executeOrder(map), "Invalid target alliance!");
    }
}