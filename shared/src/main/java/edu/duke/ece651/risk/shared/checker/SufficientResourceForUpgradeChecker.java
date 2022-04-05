package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

/**
 * Checker for resource to upgrade unit.
 */
public class SufficientResourceForUpgradeChecker extends ActionChecker{

    public SufficientResourceForUpgradeChecker(ActionChecker next) {
        super(next);
    }
    @Override
    protected String checkMyRule(RISKMap riskMap, Order unitUpgradeOrder) {
        Unit toUpgrade = new BasicUnit(unitUpgradeOrder.getUnitType(), unitUpgradeOrder.getUnitAmount());
        if (unitUpgradeOrder.getToLevel() <= toUpgrade.getLevel() || unitUpgradeOrder.getToLevel() > toUpgrade.getLevelBound()) {
            return toUpgrade.getType() + " level " + toUpgrade.getLevel() + " cannot upgrade to " + unitUpgradeOrder.getToLevel();
        }
        int techCost = toUpgrade.getAmount() * toUpgrade.getCostToLevel(unitUpgradeOrder.getToLevel());
        if (techCost > riskMap.getOwners().get(unitUpgradeOrder.getPlayerID()).getOwnedTechResource()) {
            return "Insufficient resource to upgrade " + toUpgrade;
        }
        return null;
    }
}
