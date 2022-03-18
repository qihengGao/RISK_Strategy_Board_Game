package edu.duke.ece651.risk.shared;

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
	 */
	public abstract int resolveCurrent();


}
