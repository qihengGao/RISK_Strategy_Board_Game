package edu.duke.ece651.risk.shared.factory;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.Test;

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
    assertTrue(t1.tryAddNeighbor(t2.getName()));
    assertSame(t1.getNeighborByName("Test2"), "Test2");
    assertNull(t1.getNeighborByName("Atlantis"));
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
    Unit u2 = new BasicUnit("a", 5, 3);
    t.tryAddUnit(u1);
    assertTrue(t.tryAddUnit(u2));
//    for (Unit u : t.getUnits()) {
//      System.out.println(u);
//    }
    assertFalse(u1.equals(u2));
    assertNotEquals(u1.hashCode(), u2.hashCode());
    assertNull(t.getUnitByTypeLevel("b", 0));
    assertNull(t.getUnitByTypeLevel("a", 1));
    assertNotNull(t.getUnitByTypeLevel("a", 3));
    assertEquals(10, t.getUnitByTypeLevel("a", 0).getAmount());
  }
}
