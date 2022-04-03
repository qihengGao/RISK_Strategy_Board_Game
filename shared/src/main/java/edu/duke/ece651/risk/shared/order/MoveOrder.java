package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.checker.*;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class MoveOrder extends Order {
  private final ActionChecker moveChecker;

  public MoveOrder() {
    this.moveChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistMoveChecker(new PathResourceMoveChecker(null)))));
  }

  public MoveOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
    this();
    this.playerID = ID;
    this.srcTerritory = srcTerritory;
    this.destTerritory = destTerritory;
    this.unitAmount = unitAmount;
    this.unitType = unitUnderOrder;
    this.orderType = "Move";


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
