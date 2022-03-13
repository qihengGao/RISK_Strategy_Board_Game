package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public abstract class Order implements Serializable{
    private Unit unitUnderOrder;
    private Territory srcTerritory;
    private Territory destTerritory;
    
    public Unit getUnitUnderOrder() {
        return unitUnderOrder;
    }

    // public void setUnitUnderOrder(Unit unitUnderOrder) {
    //     this.unitUnderOrder = unitUnderOrder;
    // }

    public Territory getSrcTerritory() {
        return srcTerritory;
    }

    // public void setSrcTerritory(Territory srcTerritory) {
    //     this.srcTerritory = srcTerritory;
    // }

    public Territory getDestTerritory() {
        return destTerritory;
    }

    // public void setDestTerritory(Territory destTerritory) {
    //     this.destTerritory = destTerritory;
    // }


}
