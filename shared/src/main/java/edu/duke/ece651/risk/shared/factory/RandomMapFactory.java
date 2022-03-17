package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.BasicTerritory;
import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.Territory;

import java.util.HashSet;
import java.util.Random;

public class RandomMapFactory implements AbstractMapFactory {
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

    Random generator = new Random(123);
    HashSet<String> visited = new HashSet<String>();
    String curr = "Test0";
    while (visited.size() < n*3) {
      visited.add(curr);
      String toConnect = "Test" + generator.nextInt(n*3);
      while (map.getTerritoryByName(curr).getNumOfNeighbors()<2){
        toConnect = "Test" + generator.nextInt(n*3);
        // TODO: change to tryConnect
        map.connectTerr(curr, toConnect);
      }
      curr = toConnect;
    }
    
    return map;
  }

}
