package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public abstract class Order implements Serializable{
    protected String srcTerritory;
    protected String destTerritory;
    protected String unitType;
    protected int unitAmount;

    public Order(String srcTerritory, String destTerritory, String unitType, int unitAmount) {
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
    }

    public String getSrcTerritory(){
        return this.srcTerritory;
    }

    public String getDestTerritory(){
        return this.destTerritory;
    }

    public String getUnitType(){
        return this.unitType;
    }

    public int getUnitAmount(){
        return this.unitAmount;
    }

    public abstract void executeOrder(RISKMap riskMap);

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(srcTerritory);
        result.append("->");
        result.append(destTerritory);
        result.append(": "+unitType+" ");
        result.append(unitAmount);
        return result.toString();
    }

}
