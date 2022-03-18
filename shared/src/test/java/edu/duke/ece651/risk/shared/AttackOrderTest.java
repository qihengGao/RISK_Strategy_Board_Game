package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class AttackOrderTest {

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

    private String checkValidOrder(RISKMap riskMap, long ID, PrintStream output, String userInput) throws IOException {
        String[] inputs = checkFormatAndSplit(userInput);
        int amount = readOrderUnitAmount(inputs);
        Order o = new AttackOrder(ID, inputs[0], inputs[1], inputs[2], amount);
        String check_message = o.executeOrder(riskMap);
        return check_message;
    }

    private int readOrderUnitAmount (String[] inputs){
        int ans;
        try{
            ans = Math.abs(Integer.parseInt(inputs[3]));
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Unit Amount must be an integer!");
        }
        return ans;
    }

    private String[] checkFormatAndSplit(String userInput) throws IllegalArgumentException{
        String[] ans = userInput.split(",");
        if (ans.length != 4) {
            throw new IllegalArgumentException("Your input " + userInput + " is not following the format!");
        }
        return ans;
    }

    @Test
    public void test_executeOrder() throws IOException{
        RISKMap riskMap = buildTestMap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        //-------------VALID
        //normal consequential orders
        displayMap(riskMap);
        String check_message = checkValidOrder(riskMap, 0, output, "Test2,Test3,Unit,10");
        System.out.println(check_message);
        assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 0);
        assertEquals(riskMap.getTerritoryByName("Test3").getUnitByType("Unit").getAmount(), 0);

        displayMap(riskMap);
        String check_message2 = checkValidOrder(riskMap, 0, output, "Test1,Test4,Unit,2");
        System.out.println(check_message2);
        assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 8);
        assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 8);
        displayMap(riskMap);
    }
}