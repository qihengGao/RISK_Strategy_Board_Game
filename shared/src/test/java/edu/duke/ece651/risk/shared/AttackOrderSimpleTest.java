package edu.duke.ece651.risk.shared;

        import edu.duke.ece651.risk.shared.factory.AbstractMapFactory;
        import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
        import edu.duke.ece651.risk.shared.map.MapTextView;
        import edu.duke.ece651.risk.shared.map.RISKMap;
        import edu.duke.ece651.risk.shared.unit.BasicUnit;
        import org.junit.jupiter.api.Test;

        import java.io.*;
        import java.util.TreeMap;

        import static org.junit.jupiter.api.Assertions.*;

public class AttackOrderSimpleTest {
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

    private String checkValidOrder(RISKMap riskMap, Order... orders) throws IOException {
        String check_message = null;
        for (Order o : orders){
            check_message = o.executeOrder(riskMap);
        }

        for (Order o : orders){
            if (check_message==null){
                BattleField BF = riskMap.getTerritoryByName(o.getDestTerritory()).getBattleField();
                BF.setAttackResolver(new SimpleAttackResolver());
                System.out.println("Attackers(" + o.getPlayerID() + "):" +BF.getAttackers().get(o.getPlayerID()));
                BF.fightBattle(riskMap.getTerritoryByName(o.getDestTerritory()), "Unit");
                BF.resetAttackersList();
                assertNull(BF.getAttackers().get(o.getPlayerID()));
            }

        }


        return check_message;
    }

    @Test
    public void test_executeOrder() throws IOException{
        RISKMap riskMap = buildTestMap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true);
        //-------------VALID
        //normal consequential orders

        String check_message = checkValidOrder(riskMap, new AttackOrderSimple(0, "Test2",
                "Test3","Unit",10));
        System.out.println(check_message);
        displayMap(riskMap);

        assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 0);
        assertEquals(riskMap.getTerritoryByName("Test3").getUnitByType("Unit").getAmount(), 0);
        assertNull(riskMap.getTerritoryByName("Test3").getBattleField().getAttackers().get((long)0));

        String check_message2 = checkValidOrder(riskMap, new AttackOrderSimple(0, "Test1",
                "Test4","Unit",2));
        System.out.println(check_message2);
        displayMap(riskMap);

        assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 8);
        assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 8);
        assertNull(riskMap.getTerritoryByName("Test4").getBattleField().getAttackers().get((long)0));
        displayMap(riskMap);

        //resolve same player same territory attacks
        Order o1 = new AttackOrderSimple(0, "Test1","Test4","Unit",2);
        Order o2 = new AttackOrderSimple(0, "Test1","Test4","Unit",2);

        String check_message3 = checkValidOrder(riskMap, o1,o2);
        System.out.println(check_message3);
        assertEquals(riskMap.getTerritoryByName("Test1").getUnitByType("Unit").getAmount(), 4);
        assertEquals(riskMap.getTerritoryByName("Test4").getUnitByType("Unit").getAmount(), 4);
        assertNull(riskMap.getTerritoryByName("Test4").getBattleField().getAttackers().get((long)0));

        displayMap(riskMap);

        //resolve same player different territory attacks
        Order Move = new MoveOrder(0, "Test0", "Test2", "Unit", 5);
        Order o3 = new AttackOrderSimple(0, "Test0","Test3","Unit",2);
        Order o4 = new AttackOrderSimple(0, "Test2","Test3","Unit",2);
        Move.executeOrder(riskMap);
        String check_message4 = checkValidOrder(riskMap, o3,o4);
        System.out.println(check_message4);
        assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 3);
        assertEquals(riskMap.getTerritoryByName("Test2").getUnitByType("Unit").getAmount(), 3);
        assertEquals(riskMap.getTerritoryByName("Test3").getUnitByType("Unit").getAmount(), 4);
        assertNull(riskMap.getTerritoryByName("Test4").getBattleField().getAttackers().get((long)0));

        displayMap(riskMap);

        //resolve different player different territory attacks
        Order o5 = new AttackOrderSimple(0, "Test0","Test5","Unit",3);
        Order o6 = new AttackOrderSimple(2, "Test8","Test5","Unit",2);
        String check_message5 = checkValidOrder(riskMap, o5,o6);
        System.out.println(check_message5);
        assertEquals(riskMap.getTerritoryByName("Test0").getUnitByType("Unit").getAmount(), 0);
        assertEquals(riskMap.getTerritoryByName("Test8").getUnitByType("Unit").getAmount(), 8);
        assertEquals(riskMap.getTerritoryByName("Test5").getUnitByType("Unit").getAmount(), 5);
        assertNull(riskMap.getTerritoryByName("Test4").getBattleField().getAttackers().get((long)0));

        displayMap(riskMap);

    }
}