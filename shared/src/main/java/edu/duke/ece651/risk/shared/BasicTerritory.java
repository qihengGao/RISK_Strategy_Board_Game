package edu.duke.ece651.risk.shared;

// import java.util.HashSet;
import java.util.TreeSet;

/**
 * Basic Territory serves for version1 RISK game.
 */
public class BasicTerritory implements Territory {
  private int OwnerID;
  private final String name;
  private final TreeSet<Territory> neighbors; // to make in order with repect to name
  
  public BasicTerritory(String Name){
    this.OwnerID = -1;
    this.name = Name;
    this.neighbors = new TreeSet<Territory>((t1, t2) -> t1.getName().compareTo(t2.getName()));
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
   * Return the number of neighbors.
   */
  public int getNumOfNeighbors() {
    return this.neighbors.size();
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
  public Iterable<Territory> getNeighbors(){
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
