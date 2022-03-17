package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.map.GameMap;

public interface AbstractMapFactory {
  /**
   * Create random map for specific number of players
   * @param n: int: number of players
   * @return
   */
  public GameMap createMapForNplayers(int n);
}
