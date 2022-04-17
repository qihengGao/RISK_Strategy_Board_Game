package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;

public class SrcOwnershipAttackChecker extends ActionChecker {
    /**
     * check if the source territory is owned by this player
     * @param riskMap
     * @param moveOrder
     * @return
     */
    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
        if (src.getOwnerID().equals(moveOrder.getPlayerID())){
            return null;
        }
        return "You must place attack orders from your own territories!";
    }

    public SrcOwnershipAttackChecker(ActionChecker next) {
        super(next);
    }

}
