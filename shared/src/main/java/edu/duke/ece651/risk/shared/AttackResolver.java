package edu.duke.ece651.risk.shared;

/**
 * Abstract class for general chaining defensing attack check.
 */
public abstract class AttackResolver {
	protected final AttackResolver next;

	/**
	 * Constructors.
	 */
	public AttackResolver(AttackResolver next){
		this.next = next;
	}

	/**
	 * Rule need to be overriden by specific rule.
	 */
	public abstract boolean resolveCurrent();

	public boolean isDefenseSuccessful() {
		boolean currResult = this.resolveCurrent();
		if(currResult == false){
			return false;
		}
		if(this.next != null){
			return this.next.resolveCurrent();
		}
		return false;
	}
}
