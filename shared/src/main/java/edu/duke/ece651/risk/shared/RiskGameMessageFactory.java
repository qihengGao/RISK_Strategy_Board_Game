package edu.duke.ece651.risk.shared;

public class RiskGameMessageFactory {
    public RiskGameMessage createInitMessage(){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setInitGame(true);
        return riskGameMessage;
    }

    public RiskGameMessage createReconnectMessage(ClientContext context){
        RiskGameMessage riskGameMessage = new RiskGameMessage();
        riskGameMessage.setInitGame(false);
        riskGameMessage.setClientid(context.getPlayerID());
        return riskGameMessage;
    }
}
