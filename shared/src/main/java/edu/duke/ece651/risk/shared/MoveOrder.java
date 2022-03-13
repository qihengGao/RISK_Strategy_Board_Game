package edu.duke.ece651.risk.shared;

public class MoveOrder extends Order {


    public MoveOrder(String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
        super(srcTerritory, destTerritory, unitUnderOrder, unitAmount);
    }
}
