package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

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
