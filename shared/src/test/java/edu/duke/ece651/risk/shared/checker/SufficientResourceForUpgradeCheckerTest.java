package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.UpgradeUnitOrder;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SufficientResourceForUpgradeCheckerTest {

    private RISKMap buildTestMap(){
        AbstractMapFactory tmf = new RandomMapFactory();
        RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
        int count = 0;
        for (Territory t: riskMap.getContinent()){
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
    public void test_checkMyRule(){
        ActionChecker checker = new SufficientResourceForUpgradeChecker(null);
        RISKMap riskMap = this.buildTestMap();

        // error: upgrade to a lower level
        UpgradeUnitOrder orderLevelLower = new UpgradeUnitOrder(0L, "Test1", "Soldier", 10, 0);
        String expectedMessage = "Soldier level 0 cannot upgrade to 0";
        String actualMessage = checker.checkMove(riskMap, orderLevelLower);
        assertEquals(expectedMessage, actualMessage);

        // error: upgrade to a lower level
        UpgradeUnitOrder orderHighLevel = new UpgradeUnitOrder(0L, "Test1", "Soldier", 10, 7);
        expectedMessage = "Soldier level 0 cannot upgrade to 7";
        actualMessage = checker.checkMove(riskMap, orderHighLevel);
        assertEquals(expectedMessage, actualMessage);

        // error: insufficient resource
        UpgradeUnitOrder orderInsufficientResource = new UpgradeUnitOrder(0L, "Test1", "Soldier", 10, 2);
        expectedMessage = "Insufficient resource to upgrade -1's 10 lv.0 Soldier";
        actualMessage = checker.checkMove(riskMap, orderInsufficientResource);
        assertEquals(expectedMessage, actualMessage);

        // success
        UpgradeUnitOrder orderSuccess = new UpgradeUnitOrder(0L, "Test1", "Soldier", 9, 2);
        actualMessage = checker.checkMove(riskMap, orderSuccess);
        assertNull(actualMessage);
    }
}
