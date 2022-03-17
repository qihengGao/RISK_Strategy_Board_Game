package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.Order;
import edu.duke.ece651.risk.shared.RISKMap;
import edu.duke.ece651.risk.shared.Territory;

public class TerrExistChecker extends ActionChecker {
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
