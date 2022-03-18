package edu.duke.ece651.risk.shared.map;

public interface MapView {
  /**
   * Display the map at initial phase
   * @return map information in string
   */
  public String displayMapInit();

  /**
   * Display the map
   * @return map information in string
   */
  public String displayMap();
}
