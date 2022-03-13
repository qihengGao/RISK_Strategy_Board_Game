package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public class Order implements Serializable{
    private String srcTerritory;
    private String destTerritory;
    private String unitType;
    private int unitAmount;

    public Order(String srcTerritory, String destTerritory, String unitType, int unitAmount) {
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
    }

<<<<<<< HEAD
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
=======
    
>>>>>>> 196c9620eb0f8372f7f1ddd72277f7d1402fa6a2


}
