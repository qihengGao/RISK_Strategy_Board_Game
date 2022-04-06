package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class PathResourceMoveCheckerTest {

    private void displayMap(RISKMap riskMap) {
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
        idToColor.put((long)0, new Color("Red"));
        idToColor.put((long)1, new Color("Green"));
        idToColor.put((long)2, new Color("Blue"));

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        System.out.println(mapTextView.displayMap());
    }
    @Test
    void checkMyRule() {
        //build test map
        Territory territory1 = new BasicTerritory("terr1", 5);
        Territory territory2 = new BasicTerritory("terr2", 10);
        Territory territory3 = new BasicTerritory("terr3", 1);
        Territory territory4 = new BasicTerritory("terr4", 1);
        Territory territory5 = new BasicTerritory("terr5", 2);

        territory1.tryAddUnit(new BasicUnit("Unit", 10));
        territory2.tryAddUnit(new BasicUnit("Unit", 10));
        territory3.tryAddUnit(new BasicUnit("Unit", 10));
        territory4.tryAddUnit(new BasicUnit("Unit", 10));
        territory5.tryAddUnit(new BasicUnit("Unit", 10));

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
        riskMap.getOwners().put(0L, new Owner(0));
        riskMap.getOwners().put(1L, new Owner(1));
        riskMap.getOwners().put(2L, new Owner(2));

        PathResourceMoveChecker checker = new PathResourceMoveChecker(null);
        displayMap(riskMap);
        Order order = new MoveOrder(0L, "terr1","terr4","Unit", 1);
        assertEquals(checker.checkMyRule(riskMap, order),
                "You do not have sufficient food resources");

        riskMap.getOwners().get(0L).tryAddOrRemoveFoodResource(10);
        assertNull(checker.checkMyRule(riskMap, order));

    }

    @Test
    public void test_doDijkstra(){
        Territory territory1 = new BasicTerritory("terr1", 5);
        Territory territory2 = new BasicTerritory("terr2", 10);
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

        PathResourceMoveChecker checker = new PathResourceMoveChecker(null);
        HashMap<Territory, Territory> parents = checker.doDijkstra(riskMap, 0L, territory1);
        ArrayList<Territory> actualPath = checker.getPathFromSrcToDest(parents, territory1, territory4);
        assertEquals(3, actualPath.size());
        assertSame(territory3, actualPath.get(0));
        assertSame(territory5, actualPath.get(1));
        assertSame(territory4, actualPath.get(2));
        assertEquals(4, checker.getCostFromSrcToDest(actualPath));

        territory3.tryChangeOwnerTo(1L);
        parents = checker.doDijkstra(riskMap, 0L, territory1);
        actualPath = checker.getPathFromSrcToDest(parents, territory1, territory5);
        assertEquals(3, actualPath.size());
        assertSame(territory2, actualPath.get(0));
        assertSame(territory4, actualPath.get(1));
        assertSame(territory5, actualPath.get(2));
        assertEquals(13, checker.getCostFromSrcToDest(actualPath));
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
        PathResourceMoveChecker checker = new PathResourceMoveChecker(null);
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

        PathResourceMoveChecker checker = new PathResourceMoveChecker(null);
        assertEquals(15, checker.getCostFromSrcToDest(paths));
    }

    @Test
    public void test_dfsToDst(){
        // success: src = dest
        Territory territory1 = new BasicTerritory("test1");
        Territory territory2 = new BasicTerritory("test2");
        territory1.tryChangeOwnerTo(0L);
        territory2.tryChangeOwnerTo(0L);

        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);
        RISKMap riskMap = new RISKMap(territories);
        riskMap.connectTerr("test1", "test2");

        PathResourceMoveChecker checker = new PathResourceMoveChecker(null);

        // source same as destination
        Territory returnedTerritory = checker.dfsToDst(riskMap, 0L, territory1, "test1", new HashSet<Territory>());
        assertSame(returnedTerritory, territory1);

        returnedTerritory = checker.dfsToDst(riskMap, 0L, territory1, "test2", new HashSet<Territory>());
        assertSame(returnedTerritory, territory2);
    }
}