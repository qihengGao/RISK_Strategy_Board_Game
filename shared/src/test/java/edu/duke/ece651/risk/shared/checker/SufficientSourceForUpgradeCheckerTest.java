package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.Owner;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.UpgradeUnitOrder;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SufficientSourceForUpgradeCheckerTest {

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
        ActionChecker checker = new SufficientSourceForUpgradeChecker(null);
        RISKMap riskMap = this.buildTestMap();
        UpgradeUnitOrder order = new UpgradeUnitOrder(0L, "Test1", "Soldier", 10, 1);
        System.out.println(order.getToLevel());
        System.out.println(order.getToLevel());

//        String expectedMessage = "";
//        String actualMessage = checker.checkMove(riskMap, order);
//        assertEquals("", actualMessage);
    }
}
