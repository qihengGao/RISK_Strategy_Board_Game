package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

public class TerrExistChecker extends ActionChecker {

  /**
   * check if all territories exist in the map
   * @param riskMap
   * @param moveOrder
   * @return
   */
  @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    Territory dst = riskMap.getTerritoryByName(moveOrder.getDestTerritory());
    if (src == null){
      return "Your Source territory does not exist!";
    }
    if (dst == null){
      return "Your Destination territory does not exist!";
    }
    return null;
  }

  public TerrExistChecker(ActionChecker next) {
    super(next);
  }
}
