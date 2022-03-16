package edu.duke.ece651.risk.shared;

import java.io.Serializable;

public abstract class Order implements Serializable{
  protected String srcTerritory;
  protected String destTerritory;
  protected String unitType;
  protected int unitAmount;
  protected long playerID;
  protected String orderType;

  public Order(long ID, String srcTerritory, String destTerritory, String unitType, int unitAmount, String orderType) {
    this.playerID = ID;
    this.srcTerritory = srcTerritory;
    this.destTerritory = destTerritory;
    this.unitType = unitType;
    this.unitAmount = unitAmount;
    this.orderType = orderType;
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
