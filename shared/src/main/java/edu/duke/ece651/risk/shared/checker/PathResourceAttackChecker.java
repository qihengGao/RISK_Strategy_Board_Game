package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PathResourceAttackChecker extends ActionChecker {

    /**
     * check if the attack order has enough resources to operate on
     * @param riskMap
     * @param moveOrder
     * @return null if all rules passed; return error message if rule didn't pass
     */
    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        int cost = -1*moveOrder.getUnitAmount();

        return riskMap.getOwners().get(moveOrder.getPlayerID()).tryAddOrRemoveFoodResource(cost);
    }
    public PathResourceAttackChecker(ActionChecker next) {
        super(next);
    }
}
