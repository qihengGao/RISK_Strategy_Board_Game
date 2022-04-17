package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.checker.*;
import edu.duke.ece651.risk.shared.map.RISKMap;

public class FormAllianceOrder extends Order{
    /**
     * ctor to specify the chain of rules for this FormAllianceOrder
     */
    public FormAllianceOrder(long ID, long allianceID) {
        this.playerID = ID;
        this.allianceID = allianceID;
        this.orderType = "Form Alliance";
    }

    public FormAllianceOrder(){
    }

    @Override
    public String executeOrder(RISKMap riskMap) {
        if (riskMap.getOwners().get(this.playerID).formAlliance(this.allianceID)){
            return null;
        }
        return "Invalid target alliance!";
    }
}
