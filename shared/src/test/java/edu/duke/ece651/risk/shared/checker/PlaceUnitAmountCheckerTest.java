package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceUnitAmountCheckerTest {
    @Test
    public void test_checkMyRule_unitAmountNegative(){
        PlaceRuleChecker checker = new PlaceUnitAmountChecker(null,10);
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());

        // error: unitAmount < 0
        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", -1);
        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }

    @Test
    public void test_checkMyRule_exceedTotalAmount(){
        PlaceRuleChecker checker = new PlaceUnitAmountChecker(null,10);
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());

        // error: unitAmount < 0
        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", 4);
        placeOrders.put("test3", 6);

        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }

    @Test
    public  void test_checkMyRule_notUsingAllUnits(){
        PlaceRuleChecker checker = new PlaceUnitAmountChecker(null,10);
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());

        // error: unitAmount < 0
        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", 4);
        placeOrders.put("test3", 4);

        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }

    @Test
    public  void test_checkMyRule_success(){
        PlaceRuleChecker checker = new PlaceUnitAmountChecker(null,10);
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());

        // error: unitAmount < 0
        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", 4);
        placeOrders.put("test3", 5);

        assertDoesNotThrow(()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }
}
