package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.checker.*;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class MoveOrder extends Order {
  //specific checkers for this move order
  private final ActionChecker moveChecker;

  /**
   * ctor to specify the chain of rules for this move order
   */
  public MoveOrder() {
    this.moveChecker = new TerrExistChecker(
            new SrcOwnershipChecker(
                    new ActionUnitChecker(
                            new PathExistMoveChecker(
                                    new PathResourceMoveChecker(null)))));
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
   * try to execute this move order
   * move all units into the destination if rules passes
   * @param riskMap
   * @return null if success, error message if some rule didn't pass
   */
  @Override
  public String executeOrder(RISKMap riskMap) {
    String check_message = moveChecker.checkMove(riskMap, this);
    System.out.println("Executing order:"+ this.toString());
    if (check_message == null){
      Territory sourceTerritory = riskMap.getTerritoryByName(this.srcTerritory);
      Territory destinationTerritory = riskMap.getTerritoryByName(this.destTerritory);
      Unit sourceTerritoryUnit = sourceTerritory.getUnitByType(this.unitType);
      sourceTerritoryUnit.tryDecreaseAmount(this.unitAmount);
      Unit toAdd = new BasicUnit(this.unitType, this.unitAmount);
      destinationTerritory.tryAddUnit(toAdd);
    }
    return check_message;
  }

}
