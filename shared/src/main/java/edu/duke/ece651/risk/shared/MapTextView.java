package edu.duke.ece651.risk.shared;

public class MapTextView implements MapView{
  
  private final Map toDisplay;

  public MapTextView(Map toDisplay){
    this.toDisplay = toDisplay;
  }

  /**
   * Display the map at initial phase
   * @return map information in string
   */
  public String displayMapInit(){
    return displayTerrGroup(toDisplay.getContinent(),"Initial World Map:\n");
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
    for (int i = 0; i < header.length(); i++){
      ans.append("-");
    }
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
