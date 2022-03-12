package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.TreeMap;

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
     * test for getClientId
     */
    @Test
    public void test_getClientId(){
        RiskGameMessage message1 = this.generateMessage(true);
        RiskGameMessage message2 = this.generateMessage(false);
        assertEquals(0, message1.getClientid());
        assertEquals(0, message2.getClientid());
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
     * test for getRiskMap
     */
    @Test
    public void test_getRiskMap(){
        RISKMap riskMap = mock(RISKMap.class);
        RiskGameMessage message = this.generateMessageWithSelfSuppliedMap(riskMap, true);
        assertSame(riskMap, message.getRiskMap());
    }

    /**
     * test for getCurrentState()
     */
    @Test
    public void test_getCurrentState(){
        RiskGameMessage message1 = this.generateMessage(true);
        RiskGameMessage message2 = this.generateMessage(false);
        assertInstanceOf(InitiateSocketState.class, message1.getCurrentState());
        assertInstanceOf(InitiateSocketState.class, message2.getCurrentState());
    }

    /**
     * test for getPrompt()
     */
    @Test
    public void test_getPrompt(){
        RiskGameMessage message1 = this.generateMessage(true);
        RiskGameMessage message2 = this.generateMessage(false);
        String expectedPrompt = "prompt";
        assertEquals(expectedPrompt, message1.getPrompt());
        assertEquals(expectedPrompt, message2.getPrompt());
    }
}
