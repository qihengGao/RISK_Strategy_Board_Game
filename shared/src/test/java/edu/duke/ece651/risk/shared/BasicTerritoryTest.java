package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BasicTerritoryTest {
  @Test
  public void test_newTerritory() {
    Territory t1 = new BasicTerritory("FitzPatrick");
    assertEquals(t1.getName(), "FitzPatrick");
    assertFalse(t1.getNeighbors().iterator().hasNext());
    assertEquals(t1.getOwnerID(), -1);
    assertTrue(t1.tryChangeOwnerTo(1));
    assertEquals(t1.getOwnerID(), 1);
  }
  
  @Test
  public void test_addNeighbor() {
    Territory t1 = new BasicTerritory("Test1");
    Territory t_invalid = new BasicTerritory("Test1");//illegal
    Territory t2 = new BasicTerritory("Test2");//legal
    assertFalse(t1.tryAddNeighbor(t_invalid));
    assertTrue(t1.tryAddNeighbor(t2));
    assertSame(t1.getNeighborByName("Test2"), t2);
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
}
