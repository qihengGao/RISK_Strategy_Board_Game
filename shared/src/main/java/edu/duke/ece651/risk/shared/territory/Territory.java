package edu.duke.ece651.risk.shared.territory;

import edu.duke.ece651.risk.shared.battle.BattleField;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.TreeSet;

public interface Territory extends Serializable {
  /**
   * @param units to be set
   */
  public void setUnits(TreeSet<Unit> units);

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
   * increase the size of this territory
   * @param toIncrease
   */
  public void increaseSize(int toIncrease);

  /**
   * Return the number of curr terrotory's neighbor
   * @return int
   */
  public int getNumOfNeighbors();

  /**
   * Return the size of territory.
   * @return
   */
  public int getSize();

  /**
   * Return the food resource generated on the territory.
   * @return
   */
  public int getFoodProduction();

  /**
   * Return the tech resource generated on the territory.
   * @return
   */
  public int getTechProduction();

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
   * @return
   */
  public HashSet<Unit> getUnits();
  
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

  /**
   * Get the unit specified by type and level
   * @param type
   * @param level
   * @return
   */
  public Unit getUnitByTypeLevel(String type, int level);

  /**
   * Try upgrade the unit specified in the  toUpgrade {type, amount, level} to the toLevel\
   * return null if successfully
   * @param toUpgrade
   * @param toLevel
   * @return
   */
  public String tryUpgradeUnitToLevel(Unit toUpgrade, int toLevel);

  public Unit getUnitByTypeAndID(String type, long ownerId);

  public Unit getUnitByTypeLevelID(String type, int level, long ownerId);
}
