package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

public class PathExistAttackChecker extends ActionChecker{
    public PathExistAttackChecker(ActionChecker next) {
        super(next);
    }

    /**
     * check if the path exist for attack
     * @param riskMap
     * @param moveOrder
     * @return null if all rules passed; return error message if rule didn't pass
     */
    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
        Territory dst = riskMap.getTerritoryByName(src.getNeighborByName(moveOrder.getDestTerritory()));

        if (dst == null) {
            return "You cannot attack " + moveOrder.getDestTerritory() + " from " + moveOrder.getSrcTerritory() + "!";
        }
        if (dst.getOwnerID().equals(src.getOwnerID())){
            return "You cannot attack your own territory!";
        }
        return null;
    }

}
