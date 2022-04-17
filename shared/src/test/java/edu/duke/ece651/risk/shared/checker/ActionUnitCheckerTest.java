package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class ActionUnitCheckerTest {

    private RISKMap buildTestMap(){
        AbstractMapFactory tmf = new RandomMapFactory();
        RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
        int count = 0;
        for (Territory t: riskMap.getContinent()){
            Unit u = new BasicUnit("Unit", 10);
            u.setOwnerId((long) (count / 3));
            t.tryAddUnit(u);
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

    @Test
    void checkMyRule() {
        ActionUnitChecker checker = new ActionUnitChecker(null);
        RISKMap map = buildTestMap();
        displayMap(map);
        Order move1 = new MoveOrder(0L,"Test0", "Test2", "Soldier", 1);
        assertEquals(checker.checkMyRule(map, move1),
                "You do not have Soldier in Test0!"
                );
        Order move2 = new MoveOrder(0L,"Test0", "Test2", "Unit", -1);
        assertEquals(checker.checkMyRule(map, move2),
                "Unit Amount must be positive integer!"
        );
        Order move3 = new MoveOrder(0L,"Test0", "Test2", "Unit", 11);
        assertEquals(checker.checkMyRule(map, move3),
                "You do not have sufficient Unit to move in Test0!"
        );
        Order validMove = new MoveOrder(0L,"Test0", "Test2", "Unit", 1);
        assertNull(checker.checkMyRule(map, validMove));

    }
}