package edu.duke.ece651.risk.server;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class MapTextViewTest {
  @Test
  public void test_viewMap() {
    RISKMap map = new RISKMap(new TreeMap<String, Territory>());
    MapTextView mtv = new MapTextView(map);
    Territory t1 = new Territory("FitzPatrick");
    Territory t2 = new Territory("Valhalla");
    map.tryAddTerritory(t1);
    map.tryAddTerritory(t2);
    t1.tryAddNeighbor(t2);
    
    System.out.println(mtv.displayMapInit());
    
  }


}
