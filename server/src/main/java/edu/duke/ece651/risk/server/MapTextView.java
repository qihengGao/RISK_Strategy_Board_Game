package edu.duke.ece651.risk.server;

public class MapTextView {
  
  private final RISKMap toDisplay;

  public MapTextView(RISKMap toDisplay){
    this.toDisplay = toDisplay;
  }

  public String displayMapInit(){
    StringBuilder ans = new StringBuilder(headerBuilder("Initial World Map:\n"));
    for (Territory t : toDisplay.continent.values()){
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
