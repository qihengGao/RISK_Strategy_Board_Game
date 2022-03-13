package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public class Order implements Serializable{
    private String srcTerritory;
    private String destTerritory;
    private String unitUnderOrder;
    private int unitAmount;

    public Order(String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitUnderOrder = unitUnderOrder;
        this.unitAmount = unitAmount;
    }

    


}
