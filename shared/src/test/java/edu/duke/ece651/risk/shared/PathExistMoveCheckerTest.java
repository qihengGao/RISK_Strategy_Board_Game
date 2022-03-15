package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

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

    //test long path (not adjacent)
    riskMap.getTerritoryByName("Test4").tryChangeOwnerTo(0);
    riskMap.getTerritoryByName("Test5").tryChangeOwnerTo(0);
    Order order_long = new MoveOrder(0, "Test0", "Test1", "Unit", 2);
    assertEquals(pathChecker.checkMove(riskMap, order_long), null);
    
  }

  private RISKMap buildTestMap(){
    AbstractMapFactory tmf = new RandomMapFactory();
    RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
    int count = 0;
    for (Territory t: riskMap.getContinent()){
      t.tryAddUnit(new BasicUnit("Unit", 10));
      t.tryChangeOwnerTo(count / 3);
      count++;
    }
    
    return riskMap;
  }

  /**
   * Uncomment to help you write test cases
  private void displayMap(RISKMap riskMap) {
    TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
    idToColor.put((long)0, new Color("Red"));
    idToColor.put((long)1, new Color("Green"));
    idToColor.put((long)2, new Color("Blue"));
    
    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    System.out.println(mapTextView.displayMap());
  }
  */

}
