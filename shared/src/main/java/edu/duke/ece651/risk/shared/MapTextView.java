package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.*;
import java.util.TreeMap;

public class MapTextView implements MapView{
  
  private final Map toDisplay;
  // TODO: Color obj, hashing relationship between id and Color
  // TODO: accomplish in assign phase
  // Color
  // this.name;
  // this.value; 
  // getDisInfo: return value;
  private TreeMap<Integer, Color> idToColor;

  public MapTextView(Map toDisplay){
    this.toDisplay = toDisplay;
    this.idToColor = new TreeMap<>();
  }

  public MapTextView(Map toDisplay, TreeMap<Integer, Color> idToColor) {
    this.toDisplay = toDisplay;
    this.idToColor = idToColor;
  }

  /**
   * Display the map at initial phase
   * @return map information in string
   */
  public String displayMapInit(){
    return displayTerrGroup(toDisplay.getContinent(),"Initial World Map:\n");
  }

  public String displayMap() {
    StringBuilder ans = new StringBuilder();
    for (int id : idToColor.keySet()) {
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
      ans.append(" "+t.getName()+displayNeighbors(t));
    }
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
    for (Territory neighbor : t.getNeighbors()){
      ans.append(" "+neighbor.getName()+",");
    }
    ans.deleteCharAt(ans.length()-1);
    ans.append(")\n");
    return ans.toString();
  }

}
