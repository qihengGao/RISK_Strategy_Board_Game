package edu.duke.ece651.risk.shared.checker;

import java.util.TreeMap;

import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathExistMoveCheckerTest {
  @Test
  public void test_checkPathExist() {
    RISKMap riskMap = buildTestMap();
    ActionChecker pathChecker = new PathExistMoveChecker(null);
    
    //test path exist
    Order order = new MoveOrder(0, "Test0", "Test2", "Unit", 2);
    assertEquals(pathChecker.checkMove(riskMap, order), null);
    
    //test path doesn't exist
    Order order_invalid = new MoveOrder(0, "Test0", "Test1", "Unit", 2);
    assertEquals(pathChecker.checkMove(riskMap, order_invalid), "Move Order path does not exist in your territories!");
    
    Order order_not_owned = new MoveOrder(0, "Test0", "Test3", "Unit", 2);
    assertEquals(pathChecker.checkMove(riskMap, order_not_owned), "Move Order path does not exist in your territories!");

    Order order_to_self = new MoveOrder(0, "Test0", "Test0", "Unit", 2);
    assertEquals(pathChecker.checkMove(riskMap, order_to_self), "You cannot move within same territory!");

    displayMap(riskMap);

    //test long path (not adjacent)
    riskMap.getTerritoryByName("Test4").tryChangeOwnerTo(0L);
    riskMap.getTerritoryByName("Test5").tryChangeOwnerTo(0L);
    displayMap(riskMap);
    Order order_long = new MoveOrder(0, "Test0", "Test1", "Unit", 2);
    assertEquals(order_long.executeOrder(riskMap), null);
    displayMap(riskMap);
    
  }

  private RISKMap buildTestMap(){
    AbstractMapFactory tmf = new RandomMapFactory();
    RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
    int count = 0;
    for (Territory t: riskMap.getContinent()){
      t.tryAddUnit(new BasicUnit("Unit", 10));
      t.tryChangeOwnerTo((long) (count / 3));
      count++;
    }
    riskMap.getOwners().put(0L, new Owner(0, 6, 100, 100));
    riskMap.getOwners().put(1L, new Owner(0, 6, 100, 100));
    riskMap.getOwners().put(2L, new Owner(0, 6, 100, 100));
    
    return riskMap;
  }


  private void displayMap(RISKMap riskMap) {
    TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
    idToColor.put((long)0, new Color("Red"));
    idToColor.put((long)1, new Color("Green"));
    idToColor.put((long)2, new Color("Blue"));

    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    System.out.println(mapTextView.displayMap());
  }

}
