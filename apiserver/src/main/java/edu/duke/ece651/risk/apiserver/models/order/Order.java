package edu.duke.ece651.risk.apiserver.models.order;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.duke.ece651.risk.shared.map.RISKMap;

import javax.persistence.*;
import java.io.Serializable;

//json resolve for specific order types

@Entity
@Table(name = "predefineColors",
        uniqueConstraints = {
        })
public class Order implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    //all required information for some order
    protected String srcTerritory;
    protected String destTerritory;
    protected String unitType;
    protected int unitAmount;
    protected Long playerID;
    protected String orderType;
    protected int toLevel;
    protected Long allianceID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //default ctor
    public Order() {
    }

    //setters required for Json deserialization
    public void setSrcTerritory(String srcTerritory) {
        this.srcTerritory = srcTerritory;
    }

    public void setDestTerritory(String destTerritory) {
        this.destTerritory = destTerritory;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setToLevel(int toLevel) {
        this.toLevel = toLevel;
    }

    public void setAllianceID(Long allianceID) {
        this.allianceID = allianceID;
    }

    //getters required for Json deserialization
    public long getPlayerID() {
        return this.playerID;
    }

    public String getSrcTerritory() {
        return this.srcTerritory;
    }

    public String getDestTerritory() {
        return this.destTerritory;
    }

    public String getUnitType() {
        return this.unitType;
    }

    public int getUnitAmount() {
        return this.unitAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public int getToLevel() {
        return this.toLevel;
    }

    public Long getAllianceID() {
        return allianceID;
    }

    //ctor
    public Order(long ID, String srcTerritory, String destTerritory, String unitType, int unitAmount, String orderType, int toLevel, Long allianceID) {
        this.playerID = ID;
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
        this.orderType = orderType;
        this.toLevel = toLevel;
        this.allianceID = allianceID;
    }
    //dinamic dispatched methods


    /**
     * @return String: the information of this order
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(orderType + "(" + "):");
        result.append(srcTerritory);
        result.append("->");
        result.append(destTerritory);
        result.append(": " + unitType + " ");
        result.append(unitAmount);
        return result.toString();
    }

}
