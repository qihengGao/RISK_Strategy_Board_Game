package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeUnitOrderTest {
    @Test
    public void test_negative() {
        Territory t1 = new BasicTerritory("Test1");
        HashSet<Territory> terrs = new HashSet<Territory>();
        terrs.add(t1);
        RISKMap map = new RISKMap(terrs);
        UpgradeUnitOrder order = new UpgradeUnitOrder(0, "Test1", "Soldier", -1, 1);
        assertEquals("cannot upgrade negative amount of units", order.executeOrder(map));
        UpgradeUnitOrder order2 = new UpgradeUnitOrder(0, "Test1", "Soldier",1, 1);
        assertEquals("You must place orders from your own territories!", order2.executeOrder(map));
        t1.tryChangeOwnerTo(0L);
        Owner o = new Owner(0);
        map.tryAddOwner(o);
        UpgradeUnitOrder o3 = new UpgradeUnitOrder(1, "Test1", "Soldier",1, 1);
        assertEquals("You must place orders from your own territories!", o3.executeOrder(map));
        Unit u = new BasicUnit("Soldier", 10);
        u.setOwnerId(0L);
        t1.tryAddUnit(u);
        UpgradeUnitOrder o4 = new UpgradeUnitOrder(0, "Test1", "Soldier",1, 2);
        assertEquals("Insufficient resource to upgrade 1 lv.0 Soldier", o4.executeOrder(map));
        o.tryAddOrRemoveTechResource(100);
        assertEquals("cannot upgrade units beyond maximum technology level", o4.executeOrder(map));
        o.tryUpgradeTechLevel();
        UpgradeUnitOrder o5 = new UpgradeUnitOrder(0, "Test1", "Soldier level 1", 1, 2);
        assertEquals("Current territory does not have the level 1 Soldier", o5.executeOrder(map));
        UpgradeUnitOrder o6 = new UpgradeUnitOrder(0, "Test1", "Soldier level 0", 1, 0);
        assertEquals("Soldier level 0 cannot upgrade to 0", o6.executeOrder(map));
        UpgradeUnitOrder o7 = new UpgradeUnitOrder(0, "Test1", "Soldier level 0", 11, 1);
        assertEquals("Unit amount is not sufficient to do the upgrade", o7.executeOrder(map));
        UpgradeUnitOrder o8 = new UpgradeUnitOrder(0, "Test1", "Soldier level 0", 10, 1);
        assertEquals(1, o8.getToLevel());
        assertEquals(0, o8.getPlayerID());
        assertEquals("Test1", o8.getSrcTerritory());
        o8.setToLevel(1);
        assertEquals(1, o8.getToLevel());
        assertNull(o8.executeOrder(map));
        assertEquals(0, t1.getUnitByType("Soldier level 0").getAmount());
        assertEquals(10, t1.getUnitByType("Soldier level 1").getAmount());

        UpgradeUnitOrder order_ = new UpgradeUnitOrder();
        order_.setOrderType("Upgrade Unit");
        assertEquals("Upgrade Unit", order_.getOrderType());
        order_.setSrcTerritory("Test1");
        assertEquals("Test1", order_.getSrcTerritory());
        order_.setDestTerritory("Test1");
        assertEquals("Test1", order_.getDestTerritory());
        order_.setUnitType("Soldier level 1");
        assertEquals("Soldier level 1", order_.getUnitType());
        order_.setUnitAmount(10);
        assertEquals(10, order_.getUnitAmount());
        order_.setPlayerID(1L);
        assertEquals(1L, order_.getPlayerID());
    }
}