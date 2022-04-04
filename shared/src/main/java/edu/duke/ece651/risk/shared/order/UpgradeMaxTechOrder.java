package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.map.RISKMap;

public class UpgradeMaxTechOrder extends Order {

    public UpgradeMaxTechOrder(){
    }

    public UpgradeMaxTechOrder(long ID, String srcTerritory, String destTerritory, String unitType, int unitAmount) {
        this();
        this.playerID = ID;
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
        this.orderType = "Upgrade Tech Level";
    }

    @Override
    public String executeOrder(RISKMap riskMap) {
        return riskMap.getOwners().get(this.playerID).tryUpgradeTechLevel();
    }
}
