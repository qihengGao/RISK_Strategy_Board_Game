package edu.duke.ece651.risk.shared.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.AttackOrder;
import edu.duke.ece651.risk.shared.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;

public class PathExistAttackCheckerTest {

    /**
     * helper function to create run checker
     * @param _territory1Name territory 1 name, should be territory1
     * @param _territory2Name territory 2 name, should be territory2
     * @param _owner1Id
     * @param _owner2Id
     * @param expected
     */
    private void helper_checkMyRule(String _territory1Name, String _territory2Name, long _owner1Id, long _owner2Id, String expected){
        String territory1Name = "territory1";
        String territory2Name = "territory2";
        long owner1Id = _owner1Id;
        long owner2Id = _owner2Id;

        Territory territory1 = new BasicTerritory(territory1Name);
        Territory territory2 = new BasicTerritory(territory2Name);
        territory1.tryAddNeighbor(territory2Name);
        territory2.tryAddNeighbor(territory1Name);
        territory1.tryChangeOwnerTo(owner1Id);
        territory2.tryChangeOwnerTo(owner2Id);

        HashSet<Territory> territories = new HashSet<>();
        territories.add(territory1);
        territories.add(territory2);
        
        RISKMap riskMap = new RISKMap(territories);
        Order attackOrder = new AttackOrder(owner1Id, _territory1Name, _territory2Name, "soldier", 10);

        ActionChecker checker = new PathExistAttackChecker(null);
        String result = checker.checkMyRule(riskMap, attackOrder);

        assertEquals(expected, result);
    }

    /**
     * check valid rule
     */
    @Test
    public void test_checkMyRule_valid(){
        this.helper_checkMyRule("territory1", "territory2", 0L, 1L, null);
    }

    /**
     * check invalid destination
     */
    @Test
    public void test_checkMyRule_invalidDestination(){
        String expected = "You cannot attack territoryXX from territory1!";
        this.helper_checkMyRule("territory1", "territoryXX", 0L, 1L, expected);
    }

    /**
     * check attacking own territory
     */
    @Test
    public void test_checkMyRule_attackOwnTerritory(){
        String expected = "You cannot attack your own territory!";
        this.helper_checkMyRule("territory1", "territory2", 0L, 0L, expected);
    }
}
