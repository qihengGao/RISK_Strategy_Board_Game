package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;

import java.io.Serializable;

public abstract class ActionChecker implements Serializable {

  private final ActionChecker next;

  public ActionChecker(ActionChecker next){
    this.next = next;
  }

  protected abstract String checkMyRule(RISKMap riskMap, Order moveOrder);

  public String checkMove (RISKMap riskMap, Order moveOrder) {
    //if we fail our own rule: stop the placement is not legal
    String this_message = checkMyRule(riskMap, moveOrder);
    if (this_message!=null) {
      return this_message;
    }
    //other wise, ask the rest of the chain.
    if (next != null) {
      return next.checkMove(riskMap, moveOrder);
    }
    //if there are no more rules, then the placement is legal
    return null;
  }

}
