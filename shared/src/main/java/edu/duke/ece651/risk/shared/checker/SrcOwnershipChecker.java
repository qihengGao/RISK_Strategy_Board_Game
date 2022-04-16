package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Owner;
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
    if (ownBySelf(src, moveOrder.getPlayerID()) || ownByAlliance(riskMap, src, moveOrder.getPlayerID())){
      return null;
    }
    return "You must place orders from your own territories or from your alliance territories!";
  }

  public SrcOwnershipChecker(ActionChecker next) {
    super(next);
  }

  private boolean ownBySelf(Territory t, long ID) {
    return t.getOwnerID().equals(ID);
  }

  private boolean ownByAlliance(RISKMap riskMap, Territory t, long ID) {
    return riskMap.getOwners().getOrDefault(ID, new Owner(-1L)).getAlliance().contains(t.getOwnerID());
  }
}
