package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.Order;
import edu.duke.ece651.risk.shared.RISKMap;
import edu.duke.ece651.risk.shared.Territory;

import java.util.HashSet;

public class
PathExistMoveChecker extends ActionChecker {
  @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    HashSet<Territory> visited = new HashSet<Territory>();

    Territory dst = dfsToDst(riskMap, moveOrder.getPlayerID(), src, moveOrder.getDestTerritory(), visited);
    if (dst == null){
      return "Move Order path does not exist in your territories!";
    }
    return null;
  }

  private Territory dfsToDst(RISKMap riskMap, long ID, Territory curr, String dstName, HashSet<Territory> visited){
    if (curr.getName().equals(dstName)){
      return riskMap.getTerritoryByName(dstName);
    }
    visited.add(curr);
    Territory end = null;
    for (String tName: curr.getNeighbors()){
      Territory t = riskMap.getTerritoryByName(tName);
      if (!visited.contains(t) && t.getOwnerID()==ID){
        end = dfsToDst(riskMap, ID, t, dstName, visited);
        if (end!=null){
          return end;
        }
      }
    }
    return null;
  }
      
      
  public PathExistMoveChecker(ActionChecker next) {
    super(next);
  }
}
