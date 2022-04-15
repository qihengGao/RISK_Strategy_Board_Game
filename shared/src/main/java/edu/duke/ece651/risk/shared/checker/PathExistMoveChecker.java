package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;

import javax.management.Query;
import java.util.*;

public class
PathExistMoveChecker extends ActionChecker {

  /**
   * check if the path exist for move using DFS
   * @param riskMap
   * @param moveOrder
   * @return null if all rules passed; return error message if rule didn't pass
   */
  @Override
  protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
    if (moveOrder.getSrcTerritory().equals(moveOrder.getDestTerritory())){
      return "You cannot move within same territory!";
    }
    Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
    HashSet<Territory> visited = new HashSet<Territory>();

    Territory dst = dfsToDst(riskMap, moveOrder.getPlayerID(), src, moveOrder.getDestTerritory(), visited);
    if (dst == null){
      return "Move Order path does not exist in your territories!";
    }
    return null;
  }

  /**
   * Depth First Search to confirm path exist from curr territory to destination territory with name dstName
   * within the territories of a player with ID
   * @param riskMap the map to operate on
   * @param ID check ownership of territory
   * @param curr curr territory
   * @param dstName destination territory name
   * @param visited visited territories
   * @return the Territory if found a path. null if not
   */
  private Territory dfsToDst(RISKMap riskMap, long ID, Territory curr, String dstName, HashSet<Territory> visited){
    //if find the destination, return this territory
    if (curr.getName().equals(dstName)){
      return riskMap.getTerritoryByName(dstName);
    }
    //if not
    //add curr into visited
    visited.add(curr);
    Territory end = null;

    //iterate over neighbors owned by same person
    for (String tName: curr.getNeighbors()){
      Territory t = riskMap.getTerritoryByName(tName);
      if (!visited.contains(t) && (ownBySelf(t, ID) || ownByAlliance(riskMap, t, ID))){
        end = dfsToDst(riskMap, ID, t, dstName, visited);
        //if found destination, return
        if (end!=null){
          return end;
        }
      }
    }
    //if not return null
    return null;
  }

  private boolean ownBySelf(Territory t, long ID) {
    return t.getOwnerID().equals(ID);
  }

  private boolean ownByAlliance(RISKMap riskMap, Territory t, long ID) {
    return riskMap.getOwners().getOrDefault(ID, new Owner(-1L)).getAlliance().contains(t.getOwnerID());
  }

  public PathExistMoveChecker(ActionChecker next) {
    super(next);
  }
}
