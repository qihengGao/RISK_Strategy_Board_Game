package edu.duke.ece651.risk.shared;

import java.io.Serializable;
import java.util.TreeMap;

public class RiskGameMessage implements Serializable {

    private long clientid;
    private boolean initGame;

    public boolean isInitGame() {
        return initGame;
    }

    public void setInitGame(boolean initGame) {
        this.initGame = initGame;
    }

    public RiskGameMessage() {
    }

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

    private RISKMap riskMap;

    public void setClientid(long clientid) {
        this.clientid = clientid;
    }

    public void setRiskMap(RISKMap riskMap) {
        this.riskMap = riskMap;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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