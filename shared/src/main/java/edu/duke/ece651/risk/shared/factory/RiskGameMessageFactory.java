package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.ClientContext;
import edu.duke.ece651.risk.shared.RiskGameMessage;
import edu.duke.ece651.risk.shared.state.RestoreState;
import edu.duke.ece651.risk.shared.state.SelectRoomState;

public class RiskGameMessageFactory {

    /**
     * Create an initiate game message.
     * @return A RiskGameMessage which contains a field initGame==true.
     */
    public RiskGameMessage createInitMessage(){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setClientCurrentStateName("RestoreState");
        riskGameMessage.setInitGame(true);
        return riskGameMessage;
    }

    /**
     * Create a restore game message.
     * @param context ClientContext which contains the clientID.
     * @return A RiskGameMessage which contains a field initGame==false and clientID == the clientID player want to restore.
     */


    public RiskGameMessage createReconnectMessage(ClientContext context, Long oriClientID){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setInitGame(false);
        riskGameMessage.setClientCurrentStateName("RestoreState");
        riskGameMessage.setClientid(oriClientID);
        return riskGameMessage;
    }


    /**
     * Create a message to create a new game room with specified room size.
     * @param roomSize Room size.
     * @return A RiskGameMessage which contains a field createAGameRoom==true and roomSize.
     */
    public RiskGameMessage createCreateGameRoomMessage(int roomSize){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setClientCurrentStateName("SelectRoomState");
        riskGameMessage.setCreateAGameRoom(true);
        riskGameMessage.setRoomSize(roomSize);
        return riskGameMessage;
    }


    /**
     * Create a message to join an existing game room.
     * @param roomID Room ID which player want to join.
     * @return A RiskGameMessage which contains a field createAGameRoom==false and the room ID which player want to join.
     */
    public RiskGameMessage createJoinGameRoomMessage(int roomID){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setClientCurrentStateName("SelectRoomState");
        riskGameMessage.setCreateAGameRoom(false);
        riskGameMessage.setRoomID(roomID);
        return riskGameMessage;
    }



    public static RiskGameMessage createRestoreStateMessage(String prompt){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setCurrentState(new RestoreState());
        riskGameMessage.setPrompt(prompt);
        return riskGameMessage;
    }

    public static RiskGameMessage createSelectRoomState(String prompt){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setCurrentState(new SelectRoomState());
        riskGameMessage.setPrompt(prompt);
        return riskGameMessage;
    }
}
