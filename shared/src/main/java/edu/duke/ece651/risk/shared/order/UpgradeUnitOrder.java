package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.checker.ActionChecker;
import edu.duke.ece651.risk.shared.checker.SrcOwnershipChecker;
import edu.duke.ece651.risk.shared.checker.SufficientSourceForUpgradeChecker;
import edu.duke.ece651.risk.shared.checker.TerrExistChecker;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

public class UpgradeUnitOrder extends Order{
    private int toLevel;
    private final ActionChecker upgradeUnitChecker;

    UpgradeUnitOrder() {
        this.upgradeUnitChecker =new SrcOwnershipChecker(new SufficientSourceForUpgradeChecker(null));
    }

    UpgradeUnitOrder(long ID, String srcTerritory, String unitType, int unitAmount, int toLevel) {
        this();
        this.playerID = ID;
        this.srcTerritory = srcTerritory;
        this.destTerritory = srcTerritory;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
        this.orderType = "Upgrade Unit";
        this.toLevel = toLevel;
    }


    @Override
    public String executeOrder(RISKMap riskMap) {

        Unit toUpgrade = new BasicUnit(this.unitType, this.unitAmount);
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
