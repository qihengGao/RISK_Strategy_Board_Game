package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;

public class ActionUnitChecker extends ActionChecker {
    @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    Unit unitToMove = src.getUnitByType(moveOrder.getUnitType());
    if (unitToMove == null){
      return "You do not have " + moveOrder.getUnitType() +" in "+ moveOrder.getSrcTerritory() +"!";
    }
    if (moveOrder.getUnitAmount()<0){
      return "Unit Amount must be positive integer!";
    }
    
    if (unitToMove.getAmount() < moveOrder.getUnitAmount()){
      return "You do not have sufficient " + moveOrder.getUnitType() + " to move in "+ moveOrder.getSrcTerritory() +"!";
    }
    return null;
  }

  public ActionUnitChecker(ActionChecker next) {
    super(next);
  }

}
