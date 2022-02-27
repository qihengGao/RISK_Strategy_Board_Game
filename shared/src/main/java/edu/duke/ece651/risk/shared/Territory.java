package edu.duke.ece651.risk.shared;

import java.util.HashSet;

public class Territory {
  int OwnerID;
  final String name;
  final HashSet<Territory> neighbors;
  
  public Territory(String Name){
    this.OwnerID = 0;
    this.name = Name;
    this.neighbors = new HashSet<Territory> ();
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
  public void tryAddNeighbor(Territory neighbor){
    neighbors.add(neighbor);
    neighbor.neighbors.add(this);
  }
  
  /**    
   * @return the iterable of the neighbors of this Territory, 
   * such as "Gondor", "Oz", "Roshar".
   */
  public HashSet<Territory> getNeighbors(){
    return neighbors;
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
