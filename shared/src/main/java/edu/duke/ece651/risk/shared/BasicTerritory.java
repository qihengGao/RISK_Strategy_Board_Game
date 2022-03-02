package edu.duke.ece651.risk.shared;

import java.util.HashSet;

public class BasicTerritory implements Territory {
  private int OwnerID;
  private final String name;
  private final HashSet<Territory> neighbors;
  
  public BasicTerritory(String Name){
    this.OwnerID = 0;
    this.name = Name;
    this.neighbors = new HashSet<Territory>();
  }

  /**    
   * @return the ID of the Owner of this Territory, such as 1.
   */
  public int getOwnerID(){
    return OwnerID;
  }
  
  /**    
   * @return the name of this Territory, such as "Narnia".
   */
  public String getName(){
    return name;
  }

  /**    
   * @return the iterable of the neighbors of this Territory, 
   * such as "Gondor", "Oz", "Roshar".
   */
  public boolean tryAddNeighbor(Territory toAdd){
    if (!name.equals(toAdd.getName())){
      neighbors.add(toAdd);
      return true;
    }
    return false;
  }
  
  /**    
   * @return the iterable of the neighbors of this Territory, 
   * such as "Gondor", "Oz", "Roshar".
   */
  public HashSet<Territory> getNeighbors(){
    return neighbors;
  }

  /**
   * search the neighbor by name
   * @return the neighbor: Territory; null if doesn't exist
   */
  public Territory getNeighborByName(String name){
    for (Territory t : neighbors) {
      if (t.getName().equals(name)) {
        return t;
      }
    }
    return null;
  }

  /**
   * Try to change the ownerID
   * @return true if success
   */
  public boolean tryChangeOwnerTo(int newID){
    OwnerID = newID;
    return true;
  }
}
