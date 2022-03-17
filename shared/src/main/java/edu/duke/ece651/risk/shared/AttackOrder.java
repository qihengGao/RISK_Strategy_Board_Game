package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.checker.*;

public class AttackOrder extends Order{
  private final ActionChecker attackChecker;

  public AttackOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
    super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount, "Attack");
    this.attackChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistAttackChecker(null))));
  }

  @Override
  public String executeOrder(RISKMap riskMap) {
    // TODO change this to battlefield

    String check_message = attackChecker.checkMove(riskMap, this);
    if (check_message == null){
      Territory sourceTerritory = riskMap.getTerritoryByName(this.srcTerritory);
      Territory destinationTerritory = riskMap.getTerritoryByName(this.destTerritory);

      Unit sourceTerritoryUnit = sourceTerritory.getUnitByType(this.unitType);
      Unit destinationTerritoryUnit = destinationTerritory.getUnitByType(this.unitType);
      sourceTerritoryUnit.tryDecreaseAmount(this.unitAmount);
      int difference = this.unitAmount - destinationTerritoryUnit.getAmount();
      if (difference>0){
        destinationTerritoryUnit.tryDecreaseAmount(destinationTerritoryUnit.getAmount());
        destinationTerritoryUnit.tryIncreaseAmount(difference);
        destinationTerritory.tryChangeOwnerTo(this.playerID);
      }
      else {
        destinationTerritoryUnit.tryDecreaseAmount(this.unitAmount);
      }
    }
    return check_message;
  }
}
