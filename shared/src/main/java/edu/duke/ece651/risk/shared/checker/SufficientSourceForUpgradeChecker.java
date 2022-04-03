package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

/**
 * Checker for resource to upgrade unit.
 */
public class SufficientSourceForUpgradeChecker extends ActionChecker{

    public SufficientSourceForUpgradeChecker(ActionChecker next) {
        super(next);
    }
    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        Unit toUpgrade = new BasicUnit(moveOrder.getUnitType(), moveOrder.getUnitAmount());
        if (moveOrder.getToLevel() <= toUpgrade.getLevel()) {
            return toUpgrade.getType() + " level " + toUpgrade.getLevel() + "cannot upgrade to " + moveOrder.getToLevel();
        }
        int techCost = toUpgrade.getAmount() * toUpgrade.getCostToLevel(moveOrder.getToLevel());
        if (techCost > riskMap.getOwners().get(moveOrder.getPlayerID()).getOwnedTechResource()) {
            return "Insufficient resource to upgrade " + toUpgrade;
        }
        return null;
    }
}
