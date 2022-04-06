package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.HashSet;

public class TestMapFactory implements AbstractMapFactory {

  /**
   * create specified test map
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
