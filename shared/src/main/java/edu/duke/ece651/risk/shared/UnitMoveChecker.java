package edu.duke.ece651.risk.shared;

public class UnitMoveChecker extends ActionChecker {
    @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    Unit unitToMove = src.getUnitByType(moveOrder.getUnitType());
    if (unitToMove == null){
      return "The unit you want to move does not exist in your source territory!";
    }
    if (moveOrder.getUnitAmount()<0){
      return "Unit Amount must be positive integer!";
    }
    
    if (unitToMove.getAmount() < moveOrder.getUnitAmount()){
      return "You do not have sufficient " + moveOrder.getUnitType() + " to move in your source territory!";
    }
    return null;
  }

  public UnitMoveChecker(ActionChecker next) {
    super(next);
  }

}
