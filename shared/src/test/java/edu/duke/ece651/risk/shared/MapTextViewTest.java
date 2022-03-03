package edu.duke.ece651.risk.shared;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class MapTextViewTest {
  @Test
  public void test_viewMap() {
    AbstractMapFactory tmf = new TestMapFactory();
    Map map = tmf.createMapForNplayers(2);
    MapTextView mtv = new MapTextView(map);

    System.out.println(mtv.displayMapInit());

  }

  @Test
  public void test_displayAll() {
    AbstractMapFactory tmf = new TestMapFactory();
    Map map = tmf.createMapForNplayers(2);
    TreeMap<Long, Color> idToColor = new TreeMap<>();
    Color c1 = new Color("Green");
    Color c2 = new Color("Red");
    idToColor.put((long)0, c1);
    idToColor.put((long)1, c2);
    int count = 0;
    for (Territory territory : map.getContinent()) {
      territory.tryChangeOwnerTo(count % 2);
      count++;
    }
    MapTextView mtv = new MapTextView(map, idToColor);
    System.out.println(mtv.displayMap());
  }
}
