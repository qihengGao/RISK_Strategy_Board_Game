package edu.duke.ece651.risk.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class TestMapFactory implements AbstractMapFactory {

  /**
   * create random test map
   */
  @Override
  public Map createMapForNplayers(int n) {
    Map map = new RISKMap(buildRandomContinent(n));
    return map;
  }

  private HashSet<Territory> buildRandomContinent(int n) {
    ArrayList<Territory> randCont = new ArrayList<Territory>();
    for (int i = 0; i<n*3; i++){
      String terrName = "Test" + i;
      randCont.add(new BasicTerritory(terrName));
    }

    randomConnect(randCont);
    return new HashSet<Territory>(randCont);
  }

  private void randomConnect(ArrayList<Territory> randCont) {
    Random generator = new Random(123);
    for (Territory t : randCont){
      for (int i = 0; i < 3; i++){
        int idx = generator.nextInt(randCont.size());
        t.tryAddNeighbor(randCont.get(idx));
      }
    }
  }

}
