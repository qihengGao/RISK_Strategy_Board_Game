package edu.duke.ece651.risk.shared;

// import java.util.HashSet;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.util.TreeSet;

/**
 * Basic Territory serves for version1 RISK game.
 */
public class BasicTerritory implements Territory {
  private Long OwnerID;
  private final String name;
  private TreeSet<String> neighbors; // to make in order with repect to name
  private TreeSet<Unit> units;
  private BattleField battleField;

  public BasicTerritory(String Name){
    this.OwnerID = -1L;
    this.name = Name;
    this.neighbors = new TreeSet<String>();
    this.units = new TreeSet<Unit>(new unitCompactor());
    this.battleField = new BattleField(this);
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
   * Get the unit specified by type
   * @param type
   */
  @Override
  public Unit getUnitByType(String type) {
    for (Unit u : this.getUnits()) {
      if (u.getType().equals(type)) {
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
