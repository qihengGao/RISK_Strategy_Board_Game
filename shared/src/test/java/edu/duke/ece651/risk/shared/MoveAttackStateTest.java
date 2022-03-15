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
    //normal consequential orders
    checkValidMoveOrder(riskMap, 0, output, "Test0,Test2,Unit,10", "Test2,Test0,Unit,20");
    assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 20);
    assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 0);

    checkValidMoveOrder(riskMap, 1, output, "Test3,Test4,Unit,2", "Test4,Test5,Unit,12");
    assertEquals(riskMap.getTerritoryByName("Test3").getUnitByType("Unit").getAmount(), 8);
    assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 0);
    assertEquals(riskMap.getTerritoryByName("Test5").getUnitByType("Unit").getAmount(), 22);

    bytes.reset();
    //-------------INVALID
    //check invalid inputs
    checkValidMoveOrder(riskMap, 0, output, "abcdefg");
    assertEquals(bytes.toString(), "Your input abcdefg is not following the format!\n");
    bytes.reset();

    //check invalid src terr name
    checkValidMoveOrder(riskMap, 0, output, "Zion,Test1,Unit,10");
    assertEquals(bytes.toString(), "Your Source territory does not exist!\n");
    bytes.reset();

    //check invalid dst terr name
    checkValidMoveOrder(riskMap, 0, output, "Test0,IO,Unit,10");
    assertEquals(bytes.toString(), "Your Destination territory does not exist!\n");
    bytes.reset();
    

    //check not owned src terr name
    checkValidMoveOrder(riskMap, 0, output, "Test5,Test1,Unit,10");
    assertEquals(bytes.toString(), "You must place orders from your own territories!\n");
    bytes.reset();

    //check not owned dst terr name
    checkValidMoveOrder(riskMap, 0, output, "Test1,Test5,Unit,10");
    assertEquals(bytes.toString(), "Move Order path does not exist in your territories!\n");
    bytes.reset();
    
    //check invalid unit name
    checkValidMoveOrder(riskMap, 0, output, "Test0,Test1,Dragon,10");
    assertEquals(bytes.toString(), "You do not have Dragon in Test0!\n");
    bytes.reset();
    
    //check invalid unit amount
    checkValidMoveOrder(riskMap, 0, output, "Test0,Test1,Unit,huh");
    assertEquals(bytes.toString(), "Unit Amount must be an integer!\n");
    bytes.reset();

    //check negative unit amount
    checkValidMoveOrder(riskMap, 0, output, "Test0,Test1,Unit,-1");
    assertEquals(bytes.toString(), "Unit Amount must be positive integer!\n");
    bytes.reset();
    
    //check insufficient unit amount
    checkValidMoveOrder(riskMap, 0, output, "Test0,Test2,Unit,100");
    assertEquals(bytes.toString(), "You do not have sufficient Unit to move in Test0!\n");
    bytes.reset();
    
    
  }

  private void checkValidMoveOrder(RISKMap riskMap, long ID, PrintStream output, String... orderInputs) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (String orderString : orderInputs){
      sb.append(orderString + "\n");
    }
    sb.append("D\n");
    BufferedReader input = new BufferedReader(new StringReader(sb.toString()));
    MoveAttackState moveState = new MoveAttackState("Move");
    moveState.fillInOrders(riskMap, input, output, ID, "Move");
  }

  /**
   * Uncomment to help you write test cases
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
      t.tryChangeOwnerTo((long) (count / 3));
      count++;
    }
    return riskMap;
  }
   

}
