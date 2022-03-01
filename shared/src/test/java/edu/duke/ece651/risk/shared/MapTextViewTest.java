package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

public class MapTextViewTest {
  @Test
  public void test_viewMap() {
    TestMapFactory tmf = new TestMapFactory();
    Map map = tmf.createMapForNplayers(2);
    MapTextView mtv = new MapTextView(map);
    
    System.out.println(mtv.displayMapInit());
    
  }

}
