package edu.duke.ece651.risk.shared.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.TreeMap;

import edu.duke.ece651.risk.shared.*;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

public class MoveAttackStateTest {
  private static void assertEqualsIgnoreLineSeparator(String expected, String actual) {
    assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
            actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  @Test
  public void test_doAction() throws ClassNotFoundException, IOException{
    ClientContext clientContext = mock(ClientContext.class);
    MoveAttackState thisState = mock(MoveAttackState.class);
    State nextState = mock(ShowGameResultState.class);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
    PrintStream printStream = new PrintStream(System.out);
    RISKMap riskMap = mock(RISKMap.class);
    BufferedReader bufferedReader = mock(BufferedReader.class);
    long playerID = 0L;
    Color color = new Color("red");
    TreeMap<Long, Color> idToColor = new TreeMap<>();
    idToColor.put(playerID, color);


    ArrayList<Order> orders = new ArrayList<>();
    orders.add(new MoveOrder(playerID, "Test1", "Test2", "Soldier", 10));
    doCallRealMethod().when(thisState).doAction(clientContext);
    doReturn(riskMap).when(clientContext).getRiskMap();
    doReturn(bufferedReader).when(clientContext).getBufferedReader();
    doReturn(printStream).when(clientContext).getOut();
    doReturn(playerID).when(clientContext).getPlayerID();
    doReturn(idToColor).when(clientContext).getIdToColor();
    doReturn(objectOutputStream).when(clientContext).getOos();
    doReturn(nextState).when(clientContext).getGameState();

    thisState.doAction(clientContext);

    verify(clientContext, times(1)).getOos();
    verify(clientContext, times(1)).getGameState();
    verify(nextState, times(1)).doAction(clientContext);
  }

  @Test
  public void test_orderPhase() throws IOException{
    MoveAttackState state = mock(MoveAttackState.class);
    RISKMap riskMap = mock(RISKMap.class);
    BufferedReader bufferedReader = mock(BufferedReader.class);
    PrintStream printStream = mock(PrintStream.class);
    long playerID = 0L;
    Color color = new Color("red");
    TreeMap<Long,Color> idToColor = new TreeMap<>();
    idToColor.put(playerID, color);

    doCallRealMethod().when(state).orderPhase(riskMap, bufferedReader, printStream, playerID, idToColor);

    state.orderPhase(riskMap, bufferedReader, printStream, playerID, idToColor);
    verify(state).fillInOrders(bufferedReader, printStream, playerID);
  }

  @Test
  public void test_fillInOrders() throws IOException {
    RISKMap riskMap = buildTestMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    //-------------VALID
    //normal consequential orders

    String check_message = checkValidOrder(riskMap, 0, output, "M\nTest0,Test2,Unit,10",
            "Attack\nTest2,Test3,Unit,20", "A\nTest1,Test4,Unit,2");
    System.out.println(check_message);
    displayMap(riskMap);
    assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 0);
    assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 0);
    assertEquals(riskMap.getTerritoryByName("Test3").getUnitByType("Unit").getAmount(), 10);
    assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 8);
    assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 10);
    displayMap(riskMap);

    checkValidOrder(riskMap, 1, output, "Move\nTest4,Test5,Unit,8");
    assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 2);
    assertEquals(riskMap.getTerritoryByName("Test5").getUnitByType("Unit").getAmount(), 18);
    displayMap(riskMap);

    bytes.reset();
    //-------------INVALID
    //check invalid order choices
    checkValidOrder(riskMap, 0, output, "abcdefg");
    assertEqualsIgnoreLineSeparator(bytes.toString(), "Please enter your action choice: [(M)ove, (A)ttack, (D)one]\n" +
            "Can't find order! Please choose from (M)ove, (A)ttack, (D)one\n" +
            "Please enter your action choice: [(M)ove, (A)ttack, (D)one]\n");
    bytes.reset();

    //check invalid inputs
    checkValidOrder(riskMap, 0, output, "M\nabcdefg");
    assertEqualsIgnoreLineSeparator(bytes.toString(), "Please enter your action choice: [(M)ove, (A)ttack, (D)one]\n" +
            "Action Order Format: (SourceTerritoryName),(DestTerritoryName),(UnitType),(UnitAmount)\n" +
            "Your input abcdefg is not following the format!\n");
    bytes.reset();

    //check invalid src terr name
    String checkNESrc = checkValidOrder(riskMap, 0, output, "M\nZion,Test1,Unit,10");
    assertEqualsIgnoreLineSeparator(checkNESrc, "Your Source territory does not exist!");

    //check invalid dst terr name
    String checkNEDest = checkValidOrder(riskMap, 0, output, "M\nTest0,IO,Unit,10");
    assertEqualsIgnoreLineSeparator(checkNEDest, "Your Destination territory does not exist!");

    //check not owned src terr name
    String checkNOSrc = checkValidOrder(riskMap, 0, output, "M\nTest5,Test1,Unit,10");
    assertEqualsIgnoreLineSeparator(checkNOSrc, "You must place orders from your own territories!");
    bytes.reset();

    //check not owned dst terr name
    String checkNOSDest = checkValidOrder(riskMap, 0, output, "M\nTest1,Test5,Unit,3");
    assertEqualsIgnoreLineSeparator(checkNOSDest, "Move Order path does not exist in your territories!");
    bytes.reset();

    //check invalid unit name
    String checkNEUnit = checkValidOrder(riskMap, 0, output, "M\nTest0,Test1,Dragon,10");
    assertEqualsIgnoreLineSeparator(checkNEUnit, "You do not have Dragon in Test0!");
    bytes.reset();

    //check invalid unit amount
    bytes.reset();
    checkValidOrder(riskMap, 0, output, "Move\nTest0,Test1,Unit,huh");
    assertEqualsIgnoreLineSeparator(bytes.toString(), "Please enter your action choice: [(M)ove, (A)ttack, (D)one]\n" +
            "Action Order Format: (SourceTerritoryName),(DestTerritoryName),(UnitType),(UnitAmount)\n" +
            "Unit Amount must be a positive integer!\n");
    bytes.reset();

    //check negative unit amount
    bytes.reset();
    checkValidOrder(riskMap, 0, output, "Move\nTest0,Test1,Unit,-1");
    assertEqualsIgnoreLineSeparator(bytes.toString(), "Please enter your action choice: [(M)ove, (A)ttack, (D)one]\n" +
            "Action Order Format: (SourceTerritoryName),(DestTerritoryName),(UnitType),(UnitAmount)\n" +
            "Unit Amount must be a positive integer!\n");
    bytes.reset();

    //check insufficient unit amount
    String checkNotEnoughUnit = checkValidOrder(riskMap, 0, output, "M\nTest0,Test2,Unit,100");
    assertEqualsIgnoreLineSeparator(checkNotEnoughUnit, "You do not have sufficient Unit to move in Test0!");
    bytes.reset();


  }

  private String checkValidOrder(RISKMap riskMap, long ID, PrintStream output, String... orderInputs) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (String orderString : orderInputs){
      sb.append(orderString + "\n");
    }
    sb.append("D\n");
    BufferedReader input = new BufferedReader(new StringReader(sb.toString()));
    MoveAttackState moveState = new MoveAttackState();
    String check_message = null;
    for (Order o : moveState.fillInOrders(input, output, ID)){
      if (o.getOrderType().equals("Attack")){
        BattleField BF = riskMap.getTerritoryByName(o.getDestTerritory()).getBattleField();
        BF.setAttackResolver(new SimpleAttackResolver());
        check_message = o.executeOrder(riskMap);
        System.out.println("Attackers(" + o.getPlayerID() + "):" +BF.getAttackers().get(o.getPlayerID()));
        assertEquals(BF.getAttackers().get(o.getPlayerID()).getAmount(), o.getUnitAmount());
      }
      else {
        check_message = o.executeOrder(riskMap);
      }
      if (check_message!=null){
        return check_message;
      }
    }
    return null;
  }

  /**
   * Uncomment to help you write test cases
   * */
  private void displayMap(RISKMap riskMap) {
    TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
    idToColor.put((long)0, new Color("Red"));
    idToColor.put((long)1, new Color("Green"));
    idToColor.put((long)2, new Color("Blue"));

    MapTextView mapTextView = new MapTextView(riskMap, idToColor);
    System.out.println(mapTextView.displayMap());
  }

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
