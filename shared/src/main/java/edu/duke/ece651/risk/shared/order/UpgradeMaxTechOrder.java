package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.map.RISKMap;

public class UpgradeMaxTechOrder extends Order {

    public UpgradeMaxTechOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder,
                               int unitAmount) {
        this.playerID = ID;
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitAmount = unitAmount;
        this.unitType = unitUnderOrder;
        this.orderType = "Move";
    }

    @Override
    public String executeOrder(RISKMap riskMap) {
        return riskMap.getOwners().get(this.playerID).tryUpgradeTechLevel();
    }
}
