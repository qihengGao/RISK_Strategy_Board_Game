package edu.duke.ece651.risk.shared.order;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.duke.ece651.risk.shared.map.RISKMap;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "orderType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttackOrder.class, name = "Attack"),
        @JsonSubTypes.Type(value = MoveOrder.class, name = "Move"),
        @JsonSubTypes.Type(value = UpgradeUnitOrder.class, name = "Upgrade Unit"),
        @JsonSubTypes.Type(value = UpgradeMaxTechOrder.class, name = "Upgrade Tech Level")
})
public abstract class Order implements Serializable{
  protected String srcTerritory;
  protected String destTerritory;
  protected String unitType;
  protected int unitAmount;
  protected long playerID;
  protected String orderType;

  public void setToLevel(int toLevel) {
    this.toLevel = toLevel;
  }

  protected int toLevel;

  public Order() {
  }

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

  public Order(long ID, String srcTerritory, String destTerritory, String unitType, int unitAmount, String orderType, int toLevel) {
    this.playerID = ID;
    this.srcTerritory = srcTerritory;
    this.destTerritory = destTerritory;
    this.unitType = unitType;
    this.unitAmount = unitAmount;
    this.orderType = orderType;
    this.toLevel = toLevel;
  }

  //getters
  public long getPlayerID(){
    return this.playerID;
  }
  
  public String getSrcTerritory(){
    return this.srcTerritory;
  }

  public String getDestTerritory(){
    return this.destTerritory;
  }

  public String getUnitType(){
    return this.unitType;
  }

  public int getUnitAmount(){
    return this.unitAmount;
  }

  public String getOrderType() {return orderType;}

  public int getToLevel() { return this.toLevel; }

  //dinamic dispatched methods

  public abstract String executeOrder(RISKMap riskMap);

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(orderType+ "("+"):");
    result.append(srcTerritory);
    result.append("->");
    result.append(destTerritory);
    result.append(": "+unitType+" ");
    result.append(unitAmount);
    return result.toString();
  }

}
