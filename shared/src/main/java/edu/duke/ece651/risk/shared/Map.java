package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public interface Map extends Serializable{

  /**
   * get the iterable of all territories in continent
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getContinent();

  /**
   * Try to add territory into continent
   * @return true if succeed
   */
  public boolean tryAddTerritory(Territory newTerr);

  /**
   * get the territory object by the name of territory
   * @param name: String
   * @return boolean
   */
  public Territory getTerritoryByName(String name);

  /**
   * get the iterable of territories by owner ID
   * @param id: int
   * @return Iterable<Territory>
   */
  public Iterable<Territory> getTerritoriesByOwnerID(int id);

  /**
   * Add two territories as neighbors
   */
  public void connectTerr(String terr1, String terr2);
}
