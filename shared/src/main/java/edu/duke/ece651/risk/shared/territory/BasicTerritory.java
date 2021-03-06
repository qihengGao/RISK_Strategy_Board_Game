package edu.duke.ece651.risk.shared.territory;

// import java.util.HashSet;
import edu.duke.ece651.risk.shared.battle.BattleField;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import edu.duke.ece651.risk.shared.unit.UnitComparator;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * Basic Territory serves for version1 RISK game.
 */
public class BasicTerritory implements Territory {
  private Long OwnerID;
  private  String name;
  private TreeSet<String> neighbors; // to make in order with repect to name

  private HashSet<Unit> units;


  private BattleField battleField;
  private int size;
  private int foodProduction;
  private int techProduction;

  public BasicTerritory() {
  }

  //ctors
  public BasicTerritory(String Name){
    this.OwnerID = -1L;
    this.name = Name;
    this.neighbors = new TreeSet<String>();
    this.units = new HashSet<Unit>();
    this.battleField = new BattleField(this);
    this.size = 1;
    this.foodProduction = 20; // predetermined as 20
    this.techProduction = 30; // predetermined as 30
  }

  public BasicTerritory(String Name, int size) {
    this(Name);
    this.size = size;
  }

  public BasicTerritory(String name, int size, int foodProduction, int techProduction) {
    this(name, size);
    this.foodProduction = foodProduction;
    this.techProduction = techProduction;
  }


  /**
   * set the units in this territory
   * @param unitsToAdd
   */
  public void setUnits(TreeSet<Unit> unitsToAdd) {
    this.units.clear();
    for (Unit u: unitsToAdd){
      this.units.add(u);
    }
  }


  /**
   * Return the size of the territory.
   * @return int size
   */
  public int getSize() { return this.size; }

  /**
   * increase the size of this territory
   * @param toIncrease
   */
  public void increaseSize(int toIncrease){
    this.size+=toIncrease;
  }

  @Override
  public int getFoodProduction() {
    return this.foodProduction;
  }

  @Override
  public int getTechProduction() {
    return this.techProduction;
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
   * @return
   */
  public HashSet<Unit> getUnits() {
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
   * Get the unit specified by type
   * @param type
   */

  @Override
  public Unit getUnitByType(String type) {
    return getUnitByTypeAndID(type, this.getOwnerID());
  }

  /**
   * get the unit by type and level
   * @param type
   * @param level
   * @return
   */
  @Override
  public Unit getUnitByTypeLevel(String type, int level) {
    return getUnitByTypeLevelID(type, level, this.getOwnerID());
  }

  /**
   * Try to change the ownerID
   * @return true if success
   */
  public boolean tryChangeOwnerTo(Long newID){
    OwnerID = newID;
    return true;
  }


  /**
   * try to upgrade a unit to level
   * @param toUpgrade
   * @param toLevel
   * @return
   */
  @Override
  public String tryUpgradeUnitToLevel(Unit toUpgrade, int toLevel) {
    if (toLevel <= toUpgrade.getLevel()) {
      return toUpgrade.getType() + " level " + toUpgrade.getLevel() + " cannot upgrade to level " + toLevel;
    }
    Unit inTerritory = this.getUnitByTypeLevel(toUpgrade.getType(), toUpgrade.getLevel());
    // Does not have the unit
    if (inTerritory == null) {
      return "Current territory does not have the level " + toUpgrade.getLevel() + " " + toUpgrade.getType();
    }
    // Amount is not sufficient
    if (!inTerritory.tryDecreaseAmount(toUpgrade.getAmount())) {
      return "Unit amount is not sufficient to do the upgrade";
    }
    Unit upgraded = new BasicUnit(toUpgrade.getType(), toUpgrade.getAmount(), toLevel);
    upgraded.setOwnerId(this.getOwnerID());
    this.tryAddUnit(upgraded);
    return null;
  }

  @Override
  public Unit getUnitByTypeAndID(String type, long ownerId) {
    String[] info = type.split(" ");
    if (info.length == 1) { // only contains type, version 1
      return getUnitByTypeLevelID(type, 0, ownerId);
    } else { // compatible for version2, Soldier level 6
      return getUnitByTypeLevelID(info[0], Integer.parseInt(info[2]), ownerId);
    }
  }

  @Override
  public Unit getUnitByTypeLevelID(String type, int level, long ownerId) {
    for (Unit u : this.getUnits()) {
      if (u.getType().equals(type) && u.getLevel() == level && u.getOwnerId() == ownerId) {
        return u;
      }
    }
    return null;
  }

  /**
   * overrode hashcode
   * @return
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    return result;
  }

  /**
   * overrode equals
   * @return
   */
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
