package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class RISKMapTest {
  @Test
  public void test_simpleMap() {
    RISKMap map = new RISKMap(new TreeMap<String, Territory>());
    Territory t1 = new Territory("FitzPatrick");
    assertTrue(map.tryAddTerritory(t1));
    assertSame(map.getTerritoryByName("FitzPatrick"), t1);
    assertNull(map.getTerritoryByName("Valhalla"));

    Territory t2 = new Territory("Valhalla");
    assertTrue(map.tryAddTerritory(t2));

    HashSet<Territory> group0 = new HashSet<Territory>();
    map.getTerritoriesByOwnerID(0).forEach(group0::add);
    
    assertTrue(group0.contains(t1));
    assertTrue(group0.contains(t2));
    
    t2.tryChangeOwnerTo(1);

    HashSet<Territory> group1 = new HashSet<Territory>();
    map.getTerritoriesByOwnerID(0).forEach(group1::add);
    
    assertTrue(group1.contains(t1));
    assertFalse(group1.contains(t2));
  }

}