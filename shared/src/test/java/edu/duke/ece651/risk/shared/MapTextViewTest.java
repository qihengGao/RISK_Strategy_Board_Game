package edu.duke.ece651.risk.shared;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class MapTextViewTest {
  @Test
  public void test_viewMap() {
    RISKMap map = new RISKMap(new TreeMap<String, Territory>());
    MapTextView mtv = new MapTextView(map);
    Territory t1 = new Territory("FitzPatrick");
    Territory t2 = new Territory("Valhalla");
    Territory t3 = new Territory("Zone1");
    Territory t4 = new Territory("Zone2");
    map.tryAddTerritory(t1);
    map.tryAddTerritory(t2);
    map.tryAddTerritory(t3);
    map.tryAddTerritory(t4);
    
    t1.tryAddNeighbor(t2);
    t1.tryAddNeighbor(t3);
    t1.tryAddNeighbor(t4);
    
    System.out.println(mtv.displayMapInit());
    
  }

}
