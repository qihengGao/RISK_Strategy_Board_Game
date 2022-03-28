package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

import javax.management.Query;
import java.util.*;

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
      if (!visited.contains(t) && t.getOwnerID().equals(ID)){
        end = dfsToDst(riskMap, ID, t, dstName, visited);
        if (end!=null){
          return end;
        }
      }
    }
    return null;
  }

  /**
   * helper method to get the next node to compute path
   * @param distances
   * @param queue
   * @return the node to continuing computing path
   */
  private Territory getMinimumDistanceTerritory(
          HashMap<Territory, Integer> distances, HashSet<Territory> queue){
    int minimumDistance = Integer.MAX_VALUE;
    Territory toReturn = null;

    for(Territory territory: distances.keySet()){
      if(distances.get(territory) < minimumDistance && queue.contains(territory)){
        toReturn = territory;
        minimumDistance = territory.getSize();
      }
    }
    return toReturn;
  }

  /**
   * helper method to use djikstra's algorithm
   * to get each territory's parent along the shortest path
   * @param riskMap
   * @param ownerId
   * @return
   */
  private HashMap<Territory, Territory> doDijkstra(RISKMap riskMap, long ownerId){
    Iterable<Territory> territories = riskMap.getContinent();

    // a hashmap to store distance to the territory, initalize to infinity
    HashMap<Territory, Integer> distances = new HashMap<Territory, Integer>();
    for(Territory territory: territories){
      if(territory.getOwnerID() == ownerId){
        distances.put(territory, Integer.MAX_VALUE);
      }
    }

    // a hashset to record each territory's parent
    HashMap<Territory, Territory> parents = new HashMap<>();
    for(Territory territory: territories){
      if(territory.getOwnerID() == ownerId) {
        parents.put(territory, null);
      }
    }

    HashSet<Territory> queue = new HashSet<>();
    for(Territory territory: territories){
      if(territory.getOwnerID() == ownerId){
        queue.add(territory);
      }
    }

    while (!queue.isEmpty()){
      Territory currTerritory = this.getMinimumDistanceTerritory(distances, queue);
      queue.remove(currTerritory);

      Iterable<String> neighborStrs = currTerritory.getNeighbors();
      for(String neighborStr: neighborStrs){
        Territory neighbor = riskMap.getTerritoryByName(neighborStr);
        if(neighbor.getOwnerID() == ownerId) {
          if (distances.get(currTerritory) + neighbor.getSize() < distances.get(neighbor)) {
            distances.put(neighbor, distances.get(currTerritory) + neighbor.getSize());
            parents.put(neighbor, currTerritory);
          }
        }
      }
    }
    return parents;
  }

  /**
   * get the shortest path from source to territory
   * @param parents
   * @param source
   * @param destination
   * @return an arraylist of path territories if path exists, null otherwise
   */
  private ArrayList<Territory> getPathFromSrcToDest(HashMap<Territory, Territory> parents,
                                   Territory source, Territory destination){
    if(!parents.keySet().contains(source) || !parents.keySet().contains(destination)){
      return null;
    }

    ArrayList<Territory> path = new ArrayList<>();
    Territory parent = parents.get(destination);

    // get cost of path from source to destination in reverse
    while(parent != null && parent != source){
      path.add(parent);
      parent = parents.get(parent);
    }
    if(parents == null){
      return null;
    }

    Collections.reverse(path);
    return path;
  }

  /**
   * return the cost to move from source to destination territory,
   * given that the path is already computed
   * @param path
   * @return
   */
  private int getCostFromSrcToDest(ArrayList<Territory> path){
    int cost = 0;

    // remove the source territory size when computing cost
    path.remove(0);
    for(Territory territory: path){
      cost += territory.getSize();
    }
    return cost;
  }
      
  public PathExistMoveChecker(ActionChecker next) {
    super(next);
  }
}
