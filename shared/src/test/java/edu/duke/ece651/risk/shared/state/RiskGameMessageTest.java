package edu.duke.ece651.risk.shared.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.TreeMap;

import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.RISKMap;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.state.InitiateSocketState;
import edu.duke.ece651.risk.shared.state.State;
import edu.duke.ece651.risk.shared.state.UnitPlaceState;
import org.junit.jupiter.api.Test;

public class RiskGameMessageTest {

    /**
     * helper method to generate a message
     * @param withIdToColor if true, use the constructor with idToColor
     * @return mocked message
     */
    private RiskGameMessage generateMessage(boolean withIdToColor){
        RISKMap riskMap = mock(RISKMap.class);
        RiskGameMessage message = this.generateMessageWithSelfSuppliedMap(riskMap, withIdToColor);
        return message;
    }

    private RiskGameMessage generateMessageWithSelfSuppliedMap(RISKMap riskMap, boolean withIdToColor){
        long clientid = 0;
        State state = new InitiateSocketState();
        String prompt = "prompt";
        Color color = new Color("red");

        TreeMap<Long,Color> idToColor = new TreeMap<Long,Color>();
        idToColor.put(clientid, color);

        RiskGameMessage message;
        if(withIdToColor){
            message = new RiskGameMessage(clientid, state, riskMap, prompt, idToColor);
        }
        else{
            message = new RiskGameMessage(clientid, state, riskMap, prompt); 
        }

        return message;
    }

    /**
     * test for getClientCurrentStateName and setClientCurrentStateName
     */
    @Test
    public void test_getSetClientCurrentStateName(){
        RiskGameMessage message = this.generateMessage(true);
        String currentState = "currentState";
        message.setClientCurrentStateName(currentState);
        assertEquals(currentState, message.getClientCurrentStateName());
    }

    /**
     * test for getRoomID and setRoomID
     */
    @Test
    public void test_getSetRoomID(){
        RiskGameMessage message = this.generateMessage(true);
        int roomId = 12345;
        message.setRoomID(12345);
        assertEquals(roomId, message.getRoomID());
    }

    /**
     * test for getRoomSize and setRoomSize
     */
    @Test
    public void test_getSetRoomSize(){
        RiskGameMessage message = this.generateMessage(true);
        int roomSize = 12345;
        message.setRoomSize(roomSize);
        assertEquals(roomSize, message.getRoomSize()); 
    }

    /**
     * test for isCreateAGameRoom and setCreateAGameRoom
     */
    @Test
    public void test_getSetIsCreateAGameRoom(){
        RiskGameMessage message = this.generateMessage(true);
        boolean isCreateAGameRoom = false;
        message.setCreateAGameRoom(isCreateAGameRoom);
        assertEquals(isCreateAGameRoom, message.isCreateAGameRoom()); 

        isCreateAGameRoom = true;
        message.setCreateAGameRoom(isCreateAGameRoom);
        assertEquals(isCreateAGameRoom, message.isCreateAGameRoom()); 
    }

    @Test
    public void test_defaultConstructor(){
        RiskGameMessage message = new RiskGameMessage();
    }
    
    @Test
    public void test_getSetIsInitGame(){
        RiskGameMessage message = this.generateMessage(true);
        boolean isInitGame = false;
        message.setInitGame(isInitGame);
        assertEquals(isInitGame, message.isInitGame());

        isInitGame = true;
        message.setInitGame(isInitGame);
        assertEquals(isInitGame, message.isInitGame());
    }

    /**
     * test for getClientId and setClientid
     */
    @Test
    public void test_getSetClientId(){
        RiskGameMessage message1 = this.generateMessage(true);
        RiskGameMessage message2 = this.generateMessage(false);
        assertEquals(0, message1.getClientid());
        assertEquals(0, message2.getClientid());

        message1.setClientid(1);
        assertEquals(1, message1.getClientid());
        message2.setClientid(2);
        assertEquals(2, message2.getClientid());
    }

    /**
     * test for getIdToColor and setIdToColor
     */
    @Test
    public void test_getSetIdToColor(){
        RiskGameMessage message = this.generateMessage(false);
        assertNull(message.getIdToColor());

        long clientId = 0;
        TreeMap<Long,Color> idToColor = new TreeMap<Long,Color>();
        idToColor.put(clientId, new Color("red"));
        message.setIdToColor(idToColor);
        assertSame(idToColor, message.getIdToColor());
    }

    /**
     * test for getRiskMap and setRiskMap
     */
    @Test
    public void test_getRiskMap(){
        RISKMap riskMap = mock(RISKMap.class);
        RiskGameMessage message = this.generateMessage(false);
        message.setRiskMap(riskMap);
        assertSame(riskMap, message.getRiskMap());
    }

    /**
     * test for getCurrentState and setCurrentState
     */
    @Test
    public void test_getSetCurrentState(){
        RiskGameMessage message1 = this.generateMessage(true);
        RiskGameMessage message2 = this.generateMessage(false);

        assertInstanceOf(InitiateSocketState.class, message1.getCurrentState());
        assertInstanceOf(InitiateSocketState.class, message2.getCurrentState());

        UnitPlaceState unitPlaceState = new UnitPlaceState();
        message1.setCurrentState(unitPlaceState);
        message2.setCurrentState(unitPlaceState);

        assertInstanceOf(UnitPlaceState.class, message1.getCurrentState());
        assertInstanceOf(UnitPlaceState.class, message2.getCurrentState());
    }

    /**
     * test for getPrompt and setPrompt
     */
    @Test
    public void test_getPrompt(){
        RiskGameMessage message1 = this.generateMessage(true);
        RiskGameMessage message2 = this.generateMessage(false);
        String expectedPrompt = "prompt";
        assertEquals(expectedPrompt, message1.getPrompt());
        assertEquals(expectedPrompt, message2.getPrompt());

        expectedPrompt = "newPrompt";
        message1.setPrompt(expectedPrompt);
        message2.setPrompt(expectedPrompt);
        assertEquals(expectedPrompt, message1.getPrompt());
        assertEquals(expectedPrompt, message2.getPrompt());
    }

    /**
     * test for setColor
     */
    @Test
    public void test_setColor(){
        RiskGameMessage message = this.generateMessage(true);
        Color color = new Color("red");
        message.setColor(color);
    }
}
