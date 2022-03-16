package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class AttackOrderTest {
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
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
        idToColor.put((long)0, new Color("Red"));
        idToColor.put((long)1, new Color("Green"));
        idToColor.put((long)2, new Color("Blue"));

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        System.out.println(mapTextView.displayMap());
    }

    private String checkValidOrder(RISKMap riskMap, long ID, PrintStream output, String... orderInputs) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String orderString : orderInputs){
            sb.append(orderString + "\n");
        }
        sb.append("D\n");
        BufferedReader input = new BufferedReader(new StringReader(sb.toString()));
        MoveAttackState moveState = new MoveAttackState();
        for (Order o : moveState.fillInOrders(input, output, ID)){
            String check_message = o.executeOrder(riskMap);
            if (check_message!=null){
                return check_message;
            }
        }
        return null;
    }

    @Test
    public void test_executeOrder() throws IOException{
        RISKMap riskMap = buildTestMap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        //-------------VALID
        //normal consequential orders

        String check_message = checkValidOrder(riskMap, 0, output, "M\nTest0,Test2,Unit,10",
                "A\nTest2,Test3,Unit,20", "A\nTest1,Test4,Unit,2");
        System.out.println(check_message);
        assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 0);
        assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 0);
        assertEquals(riskMap.getTerritoryByName("Test3").getUnitByType("Unit").getAmount(), 10);
        assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 8);
        assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 8);
        displayMap(riskMap);
    }
}