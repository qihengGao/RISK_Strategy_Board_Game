package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

public class RISKMapTest {
  @Test
  public void test_simpleMap() {
    RISKMap map = new RISKMap(new HashSet<Territory>());
    map.tryAddOwner(new Owner(1L));
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
  public void test_add_owner() {
    AbstractMapFactory amf = new RandomMapFactory();
    GameMap map = amf.createMapForNplayers(2);
    Owner o1 = new Owner(0);
    Owner o2 = new Owner(1);
    map.tryAddOwner(o1);
    map.tryAddOwner(o2);
    RISKMap r = (RISKMap) map;
    assertEquals(2, r.getOwners().size());
  }

  private RISKMap buildTestMap(){
    AbstractMapFactory tmf = new RandomMapFactory();
    RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
    Random r = new Random(199);
    int count = 0;
    for (Territory t: riskMap.getContinent()){
      Unit u = new BasicUnit("Unit", 10);
      u.setOwnerId((long) (count / 3));
      t.tryAddUnit(u);
      Unit u_gueset = new BasicUnit("Unit", 10);
      u_gueset.setOwnerId((long) ((count+1) % 3));
      t.tryAddUnit(u_gueset);

      t.increaseSize(r.nextInt(10));
      t.tryChangeOwnerTo((long) (count / 3));
      count++;
    }
    Owner o1 = new Owner(0, 6, 100, 100);
    o1.formAlliance(1L);
    riskMap.getOwners().put(0L, o1);
    Owner o2 = new Owner(1, 6, 100, 100);
    o2.formAlliance(0L);
    riskMap.getOwners().put(1L, o2);
    riskMap.getOwners().put(2L, new Owner(2, 6, 100, 100));

    return riskMap;
  }


  private void displayMap(RISKMap riskMap) {
    TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
    idToColor.put((long)0, new Color("Red"));
    idToColor.put((long)1, new Color("Green"));
    idToColor.put((long)2, new Color("Blue"));

    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    System.out.println(mapTextView.displayMap());
  }


  @Test
  public void test_breakAlliances(){
    RISKMap map = buildTestMap();
    displayMap(map);
    HashMap<Territory, Integer> distances = map.doDijkstra(map.getTerritoryByName("Test1"));

    Territory myClosestTerr = map.getMyClosestTerr(distances, 2L);
    assertEquals(myClosestTerr.getName(), "Test7");
    Territory myClosestTerr2 = map.getMyClosestTerr(distances, 1L);
    assertEquals(myClosestTerr2.getName(), "Test4");

    assertEquals(map.getTerritoryByName("Test0").getUnitByTypeAndID("Unit", 1L).getAmount(), 10);
    assertEquals(map.getTerritoryByName("Test5").getUnitByTypeAndID("Unit", 0L).getAmount(), 10);

    map.handleBreakAlliance(0L, 1L);
    System.out.println("After break:");
    displayMap(map);

    assertEquals(map.getTerritoryByName("Test0").getUnitByTypeAndID("Unit", 1L).getAmount(), 0);
    assertEquals(map.getTerritoryByName("Test5").getUnitByTypeAndID("Unit", 0L).getAmount(), 0);
    assertEquals(map.getTerritoryByName("Test0").getUnitByTypeAndID("Unit", 0L).getAmount(), 20);
    assertEquals(map.getTerritoryByName("Test5").getUnitByTypeAndID("Unit", 1L).getAmount(), 20);

  }
}
