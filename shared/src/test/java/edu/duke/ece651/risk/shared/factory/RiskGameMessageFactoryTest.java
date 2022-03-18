package edu.duke.ece651.risk.shared.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;

public class RiskGameMessageFactoryTest {
    
    @Test
    public void test_createInitMessage(){
        RiskGameMessageFactory factory = new RiskGameMessageFactory();
        RiskGameMessage message = factory.createInitMessage();
        assertEquals("RestoreState", message.getClientCurrentStateName());
        assertEquals(true, message.isInitGame());
    }

    @Test
    public void test_createReconnectMessage(){
        RiskGameMessageFactory factory = new RiskGameMessageFactory();
        ClientContext clientContext = new ClientContext();
        Long clientId = 0L;

        RiskGameMessage message = factory.createReconnectMessage(clientContext, clientId);
        assertEquals(false, message.isInitGame());
        assertEquals("RestoreState", message.getClientCurrentStateName());
        assertEquals(clientId, message.getClientid());
    }

    @Test
    public void test_createCreateGameRoomMessage(){
        RiskGameMessageFactory factory = new RiskGameMessageFactory();
        int roomSize = 10;

        RiskGameMessage message = factory.createCreateGameRoomMessage(roomSize);
        assertEquals("SelectRoomState", message.getClientCurrentStateName());
        assertEquals(true, message.isCreateAGameRoom());
        assertEquals(roomSize, message.getRoomSize());
    }

    @Test
    public void tests_createJoinGameRoomMessage(){
        RiskGameMessageFactory factory = new RiskGameMessageFactory();
        int roomId = 10;

        RiskGameMessage message = factory.createJoinGameRoomMessage(roomId);
        assertEquals("SelectRoomState", message.getClientCurrentStateName());
        assertEquals(false, message.isCreateAGameRoom());
        assertEquals(roomId, message.getRoomID());
    }

    @Test
    public void test_createRestoreStateMessage(){
        String prompt = "prompt";

        RiskGameMessage message = RiskGameMessageFactory.createRestoreStateMessage(prompt);
        assertEquals(null, message.getClientCurrentStateName());
        assertEquals(prompt, message.getPrompt());
    }

    @Test
    public void test_createSelectRoomState(){
        String prompt = "prompt";

        RiskGameMessage message = RiskGameMessageFactory.createSelectRoomState(prompt);
        assertEquals(null, message.getClientCurrentStateName());
        assertEquals(prompt, message.getPrompt());
    }
}
