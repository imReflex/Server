package org.endeavor.game.content.combat.impl;

public class Attack {
	private int hitDelay;
	private int attackDelay;

	public Attack(int hitDelay, int attackDelay) {
		this.hitDelay = hitDelay;
		this.attackDelay = attackDelay;
	}

	public int getHitDelay() {
		return hitDelay;
	}

	public void setHitDelay(int hitDelay) {
		this.hitDelay = hitDelay;
	}

	public int getAttackDelay() {
		return attackDelay;
	}

	public void setAttackDelay(int attackDelay) {
		this.attackDelay = attackDelay;
	}
}
