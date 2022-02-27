package edu.duke.ece651.risk.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TerritoryTest {
  @Test
  public void test_newTerritory() {
    Territory t1 = new Territory("FitzPatrick");
    assertEquals(t1.getName(), "FitzPatrick");
    assertFalse(t1.getNeighbors().iterator().hasNext());
    assertEquals(t1.getOwnerID(), 0);
    assertTrue(t1.tryChangeOwnerTo(1));
    assertEquals(t1.getOwnerID(), 1);
  }

}
