package edu.duke.ece651.risk.shared.territory;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.risk.shared.factory.AbstractUnitFactory;
import edu.duke.ece651.risk.shared.factory.V1UnitFactory;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;
import edu.duke.ece651.risk.shared.unit.UnitComparator;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

public class BasicTerritoryTest {
    @Test
    public void test_newTerritory() {
        Territory t1 = new BasicTerritory("FitzPatrick");
        assertEquals(t1.getName(), "FitzPatrick");
        assertFalse(t1.getNeighbors().iterator().hasNext());
        assertEquals(t1.getOwnerID(), -1);
        assertTrue(t1.tryChangeOwnerTo(1L));
        assertEquals(t1.getOwnerID(), 1);

        Territory t = new BasicTerritory("t", 9);
        assertEquals("t", t.getName());
        assertEquals(9, t.getSize());
        assertEquals(10,t.getFoodResource());
        assertEquals(20, t.getTechResource());

        Territory t_ = new BasicTerritory("t", 9, 10, 20);
        assertEquals(10,t_.getFoodResource());
        assertEquals(20, t_.getTechResource());
    }

    @Test
    public void test_addNeighbor() {
        Territory t1 = new BasicTerritory("Test1");
        Territory t_invalid = new BasicTerritory("Test1");//illegal
        Territory t2 = new BasicTerritory("Test2");//legal
        assertFalse(t1.tryAddNeighbor(t_invalid.getName()));
        assertEquals(0, t1.getNumOfNeighbors());
        assertTrue(t1.tryAddNeighbor(t2.getName()));
        assertEquals(1, t1.getNumOfNeighbors());
        assertSame(t1.getNeighborByName("Test2"), "Test2");
        assertNull(t1.getNeighborByName("Atlantis"));
        assertNotEquals(t1.hashCode(), t2.hashCode());
        assertNotNull(t1.getBattleField());
        assertFalse(t1.equals(t2));
    }

    @Test
    public void test_units() {
        Unit u1 = new BasicUnit("Soldier", 100);
        Unit u2 = new BasicUnit("Dragon", 5);
        Territory t1 = new BasicTerritory("t1");
        assertTrue(t1.tryAddUnit(u1));
        assertTrue(t1.tryAddUnit(u2));
        int count = 0;
        for (Unit u : t1.getUnits()) {
            ++count;
        }
        assertEquals(2, count);
        Unit get1 = t1.getUnitByType("Soldier");
        assertTrue(u1.equals(get1));
        assertNull(t1.getUnitByType("soldier"));

        TreeSet<Unit> units = new TreeSet<>(new UnitComparator());
        units.add(new BasicUnit("Soldier level 0", 1));
        units.add(new BasicUnit("Soldier level 4", 5));
        t1.setUnits(units);
        assertEquals(4, t1.getUnitByType("Soldier level 4").getLevel());
        assertEquals(1, t1.getUnitByType("Soldier level 0").getAmount());
        assertNull(t1.getUnitByType("Soldier level 1"));
    }

    @Test
    public void test_equal() {
        AbstractUnitFactory uf = new V1UnitFactory();
        Unit u = uf.createNSoldiers(0);
        Territory t = new BasicTerritory("0");
        Territory t_ = new BasicTerritory("0");
        assertTrue(t.equals(t));
        assertFalse(t.equals(null));
        assertFalse(t.equals(u));
        assertFalse(u.equals(t));
        assertTrue(t.equals(t_));
    }

    @Test
    public void test_type_and_level() {
        Territory t = new BasicTerritory("t");
        Unit u1 = new BasicUnit("a", 10, 0);
        Unit u1_ = new BasicUnit("a", 1);
        Unit u2 = new BasicUnit("a", 5, 3);
        t.tryAddUnit(u1);
        t.tryAddUnit(u1_);
        assertEquals(11, t.getUnitByTypeLevel("a", 0).getAmount());
        assertEquals(11, t.getUnitByType("a level 0").getAmount());
        t.tryAddUnit(u2);
        assertFalse(u1.equals(u2));
        assertNotEquals(u1.hashCode(), u2.hashCode());
        assertNull(t.getUnitByTypeLevel("b", 0));
        assertNull(t.getUnitByTypeLevel("a", 1));
        assertNotNull(t.getUnitByTypeLevel("a", 3));
        assertEquals(11, t.getUnitByTypeLevel("a", 0).getAmount());
    }

    @Test
    void test_tryUpgradeUnitToLevel() {
        Territory t = new BasicTerritory("t", 10);
        Unit toU = new BasicUnit("a", 10, 2);
        assertFalse(t.tryUpgradeUnitToLevel(toU, 3));
        Unit inT = new BasicUnit("a", 9, 2);
        t.tryAddUnit(inT);
        assertFalse(t.tryUpgradeUnitToLevel(toU, 2));
        assertFalse(t.tryUpgradeUnitToLevel(toU, 1));
        assertFalse(t.tryUpgradeUnitToLevel(toU, 3));
        Unit add = new BasicUnit("a", 1, 2);
        t.tryAddUnit(add);
        assertTrue(t.tryUpgradeUnitToLevel(toU, 3));
        assertEquals(0, t.getUnitByTypeLevel("a", 2).getAmount());
        assertEquals(10, t.getUnitByTypeLevel("a", 3).getAmount());
    }
}