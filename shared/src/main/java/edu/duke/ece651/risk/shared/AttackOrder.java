package edu.duke.ece651.risk.shared;

public class AttackOrder extends Order{
  protected final ActionChecker attackChecker;

  public AttackOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
    super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount, "Attack");
    this.attackChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistAttackChecker(null))));
  }

  @Override
  public String executeOrder(RISKMap riskMap) {
    String checkMessage = attackChecker.checkMove(riskMap, this);
    return null;
  }
}
