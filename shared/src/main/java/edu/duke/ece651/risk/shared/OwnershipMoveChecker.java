package edu.duke.ece651.risk.shared;

public class OwnershipMoveChecker extends ActionChecker {
  @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    Territory dst = riskMap.getTerritoryByName(moveOrder.getDestTerritory());
    if (src.getOwnerID() != moveOrder.getPlayerID() ||
        dst.getOwnerID() != moveOrder.getPlayerID()){
      return "You must operate move orders within your own territories!";
    }
    return null;
  }

  public OwnershipMoveChecker(ActionChecker next) {
    super(next);
  }

}
