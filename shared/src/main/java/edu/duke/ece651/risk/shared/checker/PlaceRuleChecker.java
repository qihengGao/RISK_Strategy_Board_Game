package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;

import java.util.Map;

public abstract class PlaceRuleChecker {
    private final PlaceRuleChecker next;

    public PlaceRuleChecker(PlaceRuleChecker next){
        this.next = next;
    }

    protected abstract void checkMyRule(RISKMap riskMap, Map<String, Integer> unitPlaceOrders, Long userId);

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
