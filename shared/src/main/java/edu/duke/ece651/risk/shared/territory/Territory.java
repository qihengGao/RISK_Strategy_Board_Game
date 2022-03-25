package edu.duke.ece651.risk.shared.territory;

import edu.duke.ece651.risk.shared.BattleField;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.Serializable;

public interface Territory extends Serializable {
  /**
   * @return the BattleField in this Territory to resolve battles
   */
  public BattleField getBattleField();

  /**    
   * @return the ID of the Owner of this Territory, such as 1.
   */
  public Long getOwnerID();

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
   * Try to add the neighbor territory in the territory. True if added successfully.
   * @param toAdd
   * @return
   */
  public boolean tryAddNeighbor(String toAdd);

  
  /**    
   * @return the iterable of the neighbors of this Territory, 
   * such as "Gondor", "Oz", "Roshar".
   */
  public Iterable<String> getNeighbors();

  /**
   * search the neighbor by name
   * @return the neighbor: Territory; null if doesn't exist
   */
  public String getNeighborByName(String name);
  
  /**
   * Try to change the ownerID
   * @return true if success
   */
  public boolean tryChangeOwnerTo(Long newID);
  
  /**
   * Returns the iterable list of units that the territory has.
   */
  public Iterable<Unit> getUnits();
  
  /**
   * Try to add the unit in the territory. True if added successfully.
   * @param toAdd
   */
  public boolean tryAddUnit(Unit toAdd);

  /**
   * Get the unit specified by type
   * @param type
   */
  public Unit getUnitByType(String type);
}