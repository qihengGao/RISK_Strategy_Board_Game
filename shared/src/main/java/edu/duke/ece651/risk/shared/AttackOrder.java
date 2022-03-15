package edu.duke.ece651.risk.shared;

public class AttackOrder extends Order{

  public AttackOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
    super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount);
  }

  @Override
  public String executeOrder(RISKMap riskMap) {
    // TODO Auto-generated method stub
    return null;
  }
}
