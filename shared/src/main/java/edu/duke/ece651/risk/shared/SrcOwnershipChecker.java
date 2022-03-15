package edu.duke.ece651.risk.shared;

public class SrcOwnershipChecker extends ActionChecker {
  @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    if (src.getOwnerID() != moveOrder.getPlayerID()){
      return "You must place orders from your own territories!";
    }
    return null;
  }

  public SrcOwnershipChecker(ActionChecker next) {
    super(next);
  }

}