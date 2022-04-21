package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.checker.ActionChecker;
import edu.duke.ece651.risk.shared.checker.ActionUnitChecker;
import edu.duke.ece651.risk.shared.checker.SrcOwnershipChecker;
import edu.duke.ece651.risk.shared.checker.SufficientResourceForUpgradeChecker;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

public class UpgradeUnitOrder extends Order{
    public void setUpgradeUnitChecker(ActionChecker upgradeUnitChecker) {
        this.upgradeUnitChecker = upgradeUnitChecker;
    }

    //specific checkers for this unit upgrade order
    private ActionChecker upgradeUnitChecker;

    /**
     * ctor to specify the chain of rules for this move order
     */
    public UpgradeUnitOrder() {
        this.upgradeUnitChecker =new SrcOwnershipChecker(new SufficientResourceForUpgradeChecker(null));
    }

    public UpgradeUnitOrder(long ID, String srcTerritory, String unitType, int unitAmount, int toLevel) {
        this();
        this.playerID = ID;
        this.srcTerritory = srcTerritory;
        this.destTerritory = srcTerritory;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
        this.orderType = "Upgrade Unit";
        this.toLevel = toLevel;
    }

    /**
     * try to execute this upgrade unit order
     * move all units into the destination if rules passes
     * @param riskMap
     * @return null if success, error message if some rule didn't pass
     */
    @Override
    public String executeOrder(RISKMap riskMap) {
        Unit toUpgrade = new BasicUnit(this.unitType, this.unitAmount);

        if(this.unitAmount < 0){
            return "cannot upgrade negative amount of units";
        }

        // checker: sufficient tech resource + valid level
        String checkerMsg = this.upgradeUnitChecker.checkMove(riskMap, this);
        if (checkerMsg != null) {
            return checkerMsg;
        }
        if(this.toLevel > riskMap.getOwners().get(this.playerID).getCurrTechlevel()){
            return "cannot upgrade units beyond maximum technology level";
        }
        int techCost = toUpgrade.getAmount() * toUpgrade.getCostToLevel(this.toLevel);
        String message = riskMap.getTerritoryByName(this.srcTerritory).tryUpgradeUnitToLevel(toUpgrade, toLevel);
        if (message != null) {
            return message;
        }
        riskMap.getOwners().get(this.playerID).tryAddOrRemoveTechResource(-techCost);
        return null;
    }
}
