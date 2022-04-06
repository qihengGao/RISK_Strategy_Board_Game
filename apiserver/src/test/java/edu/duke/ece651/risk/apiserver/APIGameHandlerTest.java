package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.models.State;
import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.Owner;
import edu.duke.ece651.risk.shared.factory.RandomMapFactory;
import edu.duke.ece651.risk.shared.factory.TestMapFactory;
import edu.duke.ece651.risk.shared.map.MapTextView;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.*;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class APIGameHandlerTest {
    private String displayMap(RISKMap riskMap) {
        TreeMap<Long, Color> idToColor = new TreeMap<Long, Color>();
        idToColor.put((long)1, new Color("Green"));
        idToColor.put((long)2, new Color("Blue"));

        MapTextView mapTextView = new MapTextView(riskMap, idToColor);
        return mapTextView.displayMap();
    }

    @Test
    void APIGameHandlerCtorAndGetters() {
        APIGameHandler game = new APIGameHandler(3, 0, 1L);
        assertEquals(game.getRoomSize(), 3);
        assertEquals(game.getRoomID(), 0);
        assertEquals(game.getPredefineColorList().get(0), new Color("Red"));
        assertEquals(game.getPredefineColorList().get(1), new Color("Green"));
        assertEquals(game.getPredefineColorList().get(2), new Color("Blue"));
        assertEquals(game.getPredefineColorList().get(3), new Color("Yellow"));
        assertEquals(game.getPredefineColorList().get(4), new Color("Purple"));
        assertTrue(game.getIdToColor().isEmpty());
        RISKMap riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(3);
        assertEquals(displayMap(game.getRiskMap()), displayMap(riskMap));
        assertTrue(game.getPlayers().contains(1L));
        assertEquals(game.getCurrentState(), State.WaitingToStartState.name());
    }

    @Test
    void setters() {
        APIGameHandler game = new APIGameHandler(3, 0, 1L);
        game.setPredefineColorList(new ArrayList<>());
        assertTrue(game.getPredefineColorList().isEmpty());
        game.setPlayers(new HashSet<>());
        assertTrue(game.getPlayers().isEmpty());
        game.setIdToColor(new TreeMap<>());
        assertTrue(game.getIdToColor().isEmpty());
        game.setRiskMap((RISKMap) new TestMapFactory().createMapForNplayers(3));
        RISKMap riskMap = (RISKMap) new TestMapFactory().createMapForNplayers(3);
        assertEquals(displayMap(game.getRiskMap()), displayMap(riskMap));
        game.setCurrentState(State.PlacingState.name());
        assertEquals(game.getCurrentState(), State.PlacingState.name());
    }

    @Test
    void getRiskMapByState() {
        APIGameHandler game = new APIGameHandler(2, 0, 1L);

        game.setCurrentState(State.PlacingState.name());
        RISKMap riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(2);
        assertEquals(displayMap(game.getRiskMapByState()), displayMap(riskMap));

        game.setCurrentState(State.OrderingState.name());
        riskMap.getTerritoryByName("Test1").tryAddUnit(new BasicUnit("unit", 10));
        assertEquals(displayMap(game.getRiskMapByState()), displayMap(riskMap));

    }

    @Test
    void tryAddPlayer() {
        APIGameHandler game = new APIGameHandler(2, 0, 1L);
        assertTrue(game.tryAddPlayer(2L));
        assertFalse(game.tryAddPlayer(2L));
        assertFalse(game.tryAddPlayer(3L));
    }

    @Test
    void tryPlayingGame() {
        APIGameHandler game = new APIGameHandler(2, 0, 1L);

        Map<String, Integer> unitPlaceOrders = new HashMap<>();
        assertEquals(game.tryPlaceUnit(1L, unitPlaceOrders), "Failed to place the orders! Place action invalid right now!");
        game.tryAddPlayer(2L);

        unitPlaceOrders.put("Test0", 10);
        unitPlaceOrders.put("Test1", 10);
        unitPlaceOrders.put("Test2", 10);
        game.unitPlacementPhase(3);
        assertEquals(game.tryPlaceUnit(1L, unitPlaceOrders), "You must place units in your own territories!");

        unitPlaceOrders.remove("Test2");
        unitPlaceOrders.put("Test3", 10);
        assertNull(game.tryPlaceUnit(1L, unitPlaceOrders));
        assertEquals(game.tryPlaceUnit(1L, unitPlaceOrders), "Failed to place the orders! Place action invalid right now!");

        ArrayList<Order> orders = new ArrayList<>();
        assertEquals(game.tryPreProcessOrder(1L, orders), "Failed to place the orders! Place action invalid right now!");

        Map<String, Integer> unitPlaceOrders2= new HashMap<>();
        unitPlaceOrders2.put("Test2", 10);
        unitPlaceOrders2.put("Test4", 10);
        unitPlaceOrders2.put("Test5", 10);
        assertNull(game.tryPlaceUnit(2L, unitPlaceOrders2));

        //first round
        assertNull(game.tryPreProcessOrder(1L,orders));
        assertEquals(game.getPlayerState(1L), State.WaitingState.name());
        assertEquals(game.tryPreProcessOrder(1L,orders), "Failed to place the orders! Place action invalid right now!");
        ArrayList<Order> orders2 = new ArrayList<>();
        orders2.add(new MoveOrder(2L, "Test2", "Test4", "Soldier", 1));
        assertNull(game.tryPreProcessOrder(2L,orders));

        //end game
        game.getRiskMap().getTerritoryByName("Test2").tryChangeOwnerTo(1L);
        game.getRiskMap().getTerritoryByName("Test4").tryChangeOwnerTo(1L);
        game.getRiskMap().getTerritoryByName("Test5").getUnitByType("Soldier").setAmount(0);

        orders.add(new AttackOrder(1L, "Test0", "Test5","Soldier",1));
        assertNull(game.tryPreProcessOrder(1L,orders));
        assertNull(game.tryPreProcessOrder(2L,new ArrayList<>()));

        assertEquals(game.getCurrentState(), State.EndState.name());

        assertTrue(game.isPlayerLost(2L));
        assertFalse(game.isPlayerLost(1L));

    }

    @Test
    void tryExecuteOrder() {
        APIGameHandler game = new APIGameHandler(2, 0, 1L);

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new MoveOrder(1L, "Test0", "Test5","Soldier",1));
        ArrayList<Order> orders1 = new ArrayList<>();
        orders1.add(new AttackOrder(1L, "Test0", "Test6","Soldier",1));
        ArrayList<Order> orders2 = new ArrayList<>();
        orders2.add(new UpgradeUnitOrder(1L, "Test0", "Soldier",1, 0));
        ArrayList<Order> orders3 = new ArrayList<>();
        orders3.add(new UpgradeMaxTechOrder(1L, "Test0", "Test6","Soldier",1));
        orders3.add(new UpgradeMaxTechOrder(1L, "Test0", "Test6","Soldier",1));
        ArrayList<Order> orders4 = new ArrayList<>();

        RISKMap riskMap = (RISKMap) new RandomMapFactory().createMapForNplayers(2);
        riskMap.getOwners().put(1L, new Owner(1L));
        assertNotEquals(game.tryExecuteOrder(orders, riskMap), null);
        assertNotEquals(game.tryExecuteOrder(orders1, riskMap), null);
        assertNotEquals(game.tryExecuteOrder(orders2, riskMap), null);
        assertNotEquals(game.tryExecuteOrder(orders3, riskMap), null);
        orders3.remove(0);
        assertNotEquals(game.tryExecuteOrder(orders3, riskMap), null);
        riskMap.getOwners().get(1L).tryAddOrRemoveTechResource(1000);
        assertNull(game.tryExecuteOrder(orders3, riskMap));
        assertNull(game.tryExecuteOrder(orders4, riskMap));

    }

    @Test
    void isPlayerLost() {
        APIGameHandler game = new APIGameHandler(2, 0, 1L);
        assertFalse(game.isPlayerLost(1L));
        assertEquals(game.getPlayerState(1L), State.WaitingToStartState.name());
        HashSet<Long> lost = new HashSet<>();
        lost.add(1L);
        game.setLostPlayer(lost);
        assertTrue(game.isPlayerLost(1L));
        assertEquals(game.getPlayerState(1L), State.LostState.name());


    }
}