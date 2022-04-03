package edu.duke.ece651.risk.shared.state;

import edu.duke.ece651.risk.shared.*;
import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.AttackOrder;
import edu.duke.ece651.risk.shared.order.MoveOrder;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReEnterOrderStateTest {
    // private static void assertEqualsIgnoreLineSeparator(String expected, String actual) {
    //     assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
    //             actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
    // }
//
//    private RISKMap buildTestMap(){
//        AbstractMapFactory tmf = new RandomMapFactory();
//        RISKMap riskMap = (RISKMap) tmf.createMapForNplayers(3);
//        int count = 0;
//        for (Territory t: riskMap.getContinent()){
//            t.tryAddUnit(new BasicUnit("Unit", 10));
//            t.tryChangeOwnerTo((long) (count / 3));
//            count++;
//        }
//        return riskMap;
//    }
//
//    private void displayMap(RISKMap riskMap) {
//        System.out.println("Displaying Map");
//        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
//        idToColor.put((long)0, new Color("Red"));
//        idToColor.put((long)1, new Color("Green"));
//        idToColor.put((long)2, new Color("Blue"));
//
//        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
//        System.out.println(mapTextView.displayMap());
//    }
//
//    @Test
//    public void test_doAction() throws ClassNotFoundException, IOException{
//        ClientContext clientContext = mock(ClientContext.class);
//        Order illegalOrder = mock(MoveOrder.class);
//        ReEnterOrderState thisState = spy(new ReEnterOrderState(illegalOrder));
//        State nextState = mock(ShowGameResultState.class);
//
//        doCallRealMethod().when(thisState).doAction(clientContext);
//        doReturn("").when(illegalOrder).executeOrder(any());
//        doReturn(illegalOrder).when(thisState).readOrderFromUser(any(),any(),any(),any(),any());
//        doReturn(nextState).when(clientContext).getGameState();
//
//        thisState.doAction(clientContext);
//
//        verify(nextState, times(1)).doAction(clientContext);
//    }
//
//    @Test
//    public void test_readOrderFromUser_nullInput() throws IOException{
//        RISKMap riskMap = this.buildTestMap();
//        BufferedReader bufferedReader = new BufferedReader(new StringReader("\n"));
//
//        Order illegalMoveOrder = new MoveOrder(0, "Test0", "Test1", "Unit", 10);
//        ReEnterOrderState thisState = new ReEnterOrderState(illegalMoveOrder);
//        Order outputOrder = thisState.readOrderFromUser(riskMap, bufferedReader, System.out, 0L, illegalMoveOrder.getOrderType());
//        assertNull(outputOrder);
//    }
//
//    @Test
//    public void test_readOrderFromUser_checkMesageNull() throws IOException{
//        RISKMap riskMap = mock(RISKMap.class);
//        BufferedReader bufferedReader = mock(BufferedReader.class);
//        PrintStream printStream = mock(PrintStream.class);
//        Long id = 0L;
//        String chosenOrder = "chosenOrder";
//        Order illegalMoveOrder = new MoveOrder(0, "Test0", "Test1", "Unit", 10);
//
//        //ReEnterOrderState thisState = spy(new ReEnterOrderState(illegalMoveOrder));
//        ReEnterOrderState thisState = mock(ReEnterOrderState.class);
////        String list[] = new String[4];
////        list[0] = "1";
////        list[1] = "1";
////        list[2] = "1";
////        list[3] = "1";
////        doReturn(list).when(thisState).checkFormatAndSplit(anyString());
//
//        MoveOrder moveOrder = mock(MoveOrder.class);
//        doReturn(moveOrder).when(thisState).getOrder(any(), any(), any());
//
//        doCallRealMethod().when(thisState).readOrderFromUser(riskMap, bufferedReader, printStream, id, chosenOrder);
//        doReturn(" ").when(bufferedReader).readLine();
//
//        // doThrow(IllegalArgumentException.class).when(thisState).excuteOrder(any(), any());
//
//        doReturn(chosenOrder).doReturn(null).when(thisState).excuteOrder(any(), any());
//
//
//        thisState.readOrderFromUser(riskMap, bufferedReader, printStream, id, chosenOrder);
//        // verify(printStream).println();
//    }
//
//    @Test
//    public void test_ReEnterOrder() throws IOException {
//        RISKMap riskMap = buildTestMap();
//        RISKMap riskMap2 = buildTestMap();
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        PrintStream output = new PrintStream(bytes, true);
//        displayMap(riskMap);
//
//        //illegalMoveOrder
//        Order illegalMoveOrder = new MoveOrder(0, "Test0", "Test1", "Unit", 10);
//        assertEquals(illegalMoveOrder.executeOrder(riskMap), "Move Order path does not exist in your territories!");
//        ReEnterOrderState state1 = new ReEnterOrderState(illegalMoveOrder);
//        BufferedReader input1 = new BufferedReader(new StringReader("Test0,Test2,Unit,10"));
//        Order newMoveOrder = state1.readOrderFromUser(riskMap2, input1 , output, 0L, illegalMoveOrder.getOrderType());
//        assertNull(newMoveOrder.executeOrder(riskMap));
//        displayMap(riskMap);
//
//        //illegalActionOrder
//        Order illegalAttackOrder = new AttackOrder(0, "Test0", "Test5", "Unit", 10);
//        assertEquals(illegalAttackOrder.executeOrder(riskMap), "You do not have sufficient Unit to move in Test0!");
//        ReEnterOrderState state2 = new ReEnterOrderState(illegalAttackOrder);
//        BufferedReader input2 = new BufferedReader(new StringReader("Test2,Test6,Unit,11"));
//        Order newAttackOrder = state2.readOrderFromUser(riskMap2, input2 , output, 0L, illegalAttackOrder.getOrderType());
//        assertNull(newAttackOrder.executeOrder(riskMap));
//        displayMap(riskMap);
//
//        bytes.reset();
//        //illegal and illegal
//        ReEnterOrderState state3 = new ReEnterOrderState(illegalMoveOrder);
//        BufferedReader input3 = new BufferedReader(new StringReader("alalala\nTest2,Test0,Unit,3\n"));
//        Order order = state3.readOrderFromUser(riskMap2, input3 , output, 0L, illegalMoveOrder.getOrderType());
//        System.out.println(bytes.toString());
//        displayMap(riskMap);
//    }
//
//    @Test
//    public void test_readOrderUnitAmount(){
//        Order illegalMoveOrder = new MoveOrder(0, "Test0", "Test1", "Unit", 10);
//        ReEnterOrderState thisState = new ReEnterOrderState(illegalMoveOrder);
//
//        String[] inputArr = {"Test0", "Test1", "Unit", "x"};
//        assertThrows(IllegalArgumentException.class, ()->thisState.readOrderUnitAmount(inputArr));
//
//    }
}
