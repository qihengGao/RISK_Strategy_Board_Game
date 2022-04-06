package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;

import java.util.Map;

//chain of rule checkers for unit placements
public abstract class PlaceRuleChecker {

    //chain of rule checkers
    private final PlaceRuleChecker next;

    /**
     * Checker ctor set next rule checker
     * @param next
     */
    public PlaceRuleChecker(PlaceRuleChecker next){
        this.next = next;
    }

    /**
     * check the rule of some arbitrary rule checker
     * @param riskMap
     * @param unitPlaceOrders
     * @param userId
     * @return
     */
    protected abstract void checkMyRule(RISKMap riskMap, Map<String, Integer> unitPlaceOrders, Long userId);

    /**
     * check all the rules in the chain of checkers
     * @param riskMap
     * @param unitPlaceOrders
     * @param userId
     */
    public void checkPlace (RISKMap riskMap, Map<String, Integer> unitPlaceOrders, Long userId) {
        //if we fail our own rule: stop the placement is not legal
        checkMyRule(riskMap, unitPlaceOrders, userId);
        //other wise, ask the rest of the chain.
        if (next != null) {
            next.checkPlace(riskMap, unitPlaceOrders, userId);
        }
        //if there are no more rules, then the placement is legal
    }

}
