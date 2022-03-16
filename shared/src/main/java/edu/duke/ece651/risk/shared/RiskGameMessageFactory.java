package edu.duke.ece651.risk.shared;

public class RiskGameMessageFactory {

    /**
     * Create an initiate game message.
     * @return A RiskGameMessage which contains a field initGame==true.
     */
    public RiskGameMessage createInitMessage(){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setInitGame(true);
        return riskGameMessage;
    }

    /**
     * Create a restore game message.
     * @param context ClientContext which contains the clientID.
     * @return A RiskGameMessage which contains a field initGame==false and clientID == the clientID player want to restore.
     */


    public RiskGameMessage createReconnectMessage(ClientContext context){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setInitGame(false);
        riskGameMessage.setClientid(context.getPlayerID());
        return riskGameMessage;
    }


    /**
     * Create a message to create a new game room with specified room size.
     * @param roomSize Room size.
     * @return A RiskGameMessage which contains a field createAGameRoom==true and roomSize.
     */
    public RiskGameMessage createCreateGameRoomMessage(int roomSize){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
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
        riskGameMessage.setCreateAGameRoom(false);
        riskGameMessage.setRoomID(roomID);
        return riskGameMessage;
    }
}
