package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

//chain of rule checkers for all kinds of orders
public abstract class ActionChecker implements Serializable {

  private final ActionChecker next;

  /**
   * Checker ctor set next rule checker
   * @param next
   */
  public ActionChecker(ActionChecker next){
    this.next = next;
  }

  /**
   * check the rule of some arbitrary rule checker
   * @param riskMap
   * @param moveOrder
   * @return
   */
  protected abstract String checkMyRule(RISKMap riskMap, Order moveOrder);

  /**
   * check all the rules in the chain of checkers
   * @param riskMap
   * @param moveOrder
   * @return
   */
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
