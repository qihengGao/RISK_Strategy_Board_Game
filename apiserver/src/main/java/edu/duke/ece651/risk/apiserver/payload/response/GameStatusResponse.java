package edu.duke.ece651.risk.apiserver.payload.response;

import edu.duke.ece651.risk.shared.Color;
import edu.duke.ece651.risk.shared.map.RISKMap;

import javax.persistence.SecondaryTable;
import java.util.Set;
import java.util.TreeMap;

public class GameStatusResponse {
    private String state;
    private RISKMap riskMap;
    private Long winner;
    private TreeMap<Long, Color> idToColor;
    private Set<Long> lostPlayers;
    private String prompt;

    public GameStatusResponse(String state, RISKMap riskMap, Long winner, TreeMap<Long, Color> idToColor, String prompt) {
        this.state = state;
        this.riskMap = riskMap;
        this.winner = winner;
        this.idToColor = idToColor;
        this.prompt = prompt;
    }

    public GameStatusResponse() {
    }

    public GameStatusResponse(String prompt) {
        this.prompt = prompt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public RISKMap getRiskMap() {
        return riskMap;
    }

    public void setRiskMap(RISKMap riskMap) {
        this.riskMap = riskMap;
    }

    public Long getWinner() {
        return winner;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }

    public TreeMap<Long, Color> getIdToColor() {
        return idToColor;
    }

    public void setIdToColor(TreeMap<Long, Color> idToColor) {
        this.idToColor = idToColor;
    }

    public Set<Long> getLostPlayers() {
        return lostPlayers;
    }

    public void setLostPlayers(Set<Long> lostPlayers) {
        this.lostPlayers = lostPlayers;
    }
}
