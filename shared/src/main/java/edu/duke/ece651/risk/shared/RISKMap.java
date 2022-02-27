package edu.duke.ece651.risk.shared;

import java.util.HashSet;
import java.util.TreeMap;

public class RISKMap {
  TreeMap<String, Territory> continent;
  
  public RISKMap(TreeMap<String, Territory> new_continent){
    this.continent = new_continent;
  }

  public boolean tryAddTerritory(Territory newTerr) {
    continent.put(newTerr.getName(), newTerr);
    return true;
  }

  public Territory getTerritoryByName(String name) {
    return continent.get(name);
  }

  public Iterable<Territory> getTerritoriesByOwnerID(int id) {
    HashSet<Territory> ownedByMe = new HashSet<Territory>();
    for (Territory t : continent.values()){
      if (t.getOwnerID() == id){
        ownedByMe.add(t);
      }
    }
    return ownedByMe;
  }

}
