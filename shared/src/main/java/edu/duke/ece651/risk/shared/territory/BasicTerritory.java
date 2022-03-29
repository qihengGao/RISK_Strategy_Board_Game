package edu.duke.ece651.risk.shared.territory;

// import java.util.HashSet;
import edu.duke.ece651.risk.shared.BattleField;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import edu.duke.ece651.risk.shared.unit.UnitComparator;

import java.util.TreeSet;

/**
 * Basic Territory serves for version1 RISK game.
 */
public class BasicTerritory implements Territory {
  private Long OwnerID;
  private final String name;
  private TreeSet<String> neighbors; // to make in order with repect to name

  public void setUnits(TreeSet<Unit> units) {
    this.units = units;
  }

  private TreeSet<Unit> units;
  private BattleField battleField;
  private int size;
  private int foodResource;
  private int techResource;

  public BasicTerritory(String Name){
    this.OwnerID = -1L;
    this.name = Name;
    this.neighbors = new TreeSet<String>();
    this.units = new TreeSet<Unit>(new UnitComparator());
    this.battleField = new BattleField(this);
    this.size = 1;
    this.foodResource = 10; // predetermined as 10
    this.techResource = 20; // predetermined as 20
  }

  public BasicTerritory(String Name, int size) {
    this(Name);
    this.size = size;
  }

  public BasicTerritory(String name, int size, int foodResource, int techResource) {
    this(name, size);
    this.foodResource = foodResource;
    this.techResource = techResource;
  }

  /**
   * Return the size of the territory.
   * @return int size
   */
  public int getSize() { return this.size; }

  @Override
  public int getFoodResource() {
    return this.foodResource;
  }

  @Override
  public int getTechResource() {
    return this.techResource;
  }

  /**
   * @return the BattleField in this Territory to resolve battles
   */
  public BattleField getBattleField() {
    return this.battleField;
  }

  /**    
   * @return the ID of the Owner of this Territory, such as 1.
   */
  public Long getOwnerID(){
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
   * Try to add the neighbor territory in the territory. True if added successfully.
   * @param toAdd
   * @return
   */
  public boolean tryAddNeighbor(String toAdd){
    if (!name.equals(toAdd)){
      neighbors.add(toAdd);
      return true;
    }
    return false;
  }

  /**
   * Try to add the unit in the territory. True if added successfully.
   * @param toAdd
   * @return
   */
  public boolean tryAddUnit(Unit toAdd) {
    for (Unit u : units){
      if (u.equals(toAdd)){
        u.tryIncreaseAmount(toAdd.getAmount());
        return true;
      }
    }
    this.units.add(toAdd);
    return true;
  }
  
  /**    
   * @return the iterable of all neighbors of this Territory,
   * such as "Gondor", "Oz", "Roshar".
   */
  public Iterable<String> getNeighbors(){
    return neighbors;
  }

  /**
   * Returns the iterable list of units that the territory has.
   */
  public Iterable<Unit> getUnits() {
    return this.units;
  }

  /**
   * search the neighbor by name
   * @return the neighbor: Territory; null if doesn't exist
   */
  public String getNeighborByName(String name){
    for (String t : neighbors) {
      if (t.equals(name)) {
        return t;
      }
    }
    return null;
  }

  /**
   * Get the unit specified by type (version 1)
   * @param type
   */
  @Deprecated
  @Override
  public Unit getUnitByType(String type) {
    return getUnitByTypeLevel(type, 0);
  }

  @Override
  public Unit getUnitByTypeLevel(String type, int level) {
    for (Unit u : this.getUnits()) {
      if (u.getType().equals(type) && u.getLevel() == level) {
        return u;
      }
    }
    return null;
  }

  /**
   * Try to change the ownerID
   * @return true if success
   */
  public boolean tryChangeOwnerTo(Long newID){
    OwnerID = newID;
    return true;
  }

  @Override
  public boolean tryUpgradeUnitToLevel(Unit toUpgrade, int toLevel) {
    if (toLevel <= toUpgrade.getLevel()) {
      return false;
    }
    Unit inTerritory = this.getUnitByTypeLevel(toUpgrade.getType(), toUpgrade.getLevel());
    // Does not have the unit
    if (inTerritory == null) {
      return false;
    }
    // Amount is not sufficient
    if (!inTerritory.tryDecreaseAmount(toUpgrade.getAmount())) {
      return false;
    }
    Unit upgraded = new BasicUnit(toUpgrade.getType(), toUpgrade.getAmount(), toLevel);
    return this.tryAddUnit(upgraded);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BasicTerritory other = (BasicTerritory) obj;
    if (!name.equals(other.name))
      return false;
    return true;
  }

  
}
