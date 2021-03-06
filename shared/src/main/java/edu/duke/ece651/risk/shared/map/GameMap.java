package edu.duke.ece651.risk.shared.map;

import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.io.Serializable;
import java.util.HashMap;

public interface GameMap extends Serializable, Cloneable{
  /**
   * get the number of continents in the map
   * @return int
   */

  public int getNumOfContinents();

  /**
   * try to add a new owner to save player's tech level, total resources, etc.
   * @param owner
   * @return void:
   */
  public void tryAddOwner(Owner owner);
  
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
  public Iterable<Territory> getTerritoriesByOwnerID(long id);

  /**
   * Add two territories as neighbors
   */
  public void connectTerr(String terr1, String terr2);

//  /**
//   * check the validity of path from src to dst
//   * @param src (String): path origin
//   * @param dst (String): path destination
//   * @return pair of territory if exist such path; null if not
//   */
//  public HashMap<Territory, Territory> getPath(String src, String dst);
}
