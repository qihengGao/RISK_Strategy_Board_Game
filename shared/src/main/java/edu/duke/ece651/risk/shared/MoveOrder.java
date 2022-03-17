package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.checker.*;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;

public class MoveOrder extends Order {
  private final ActionChecker moveChecker;

  public MoveOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
    super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount, "Move");
    this.moveChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistMoveChecker(null))));
  }

  /**
   * execute a move order
   */
  @Override
  public String executeOrder(RISKMap riskMap) {
    String check_message = moveChecker.checkMove(riskMap, this);
    if (check_message == null){
      Territory sourceTerritory = riskMap.getTerritoryByName(this.srcTerritory);
      Territory destinationTerritory = riskMap.getTerritoryByName(this.destTerritory);
          
      Unit sourceTerritoryUnit = sourceTerritory.getUnitByType(this.unitType);
      Unit destinationTerritoryUnit = destinationTerritory.getUnitByType(this.unitType);
      sourceTerritoryUnit.tryDecreaseAmount(this.unitAmount);
      destinationTerritoryUnit.tryIncreaseAmount(this.unitAmount);
    }
    return check_message;
  }
}
