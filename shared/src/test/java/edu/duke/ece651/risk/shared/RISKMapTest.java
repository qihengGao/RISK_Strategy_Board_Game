package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class RISKMapTest {
  @Test
  public void test_simpleMap() {
    RISKMap map = new RISKMap(new HashSet<Territory>());
    assertEquals(0, map.getNumOfContinents());
    Territory t1 = new BasicTerritory("FitzPatrick");
    assertTrue(map.tryAddTerritory(t1));
    assertEquals(1, map.getNumOfContinents());
    assertSame(map.getTerritoryByName("FitzPatrick"), t1);
    assertNull(map.getTerritoryByName("Valhalla"));

    Territory t2 = new BasicTerritory("Valhalla");
    assertTrue(map.tryAddTerritory(t2));
    assertEquals(2, map.getNumOfContinents());
    assertTrue(map.tryAddTerritory(t2));//overwrite terr

    HashSet<Territory> group0 = new HashSet<Territory>();
    map.getTerritoriesByOwnerID(-1).forEach(group0::add);
    
    assertTrue(group0.contains(t1));
    assertTrue(group0.contains(t2));
    
    t2.tryChangeOwnerTo(1L);

    HashSet<Territory> group1 = new HashSet<Territory>();
    map.getTerritoriesByOwnerID(1).forEach(group1::add);
    
    assertTrue(group1.contains(t2));
    assertFalse(group1.contains(t1));
  }

  @Test
  public void test_checkPath(){
    AbstractMapFactory amf = new RandomMapFactory();
    GameMap map = amf.createMapForNplayers(2);
    
    assertNull(map.getPath("Test0", "Test0"));//assert Path from myself to myself is null
    assertNull(map.getPath("Test0", "Test6"));//assert dst not exist path is null
    assertNull(map.getPath("TestNE", "Test6"));
    for (int i = 0; i < 6; i++){
      for (int j = 0; j < 6; j++){
        if (i!=j){
          assertNotEquals(map.getPath("Test"+i, "Test"+j), null);
        }
      }
    }
  }
  
}
