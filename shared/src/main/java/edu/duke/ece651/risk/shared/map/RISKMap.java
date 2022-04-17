package edu.duke.ece651.risk.shared.map;

import edu.duke.ece651.risk.shared.checker.PathResourceMoveChecker;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class RISKMap implements GameMap {
  private final HashSet<Territory> continent;

  public HashMap<Long, Owner> getOwners() {
    return owners;
  }

  private final HashMap<Long, Owner> owners;

  public RISKMap(HashSet<Territory> new_continent) {
    this.continent = new_continent;
    this.owners = new HashMap<Long, Owner>();
  }

  /**
   * try to add a new owner to save player's tech level, total resources, etc.
   *
   * @param owner
   * @return boolean: true if successfully added; false if not
   */
  public void tryAddOwner(Owner owner) {
    owners.put(owner.getOwnerId(), owner);
  }

  /**
   * get the number of continents in the map
   *
   * @return int
   */
  public int getNumOfContinents() {
    return this.continent.size();
  }

  /**
   * Try to add territory into continent
   *
   * @return true if succeed
   */
  public boolean tryAddTerritory(Territory newTerr) {
    if (this.continent.contains(newTerr)) {
      this.continent.remove(newTerr);
    }
    this.continent.add(newTerr);
    return true;
  }

  /**
   * get the territory object by the name of territory
   *
   * @param name: String
   * @return territory if exist, null if not
   */
  public Territory getTerritoryByName(String name) {
    for (Territory t : continent) {
      if (t.getName().equals(name)) {
        return t;
      }
    }
    return null;
  }

  /**
   * get the iterable of all territories in continent
   *
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getContinent() {
    ArrayList<Territory> result = new ArrayList<>(this.continent);
    Collections.sort(result, new TerritoryNameComparator());
    return result;
  }

  /**
   * get the iterable of territories by owner ID
   *
   * @param id: int
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getTerritoriesByOwnerID(long id) {
    ArrayList<Territory> ownedByMe = new ArrayList<>();
    for (Territory t : continent) {
      if (t.getOwnerID().equals(id)) {
        ownedByMe.add(t);
      }
    }
    Collections.sort(ownedByMe, new TerritoryNameComparator());
    return ownedByMe;
  }

  /**
   * Add two territories as neighbors
   */
  public void connectTerr(String terr1, String terr2) {
    Territory t1 = getTerritoryByName(terr1);
    Territory t2 = getTerritoryByName(terr2);
    t1.tryAddNeighbor(terr2);
    t2.tryAddNeighbor(terr1);
  }

  public void handleBreakAlliance(Long ownerID1, Long ownerID2) {
    for (Territory t : continent) {
      terrBreakAlliance(ownerID1, ownerID2, t);
    }
    this.owners.get(ownerID1).breakAlliance(ownerID2);
    this.owners.get(ownerID2).breakAlliance(ownerID1);
  }

  public void terrBreakAlliance(Long ownerID1, Long ownerID2, Territory t) {
    if (!t.getOwnerID().equals(ownerID1) && !t.getOwnerID().equals(ownerID2)){
      return;
    }
    Long guest = ownerID2;
    if (t.getOwnerID().equals(ownerID2)){
      guest = ownerID1;
    }

    for (Unit u : t.getUnits()) {
      if (u.getOwnerId()==guest){
        HashMap<Territory, Integer> distance = doDijkstra(t);
        Territory closestMyTerr = getMyClosestTerr(distance, guest);
        //move unit u from t to closestTerr
        Unit toMove = new BasicUnit(u.getType(), u.getAmount());
        u.tryDecreaseAmount(u.getAmount());
        toMove.setOwnerId(guest);
        closestMyTerr.tryAddUnit(toMove);
      }
    }
  }

  public Territory getMyClosestTerr(HashMap<Territory, Integer> distance, Long myID) {
    int minimumDistance = Integer.MAX_VALUE;
    Territory toReturn = null;

    for (Territory territory : distance.keySet()) {
      if (distance.get(territory) < minimumDistance && territory.getOwnerID().equals(myID)) {
        toReturn = territory;
        minimumDistance = distance.get(territory);
      }
    }

    return toReturn;
  }


  /**
   * helper method to use djikstra's algorithm
   * to get each territory's parent along the shortest path
   *
   * @return hashmap for each territory's distance from source
   */
  public HashMap<Territory, Integer> doDijkstra(Territory source) {
    // a hashmap to store distance from source to the territory, initalize to infinity
    HashMap<Territory, Integer> distances = new HashMap<Territory, Integer>();
    for (Territory territory : continent) {
      distances.put(territory, Integer.MAX_VALUE);
    }

    // set the distance from source -> source to 0
    distances.put(source, 0);

    //build a set of unsettled territory
    ArrayList<Territory> queue = (ArrayList<Territory>) this.getContinent();

    while (!queue.isEmpty()) {
      Territory currTerritory = getMinimumDistanceTerritory(distances, queue);
      queue.remove(currTerritory);

      for (String neighborName : currTerritory.getNeighbors()) {
        Territory neighbor = this.getTerritoryByName(neighborName);
        if (distances.get(currTerritory) + neighbor.getSize() < distances.get(neighbor)) {
          distances.put(neighbor, distances.get(currTerritory) + neighbor.getSize());
        }
      }
    }
    return distances;
  }

  /**
   * //   * helper method to get the next node to compute path
   * //   * @param distances
   * //   * @param queue
   * //   * @return the node to continuing computing path
   * //
   */
  private Territory getMinimumDistanceTerritory(
          HashMap<Territory, Integer> distances, ArrayList<Territory> queue) {
    int minimumDistance = Integer.MAX_VALUE;
    Territory toReturn = null;

    for (Territory territory : distances.keySet()) {
      if (distances.get(territory) < minimumDistance && queue.contains(territory)) {
        toReturn = territory;
        minimumDistance = distances.get(territory);
      }
    }
    return toReturn;
  }
}
