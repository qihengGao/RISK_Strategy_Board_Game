package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.GameMap;
import edu.duke.ece651.risk.shared.MapTextView;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import org.junit.jupiter.api.Test;

public class RandomMapFactoryTest {
  @Test
  public void test_randomMF() {
    AbstractMapFactory tmf = new RandomMapFactory();
    GameMap map = tmf.createMapForNplayers(5);
    MapTextView mtv = new MapTextView(map);
    
    System.out.println(mtv.displayMapInit());
    
  }

}
