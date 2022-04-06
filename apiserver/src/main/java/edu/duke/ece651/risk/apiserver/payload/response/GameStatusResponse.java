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

    /**
     * overloaded constructor with all attributes
     * @param state
     * @param riskMap
     * @param winner
     * @param idToColor
     * @param prompt
     */
    public GameStatusResponse(String state, RISKMap riskMap, Long winner, TreeMap<Long, Color> idToColor, String prompt) {
        this.state = state;
        this.riskMap = riskMap;
        this.winner = winner;
        this.idToColor = idToColor;
        this.prompt = prompt;
    }

    /**
     * default constructor
     */
    public GameStatusResponse() {
    }

    /**
     * overloaded constructor with only prompt
     * @param prompt
     */
    public GameStatusResponse(String prompt) {
        this.prompt = prompt;
    }

    /**
     * get state
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * set state
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * get risk map
     * @return risk map
     */
    public RISKMap getRiskMap() {
        return riskMap;
    }

    /**
     * set risk map
     * @param riskMap
     */
    public void setRiskMap(RISKMap riskMap) {
        this.riskMap = riskMap;
    }

    /**
     * get winner
     * @return winner
     */
    public Long getWinner() {
        return winner;
    }

    /**
     * set winner
     * @param winner
     */
    public void setWinner(Long winner) {
        this.winner = winner;
    }

    /**
     * get id to color
     * @return id to color
     */
    public TreeMap<Long, Color> getIdToColor() {
        return idToColor;
    }

    /**
     * set id to color
     * @param idToColor
     */
    public void setIdToColor(TreeMap<Long, Color> idToColor) {
        this.idToColor = idToColor;
    }

    /**
     * get all lost players
     * @return all lost players
     */
    public Set<Long> getLostPlayers() {
        return lostPlayers;
    }

    /**
     * set all lost players
     * @param lostPlayers
     */
    public void setLostPlayers(Set<Long> lostPlayers) {
        this.lostPlayers = lostPlayers;
    }
}
