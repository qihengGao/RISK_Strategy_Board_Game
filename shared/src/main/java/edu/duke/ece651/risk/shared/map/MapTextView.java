package edu.duke.ece651.risk.shared.map;

import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;

import java.util.TreeMap;

public class MapTextView implements MapView{
  
  private final GameMap toDisplay;
  private TreeMap<Long, Color> idToColor;

  public MapTextView(GameMap toDisplay){
    this.toDisplay = toDisplay;
    this.idToColor = new TreeMap<>();
  }

  public MapTextView(GameMap toDisplay, TreeMap<Long, Color> idToColor) {
    this.toDisplay = toDisplay;
    this.idToColor = idToColor;
  }

  /**
   * Display the map at initial phase
   * @return map information in string
   */
  public String displayMapInit(){
    return displayTerrGroup(toDisplay.getContinent(),"Initial World Map:");
  }

  public String displayMap() {
    StringBuilder ans = new StringBuilder();
    for (long id : idToColor.keySet()) {
      Iterable<Territory> territoriesOfId = toDisplay.getTerritoriesByOwnerID(id);
      ans.append(displayTerrGroup(territoriesOfId, idToColor.get(id).getColorName()));
    }
    return ans.toString();
  }

  /**
   * Display a group of territories
   * @param terrGroup: Iterable<Territory>: territories to display 
   * @param groupHeader: String: header of the group
   */
  private String displayTerrGroup(Iterable<Territory> terrGroup, String groupHeader){
    StringBuilder ans = new StringBuilder(headerBuilder(groupHeader));
    for (Territory t : terrGroup){
      ans.append(" " + displayUnitsOf(t) + t.getName()+displayNeighbors(t));
    }
    ans.append("\n");
    return ans.toString();
  }

  private String headerBuilder(String header) {
    StringBuilder ans = new StringBuilder(header);
    ans.append("\n");
    // for (int i = 0; i < header.length(); i++){
    //   ans.append("-");
    // }
    ans.append("-".repeat(header.length()));
    ans.append("\n");
    return ans.toString();
  }
  
  private String displayNeighbors(Territory t){
    StringBuilder ans = new StringBuilder(" (next to:");
    for (String neighbor : t.getNeighbors()){
      ans.append(" "+neighbor + " ,");
    }
    ans.deleteCharAt(ans.length()-1);
    ans.append(")\n");
    return ans.toString();
  }

  private String displayUnitsOf(Territory t) {
    StringBuilder units = new StringBuilder();
    for (Unit unit : t.getUnits()) {
      units.append(unit);
      units.append("s");
      units.append(", ");
    }
    if (units.length() == 0) { // have no units
      return "";
    }
    units.setLength(units.length() - ", ".length()); // remove final ', '
    units.append(" in ");
    return units.toString();
  }

}
