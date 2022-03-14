package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class MoveAttackStateTest {
  @Test
  public void test_fillInOrders() throws IOException {
    RISKMap riskMap = buildTestMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);

    //-------------VALID
    //normal
    checkValidMoveOrder(riskMap, output, "Test0,Test1,Unit,10");
    assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 0);
    assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 20);

    //check consequential move orders
    checkValidMoveOrder(riskMap, output,  "Test1,Test2,Unit,20");
    assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 0);
    assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 0);
    assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 30);

    //-------------INVALID
    //check invalid inputs
    checkValidMoveOrder(riskMap, output, "abcdefg");
    assertEquals(bytes.toString(), "Your input abcdefg is not following the format!\nFormat: SourceTerrirotyName,DestTerritoryName,UnitType,UnitAmount\n");
    bytes.reset();

    //check invalid src terr name
    checkValidMoveOrder(riskMap, output, "Zion,Test1,Unit,10");
    assertEquals(bytes.toString(), "Your Source territory does not exist!\n");
    bytes.reset();

    //check invalid dst terr name
    checkValidMoveOrder(riskMap, output, "Test0,IO,Unit,10");
    assertEquals(bytes.toString(), "Your Destination territory does not exist!\n");
    bytes.reset();
    

    //check not owned src terr name
    checkValidMoveOrder(riskMap, output, "Test5,Test1,Unit,10");
    assertEquals(bytes.toString(), "You must operate move orders within your own territories!\n");
    bytes.reset();

    //check not owned dst terr name
    checkValidMoveOrder(riskMap, output, "Test1,Test5,Unit,10");
    assertEquals(bytes.toString(), "You must operate move orders within your own territories!\n");
    bytes.reset();
    
    //check invalid unit name
    checkValidMoveOrder(riskMap, output, "Test0,Test1,Dragon,10");
    assertEquals(bytes.toString(), "The unit you want to move does not exist in your source territory!\n");
    bytes.reset();
    
    //check invalid unit amount
    checkValidMoveOrder(riskMap, output, "Test0,Test1,Unit,huh");
    assertEquals(bytes.toString(), "Unit Amount must be an integer!\n");
    bytes.reset();
    
    //check insufficient unit amount
    checkValidMoveOrder(riskMap, output, "Test0,Test1,Unit,10");
    assertEquals(bytes.toString(), "You do not have sufficient Unit to move in your source territory!\n");
    bytes.reset();
    
    
  }

  private void checkValidMoveOrder(RISKMap riskMap, PrintStream output, String... orderInputs) throws IOException {
    ArrayList<Order> orders = new ArrayList<Order>();
    StringBuilder sb = new StringBuilder();
    for (String orderString : orderInputs){
      sb.append(orderString + "\n");
    }
    sb.append("D\n");
    BufferedReader input = new BufferedReader(new StringReader(sb.toString()));
    orders = MoveAttackState.fillInOrders(riskMap, input, output, 0, "Move");
  }

  /**
  private void displayMap(RISKMap riskMap) {
    TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
    idToColor.put((long)0, new Color("Red"));
    idToColor.put((long)1, new Color("Green"));
    idToColor.put((long)2, new Color("Blue"));
    
    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    System.out.println(mapTextView.displayMap());
  }
  */

  private RISKMap buildTestMap(){
    AbstractMapFactory tmf = new RandomMapFactory();
    RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
    int count = 0;
    for (Territory t: riskMap.getContinent()){
      t.tryAddUnit(new BasicUnit("Unit", 10));
      t.tryChangeOwnerTo(count / 3);
      count++;
    }
    return riskMap;
  }
   

}
