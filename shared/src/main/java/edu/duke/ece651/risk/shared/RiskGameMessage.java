package edu.duke.ece651.risk.shared;

import java.io.Serializable;
import java.util.TreeMap;

public class RiskGameMessage implements Serializable {

    private long clientid;

    public long getClientid() {
        return clientid;
    }
    private TreeMap<Long,Color> idToColor;
    public TreeMap<Long, Color> getIdToColor() {
        return idToColor;
    }

    public void setIdToColor(TreeMap<Long, Color> idToColor) {
        this.idToColor = idToColor;
    }


    //orders
    //private ArrayList<>
    private RISKMap riskMap;
    private State currentState;
    private String prompt;
    private Color color = null;

    public RISKMap getRiskMap() {
        return riskMap;
    }

    public State getCurrentState() {
        return currentState;
    }

    public String getPrompt() {
        return prompt;
    }

    public RiskGameMessage(long clientid, State currentState, RISKMap riskMap, String prompt, TreeMap<Long,Color> idToColor) {
        this.clientid = clientid;
        this.riskMap = riskMap;
        this.currentState = currentState;
        this.prompt = prompt;
        this.color = idToColor.get(clientid);
        this.idToColor = idToColor;
    }

    public RiskGameMessage(long clientid, State currentState, RISKMap riskMap, String prompt) {
        this.clientid = clientid;
        this.riskMap = riskMap;
        this.currentState = currentState;
        this.prompt = prompt;
    }
}
