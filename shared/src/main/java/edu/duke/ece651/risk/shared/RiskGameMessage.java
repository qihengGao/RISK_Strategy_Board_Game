package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public class RiskGameMessage implements Serializable {

    private long clientid;

    public long getClientid() {
        return clientid;
    }


    //orders
    //private ArrayList<>
    private RISKMap riskMap;
    private State currentState;
    private String prompt;
    private Color color;

    public RISKMap getRiskMap() {
        return riskMap;
    }

    public State getCurrentState() {
        return currentState;
    }

    public String getPrompt() {
        return prompt;
    }

    public RiskGameMessage(long clientid, State currentState, RISKMap riskMap, String prompt, Color color) {
        this.clientid = clientid;
        this.riskMap = riskMap;
        this.currentState = currentState;
        this.prompt = prompt;
        this.color = color;
    }
}
