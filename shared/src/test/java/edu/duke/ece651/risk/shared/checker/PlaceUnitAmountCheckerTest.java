package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
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

        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", -1);
        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }

    @Test
    public void test_checkMyRule_exceedTotalAmount(){
        PlaceRuleChecker checker = new PlaceUnitAmountChecker(null,10);
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());

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

        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", 4);
        placeOrders.put("test3", 4);

        assertThrows(IllegalArgumentException.class, ()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }

    @Test
    public void test_checkMyRule_success(){
        PlaceRuleChecker checker = new PlaceUnitAmountChecker(null,10);
        RISKMap riskMap = new RISKMap(new HashSet<Territory>());

        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", 4);
        placeOrders.put("test3", 5);

        assertDoesNotThrow(()->checker.checkMyRule(riskMap, placeOrders, 0L));
    }

    @Test
    public void test_chcckPlace_intergrated(){
        PlaceRuleChecker terriExistChecker = new PlaceTerrExistChecker(null);
        PlaceRuleChecker unitAmountChecker = new PlaceUnitAmountChecker(terriExistChecker,10);

        Territory territory1 = new BasicTerritory("test1");
        Territory territory2 = new BasicTerritory("test2");
        Territory territory3 = new BasicTerritory("test3");
        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);
        territories.add(territory3);

        RISKMap riskMap = new RISKMap(territories);

        Map<String, Integer> placeOrders = new HashMap<>();
        placeOrders.put("test1", 1);
        placeOrders.put("test2", 4);
        placeOrders.put("test3", 5);

        assertDoesNotThrow(()->unitAmountChecker.checkPlace(riskMap, placeOrders, 0L));
    }
}
