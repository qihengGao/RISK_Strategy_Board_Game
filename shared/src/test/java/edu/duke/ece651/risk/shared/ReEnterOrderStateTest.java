package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class ReEnterOrderStateTest {
    private static void assertEqualsIgnoreLineSeparator(String expected, String actual) {
        assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
                actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
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

    private void displayMap(RISKMap riskMap) {
        System.out.println("Displaying Map");
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
        idToColor.put((long)0, new Color("Red"));
        idToColor.put((long)1, new Color("Green"));
        idToColor.put((long)2, new Color("Blue"));

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        System.out.println(mapTextView.displayMap());
    }


    @Test
    public void test_ReEnterOrder() throws IOException {
        RISKMap riskMap = buildTestMap();
        RISKMap riskMap2 = buildTestMap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        displayMap(riskMap);

        //illegalMoveOrder
        Order illegalMoveOrder = new MoveOrder(0, "Test0", "Test1", "Unit", 10);
        assertEquals(illegalMoveOrder.executeOrder(riskMap), "Move Order path does not exist in your territories!");
        ReEnterOrderState state1 = new ReEnterOrderState(illegalMoveOrder);
        BufferedReader input1 = new BufferedReader(new StringReader("Test0,Test2,Unit,10"));
        Order newMoveOrder = state1.readOrderFromUser(riskMap2, input1 , output, 0, illegalMoveOrder.getOrderType());
        assertNull(newMoveOrder.executeOrder(riskMap));
        displayMap(riskMap);

        //illegalActionOrder
        Order illegalAttackOrder = new AttackOrder(0, "Test0", "Test5", "Unit", 10);
        assertEquals(illegalAttackOrder.executeOrder(riskMap), "You do not have sufficient Unit to move in Test0!");
        ReEnterOrderState state2 = new ReEnterOrderState(illegalAttackOrder);
        BufferedReader input2 = new BufferedReader(new StringReader("Test2,Test6,Unit,11"));
        Order newAttackOrder = state2.readOrderFromUser(riskMap2, input2 , output, 0, illegalAttackOrder.getOrderType());
        assertNull(newAttackOrder.executeOrder(riskMap));
        displayMap(riskMap);

        bytes.reset();
        //illegal and illegal
        ReEnterOrderState state3 = new ReEnterOrderState(illegalMoveOrder);
        BufferedReader input3 = new BufferedReader(new StringReader("alalala\nTest2,Test0,Unit,3\n"));
        Order order = state3.readOrderFromUser(riskMap2, input3 , output, 0, illegalMoveOrder.getOrderType());
        System.out.println(bytes.toString());
        assertNull(order.executeOrder(riskMap));
        displayMap(riskMap);
    }
}