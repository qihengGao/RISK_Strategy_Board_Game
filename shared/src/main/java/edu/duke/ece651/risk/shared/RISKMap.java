package edu.duke.ece651.risk.shared;

import java.util.HashSet;

public class RISKMap implements Map {
  private final HashSet<Territory> continent;

  public RISKMap(HashSet<Territory> new_continent) {
    this.continent = new_continent;
  }

  /**
   * Try to add territory into continent
   * @return true if succeed
   */
  public boolean tryAddTerritory(Territory newTerr) {
    if (getTerritoryByName(newTerr.getName())==null){
      continent.add(newTerr);
      return true;
    }
    return false;
  }

  /**
   * get the territory object by the name of territory
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
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getContinent() {
    return continent;
  }
  
  /**
   * get the iterable of territories by owner ID
   * @param id: int
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getTerritoriesByOwnerID(int id) {
    HashSet<Territory> ownedByMe = new HashSet<Territory>();
    for (Territory t : continent) {
      if (t.getOwnerID() == id) {
        ownedByMe.add(t);
      }
    }
    return ownedByMe;
  }

  /**
   * Add two territories as neighbors
   */
  public void connectTerr(String terr1, String terr2){
    Territory t1 = getTerritoryByName(terr1);
    Territory t2 = getTerritoryByName(terr2);
    t1.tryAddNeighbor(t2);
    t2.tryAddNeighbor(t1);
  }
}
