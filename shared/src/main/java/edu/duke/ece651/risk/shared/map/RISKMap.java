package edu.duke.ece651.risk.shared.map;

import edu.duke.ece651.risk.shared.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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
   * @param owner
   * @return boolean: true if successfully added; false if not
   */
  public void tryAddOwner(Owner owner){
    owners.put(owner.getOwnerId(), owner);
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
    if(this.continent.contains(newTerr)){
      this.continent.remove(newTerr);
    }
    this.continent.add(newTerr);
    return true;
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
    Collections.sort(result, new TerritoryNameComparator());
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
  public void connectTerr(String terr1, String terr2){
    Territory t1 = getTerritoryByName(terr1);
    Territory t2 = getTerritoryByName(terr2);
    t1.tryAddNeighbor(terr2);
    t2.tryAddNeighbor(terr1);
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
    for (String tName: curr.getNeighbors()){
      Territory t = getTerritoryByName(tName);
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
