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

  
}
