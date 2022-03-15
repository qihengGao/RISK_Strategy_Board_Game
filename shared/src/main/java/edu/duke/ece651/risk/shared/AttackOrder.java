package edu.duke.ece651.risk.shared;

public class AttackOrder extends Order{
  private final ActionChecker attackChecker;

  public AttackOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
    super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount);
    this.attackChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistAttackChecker(null))));
  }

  @Override
  public String executeOrder(RISKMap riskMap) {
    // TODO Auto-generated method stub
    String check_message = attackChecker.checkMove(riskMap, this);
    return null;
  }
}
