package edu.duke.ece651.risk.shared.order;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.duke.ece651.risk.shared.map.RISKMap;

import java.io.Serializable;

//json resolve for specific order types
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "orderType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttackOrder.class, name = "Attack"),
        @JsonSubTypes.Type(value = MoveOrder.class, name = "Move"),
        @JsonSubTypes.Type(value = UpgradeUnitOrder.class, name = "Upgrade Unit"),
        @JsonSubTypes.Type(value = UpgradeMaxTechOrder.class, name = "Upgrade Tech Level")
})
public abstract class Order implements Serializable{
  //all required information for some order
  protected String srcTerritory;
  protected String destTerritory;
  protected String unitType;
  protected int unitAmount;
  protected long playerID;
  protected String orderType;
  protected int toLevel;

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
  //getters required for Json deserialization
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

  //ctor
  public Order(long ID, String srcTerritory, String destTerritory, String unitType, int unitAmount, String orderType, int toLevel) {
    this.playerID = ID;
    this.srcTerritory = srcTerritory;
    this.destTerritory = destTerritory;
    this.unitType = unitType;
    this.unitAmount = unitAmount;
    this.orderType = orderType;
    this.toLevel = toLevel;
  }
  //dinamic dispatched methods

  /**
   * execute some inherited order
   * @param riskMap
   * @return null if success, error message if some rule didn't pass
   */
  public abstract String executeOrder(RISKMap riskMap);

  /**
   * @return String: the information of this order
   */
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
