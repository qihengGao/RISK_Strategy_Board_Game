package edu.duke.ece651.risk.shared;

public class AttackOrder extends Order{

    public AttackOrder(String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
        super(srcTerritory, destTerritory, unitUnderOrder, unitAmount);
    }
}
