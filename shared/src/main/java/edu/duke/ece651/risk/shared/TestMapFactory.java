package edu.duke.ece651.risk.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class TestMapFactory implements AbstractMapFactory {

  /**
   * create random test map
   */
  @Override
  public GameMap createMapForNplayers(int n) {
    GameMap map = new RISKMap(new HashSet<Territory>());
    for (int i = 0; i<n*3; i++){
      String terrName = "Test" + i;
      map.tryAddTerritory(new BasicTerritory(terrName));
    }

    map.connectTerr("Test0", "Test1");
    map.connectTerr("Test0", "Test3");
    map.connectTerr("Test0", "Test4");
    map.connectTerr("Test1", "Test3");
    map.connectTerr("Test2", "Test1");
    map.connectTerr("Test2", "Test3");
    map.connectTerr("Test2", "Test5");
    map.connectTerr("Test3", "Test5");
    map.connectTerr("Test4", "Test3");
    map.connectTerr("Test4", "Test5");
    
    return map;
  }
}
