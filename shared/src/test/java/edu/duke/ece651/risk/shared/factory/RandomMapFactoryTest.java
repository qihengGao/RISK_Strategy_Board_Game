package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class RandomMapFactoryTest {
  @Test
  public void test_randomMF() {
    AbstractMapFactory tmf = new RandomMapFactory();
    GameMap map = tmf.createMapForNplayers(5);
    MapTextView mtv = new MapTextView(map);
    
    System.out.println(mtv.displayMapInit());
    assertInstanceOf(RISKMap.class, map);
  }

}
