package edu.duke.ece651.risk.shared;

import java.util.ArrayList;
import java.util.TreeMap;

import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.AbstractUnitFactory;
import edu.duke.ece651.risk.shared.factory.TestMapFactory;
import edu.duke.ece651.risk.shared.factory.V1UnitFactory;
import edu.duke.ece651.risk.shared.map.GameMap;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.Test;

public class MapTextViewTest {
  @Test
  public void test_viewMap() {
    AbstractMapFactory tmf = new TestMapFactory();
    GameMap map = tmf.createMapForNplayers(2);
    MapTextView mtv = new MapTextView(map);

    System.out.println(mtv.displayMapInit());

  }

  @Test
  public void test_displayAll() {
    AbstractMapFactory tmf = new TestMapFactory();
    AbstractUnitFactory uf = new V1UnitFactory();
    ArrayList<Unit> units = new ArrayList<>();
    GameMap map = tmf.createMapForNplayers(2);
    for (int i = 0; i < map.getNumOfContinents(); ++i) {
      Unit unit = uf.createNSoldiers(i);
      units.add(unit);
    }
    TreeMap<Long, Color> idToColor = new TreeMap<>();
    Color c1 = new Color("Green");
    Color c2 = new Color("Red");
    idToColor.put((long)0, c1);
    idToColor.put((long)1, c2);
    int count = 0;
    for (Territory territory : map.getContinent()) {
      territory.tryChangeOwnerTo((long) (count % 2));
      territory.tryAddUnit(units.get(count));
      count++;
    }
    MapTextView mtv = new MapTextView(map, idToColor);
    System.out.println(mtv.displayMap());
  }
}
