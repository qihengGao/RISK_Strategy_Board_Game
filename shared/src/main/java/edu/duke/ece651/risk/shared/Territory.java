package edu.duke.ece651.risk.shared;

import java.io.Serializable;
import java.util.HashSet;

public interface Territory extends Serializable {
  /**    
   * @return the ID of the Owner of this Territory, such as 1.
   */
  public int getOwnerID();

  /**    
   * @return the name of this Territory, such as "Narnia".
   */
  public String getName();
  
  /**
   * Return the number of curr terrotory's neighbor
   * @return int
   */
  public int getNumOfNeighbors();
  
  /**    
   * @return the iterable of the neighbors of this Territory, 
   * such as "Gondor", "Oz", "Roshar".
   */
  public boolean tryAddNeighbor(Territory toAdd);

  
  /**    
   * @return the iterable of the neighbors of this Territory, 
   * such as "Gondor", "Oz", "Roshar".
   */
  public Iterable<Territory> getNeighbors();

  /**
   * search the neighbor by name
   * @return the neighbor: Territory; null if doesn't exist
   */
  public Territory getNeighborByName(String name);
  
  /**
   * Try to change the ownerID
   * @return true if success
   */
  public boolean tryChangeOwnerTo(int newID);
}
