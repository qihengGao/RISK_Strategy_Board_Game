package edu.duke.ece651.risk.shared.battle;

import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.Serializable;

/**
 * Abstract class for general chaining defensing attack check.
 */
public abstract class AttackResolver implements Serializable {
	protected final AttackResolver next;

	/**
	 * Constructors.
	 */
	public AttackResolver(AttackResolver next){
		this.next = next;
	}

	/**
	 * Rule need to be overriden by specific rule.
	 * @return
	 * @param currAttacker
	 * @param currDefender
	 */
	public abstract int resolveCurrent(Unit currAttacker, Unit currDefender);


}
