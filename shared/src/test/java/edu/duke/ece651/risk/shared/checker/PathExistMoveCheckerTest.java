package edu.duke.ece651.risk.shared.checker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import edu.duke.ece651.risk.shared.*;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
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

  @Test
  public void test_doDijkstra(){
    Territory territory1 = new BasicTerritory("terr1", 10);
    Territory territory2 = new BasicTerritory("terr2", 5);
    Territory territory3 = new BasicTerritory("terr3", 1);
    Territory territory4 = new BasicTerritory("terr4", 1);
    Territory territory5 = new BasicTerritory("terr5", 2);

    territory1.tryChangeOwnerTo(0L);
    territory2.tryChangeOwnerTo(0L);
    territory3.tryChangeOwnerTo(0L);
    territory4.tryChangeOwnerTo(0L);
    territory5.tryChangeOwnerTo(0L);

    HashSet<Territory> territories = new HashSet<>();
    territories.add(territory1);
    territories.add(territory2);
    territories.add(territory3);
    territories.add(territory4);
    territories.add(territory5);

    RISKMap riskMap = new RISKMap(territories);
    riskMap.connectTerr("terr1", "terr2");
    riskMap.connectTerr("terr1", "terr3");
    riskMap.connectTerr("terr2", "terr3");
    riskMap.connectTerr("terr2", "terr4");
    riskMap.connectTerr("terr3", "terr5");
    riskMap.connectTerr("terr4", "terr5");

    PathExistMoveChecker checker = new PathExistMoveChecker(null);
    HashMap<Territory, Territory> parents = checker.doDijkstra(riskMap, 0L, territory1);
    ArrayList<Territory> actualPath = checker.getPathFromSrcToDest(parents, territory1, territory4);
    assertEquals(3, actualPath.size());
    assertSame(territory3, actualPath.get(0));
    assertSame(territory5, actualPath.get(1));
    assertSame(territory4, actualPath.get(2));

    territory3.tryChangeOwnerTo(1L);
    parents = checker.doDijkstra(riskMap, 0L, territory1);
    actualPath = checker.getPathFromSrcToDest(parents, territory1, territory5);
    assertEquals(3, actualPath.size());
    assertSame(territory2, actualPath.get(0));
    assertSame(territory4, actualPath.get(1));
    assertSame(territory5, actualPath.get(2));
  }

  @Test
  public void test_getPathFromSrcToDest(){
    Territory territory1 = new BasicTerritory("terr1");
    Territory territory2 = new BasicTerritory("terr2");
    Territory territory3 = new BasicTerritory("terr3");
    Territory territory4 = new BasicTerritory("terr4");
    Territory territory5 = new BasicTerritory("terr5");

    HashMap<Territory, Territory> parents = new HashMap<>();
    parents.put(territory1, null);
    parents.put(territory2, territory1);
    parents.put(territory4, territory3);
    parents.put(territory3, territory2);

    // success: path exists between terr1 -> terr2 -> terr3 -> terr4
    PathExistMoveChecker checker = new PathExistMoveChecker(null);
    ArrayList<Territory> actualPath = checker.getPathFromSrcToDest(parents, territory1, territory4);
    assertEquals(3, actualPath.size());
    assertSame(territory2, actualPath.get(0));
    assertSame(territory3, actualPath.get(1));
    assertSame(territory4, actualPath.get(2));

    // error: source or destination not in hashmap, e.g. terri5
    actualPath = checker.getPathFromSrcToDest(parents, territory1, territory5);
    assertNull(actualPath);
    actualPath = checker.getPathFromSrcToDest(parents, territory5, territory1);
    assertNull(actualPath);

    // error: both source and destination exist in hashmap, but path does not exist
    // e.g. terr4 -> terr 3
    actualPath = checker.getPathFromSrcToDest(parents, territory4, territory3);
    assertNull(actualPath);
  }

  @Test
  public void test_getCostFromSrcToDest(){
    Territory territory1 = new BasicTerritory("terr1", 1);
    Territory territory2 = new BasicTerritory("terr2", 2);
    Territory territory3 = new BasicTerritory("terr3", 3);
    Territory territory4 = new BasicTerritory("terr4", 4);
    Territory territory5 = new BasicTerritory("terr5", 5);

    ArrayList<Territory> paths = new ArrayList<>();
    paths.add(territory1);
    paths.add(territory2);
    paths.add(territory3);
    paths.add(territory4);
    paths.add(territory5);

    PathExistMoveChecker checker = new PathExistMoveChecker(null);
    assertEquals(15, checker.getCostFromSrcToDest(paths));
  }

}
