package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class PathResourceMoveChecker extends ActionChecker {

  /**
   * check if the move order has enough resources to operate on
   * @param riskMap
   * @param moveOrder
   * @return null if all rules passed; return error message if rule didn't pass
   */
    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
        Territory dst = riskMap.getTerritoryByName(moveOrder.getDestTerritory());

        HashMap<Territory, Territory> parents = doDijkstra(riskMap, moveOrder.getPlayerID(), src);
        ArrayList<Territory> path = getPathFromSrcToDest(parents, src, dst);
        int cost = -1*getCostFromSrcToDest(path)*moveOrder.getUnitAmount();

        return riskMap.getOwners().get(moveOrder.getPlayerID()).tryAddOrRemoveFoodResource(cost);
    }


  /**
   * Depth First Search used to build unsettled set of territories connected and own by player (ID)
   * @param riskMap the map to operate on
   * @param ID check ownership of territory
   * @param curr curr territory
   * @param dstName destination territory name
   * @param visited visited territories
   * @return the Territory if found a path. null if not
   */
  protected Territory dfsToDst(RISKMap riskMap, long ID, Territory curr, String dstName, HashSet<Territory> visited){
    if (curr.getName().equals(dstName)){
      return riskMap.getTerritoryByName(dstName);
    }
    visited.add(curr);
    Territory end = null;
    for (String tName: curr.getNeighbors()){
      Territory t = riskMap.getTerritoryByName(tName);
//      if (!visited.contains(t) && t.getOwnerID().equals(ID)){
      if (!visited.contains(t) && (ownBySelf(t, ID) || ownByAlliance(riskMap, t, ID))){
        end = dfsToDst(riskMap, ID, t, dstName, visited);

        if (end!=null){
          return end;
        }
      }
    }
    return null;
  }

    /**
     //   * helper method to get the next node to compute path
     //   * @param distances
     //   * @param queue
     //   * @return the node to continuing computing path
     //   */
  private Territory getMinimumDistanceTerritory(
          HashMap<Territory, Integer> distances, HashSet<Territory> queue){
    int minimumDistance = Integer.MAX_VALUE;
    Territory toReturn = null;

    for(Territory territory: distances.keySet()){
      if(distances.get(territory) < minimumDistance && queue.contains(territory)){
        toReturn = territory;
        minimumDistance = distances.get(territory);
      }
    }
    return toReturn;
  }



  /**
   * helper method to use djikstra's algorithm
   * to get each territory's parent along the shortest path
   * @param riskMap
   * @param ownerId
   * @return hashmap for each territory's parent
   */
  protected HashMap<Territory, Territory> doDijkstra(RISKMap riskMap,long ownerId, Territory source){
    Iterable<Territory> territories = riskMap.getContinent();

    // a hashmap to store distance to the territory, initalize to infinity
    HashMap<Territory, Integer> distances = new HashMap<Territory, Integer>();
    for(Territory territory: territories){
//      if(territory.getOwnerID().equals(ownerId)){
      if(ownBySelf(territory, ownerId) || ownByAlliance(riskMap, territory, ownerId)) {
        distances.put(territory, Integer.MAX_VALUE);
      }
    }

    // set the distance from source -> source to 0
    distances.put(source, 0);

    // a hashset to record each territory's parent
    HashMap<Territory, Territory> parents = new HashMap<>();
    for(Territory territory: territories){
//      if(territory.getOwnerID().equals(ownerId)) {
      if(ownBySelf(territory, ownerId) || ownByAlliance(riskMap, territory, ownerId)) {
        parents.put(territory, null);
      }
    }

    //build a set of unsettled territory
    HashSet<Territory> queue = new HashSet<Territory>();
    dfsToDst(riskMap, ownerId, source, null, queue);

    while (!queue.isEmpty()){
      Territory currTerritory = getMinimumDistanceTerritory(distances, queue);
      queue.remove(currTerritory);

      Iterable<String> neighborStrs = currTerritory.getNeighbors();
      for(String neighborStr: neighborStrs){
        Territory neighbor = riskMap.getTerritoryByName(neighborStr);
        // approachable neighbor: own / alliance
        if(ownBySelf(neighbor, ownerId) || ownByAlliance(riskMap, neighbor, ownerId)) {
          if (distances.get(currTerritory) + neighbor.getSize() < distances.get(neighbor)) {
            distances.put(neighbor, distances.get(currTerritory) + neighbor.getSize());
            parents.put(neighbor, currTerritory);
          }
        }
      }
    }
    return parents;
  }

  private boolean ownBySelf(Territory territory, long ownerId) {
    return territory.getOwnerID().equals(ownerId);
  }

  private boolean ownByAlliance(RISKMap riskMap, Territory territory, long ownerId) {
    return riskMap.getOwners().getOrDefault(ownerId, new Owner(-1L)).getAlliance().contains(territory.getOwnerID());
  }

  /**
   * get the shortest path from source to territory
   * @param parents
   * @param source
   * @param destination
   * @return an arraylist of path territories if path exists, null otherwise
   */
  protected ArrayList<Territory> getPathFromSrcToDest(HashMap<Territory, Territory> parents,
                                   Territory source, Territory destination){
    if(!parents.keySet().contains(source) || !parents.keySet().contains(destination)){
      return null;
    }

    ArrayList<Territory> path = new ArrayList<>();
    path.add(destination);
    Territory parent = parents.get(destination);

    // get cost of path from source to destination in reverse
    while(parent != null && parent != source){
      path.add(parent);
      parent = parents.get(parent);
    }
    if(parent == null){
      return null;
    }

    Collections.reverse(path);
    return path;
  }

  /**
   * return the cost to move from source to destination territory,
   * given that the path is already computed
   * @param path
   * @return cost
   */
  protected int getCostFromSrcToDest(ArrayList<Territory> path){
    int cost = 0;

    for(Territory territory: path){
      cost += territory.getSize();
    }
    return cost;
  }

    public PathResourceMoveChecker(ActionChecker next) {
        super(next);
    }

}
