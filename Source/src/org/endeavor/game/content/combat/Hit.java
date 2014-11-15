package org.endeavor.game.content.combat;

import java.io.Serializable;

import org.endeavor.game.entity.Entity;

public class Hit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5631405329164304471L;
	private int damage;
	private HitTypes type;
	private Entity attacker;
	private final boolean success;

	public Hit(Entity attacker, int damage, HitTypes type) {
		this.attacker = attacker;
		this.damage = damage;
		this.type = type;
		success = (damage > 0);
	}

	public Hit(int damage, HitTypes type) {
		this(null, damage, type);
	}

	public Hit(int damage) {
		this(null, damage, HitTypes.NONE);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public HitTypes getType() {
		return type;
	}

	public Entity getAttacker() {
		return attacker;
	}

	public int getHitType() {
		switch (type) {
		case MELEE:
		case RANGED:
		case MAGIC:
			if (damage > 0) {
				return /*5 - player's own*/0;
			}
			return 0;
		case POISON:
			return 2/*own poistion 7*/;
		case DISEASE:
			return 3/* own disease 8*/;
		case EAT:
			return 4/*own eat 9*/;
		default:
			break;
		}

		return 1;
	}

	public int getCombatHitType() {
		switch (type) {
		case MAGIC:
			return 2;
		case MELEE:
			return 0;
		case ABSORB:
			return 5;
		case DEFLECT:
			return 3;
		case CANNON:
			return 4;
		case NONE:
		case POISON:
		case DISEASE:
		case EAT:
			return 255;
		case RANGED:
			return 1;
		}
		
		return 0;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setType(HitTypes type) {
		this.type = type;
	}

	public static enum HitTypes {
		NONE, MELEE, RANGED, MAGIC, POISON, DISEASE, ABSORB, EAT, CANNON, DEFLECT;
	}
}
