package edu.duke.ece651.risk.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class RISKMap implements GameMap {
  private final HashSet<Territory> continent;

  public RISKMap(HashSet<Territory> new_continent) {
    this.continent = new_continent;
  }

  /**
   * get the number of continents in the map
   * @return int
   */  
  public int getNumOfContinents() {
    return this.continent.size();
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
    ArrayList<Territory> result = new ArrayList<>(this.continent);
    Collections.sort(result, new ThisIsSerializable());
    return result;
  }
  
  /**
   * get the iterable of territories by owner ID
   * @param id: int
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getTerritoriesByOwnerID(long id) {
    ArrayList<Territory> ownedByMe = new ArrayList<>();
    for (Territory t : continent) {
      if (t.getOwnerID() == id) {
        ownedByMe.add(t);
      }
    }
    Collections.sort(ownedByMe, new ThisIsSerializable());
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

  /**
   * check the validity of path from src to dst
   * @param src (String): path origin
   * @param dst (String): path destination
   * @return pair of territory if exist such path; null if not
   */
  public HashMap<Territory, Territory> getPath(String src, String dst){
    Territory start = getTerritoryByName(src);
    Territory end = getTerritoryByName(dst);
    if (start == null || end == null){
      return null;
    }
    HashSet<Territory> visited = new HashSet<Territory>(); 
    end = dfsToDst(start, dst, visited);
    if (start.equals(end) || end == null){
      return null;
    }
    HashMap<Territory, Territory> ans = new HashMap<Territory, Territory>();
    ans.put(start, end);
    return ans;
  }

  private Territory dfsToDst(Territory curr, String dst, HashSet<Territory> visited){
    if (curr.getName().equals(dst)){
      return getTerritoryByName(dst);
    }
    visited.add(curr);
    Territory end;
    for (Territory t: curr.getNeighbors()){
      if (!visited.contains(t)){
        end = dfsToDst(t, dst, visited);
        if (end!=null){
          return end;
        }
      }
    }
    return null;
  }
}
