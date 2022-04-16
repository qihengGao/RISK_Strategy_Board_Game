package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

public class SrcOwnershipChecker extends ActionChecker {

  /**
   * check if the source territory is owned by this player
   * @param riskMap
   * @param moveOrder
   * @return
   */
  @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    if (!riskMap.getOwners().get(moveOrder.getPlayerID()).getAlliance().contains(src.getOwnerID())){
      if (src.getOwnerID() != moveOrder.getPlayerID()){
        return "You must place orders from your own territories!";
      }
    }
    return null;
  }

  public SrcOwnershipChecker(ActionChecker next) {
    super(next);
  }

}
