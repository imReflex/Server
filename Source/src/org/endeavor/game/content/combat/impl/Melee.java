package org.endeavor.game.content.combat.impl;

import java.io.Serializable;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.HitTask;
import org.endeavor.game.content.combat.Combat;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;

public class Melee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4905589972876433518L;
	private final Entity entity;
	private Attack attack = new Attack(1, 5);
	private Animation animation = new Animation(422, 0);

	private int nextDamage = -1;
	private double damageBoost = 1.0D;

	public Melee(Entity entity) {
		this.entity = entity;
	}

	public void execute(Entity attacking) {
		if (attack == null) {
			return;
		}
		boolean success = entity.getAccuracy(attacking, CombatTypes.MELEE) >= Combat.next(100);

		int damage = (int) (entity.getCorrectedDamage(Combat.next(entity.getMaxHit(CombatTypes.MELEE) + 1)) * damageBoost);

		if (nextDamage != -1) {
			damage = nextDamage;
			success = true;
		}

		Hit hit = new Hit(entity, (success) || (entity.isIgnoreHitSuccess()) ? damage : 0, Hit.HitTypes.MELEE);
		entity.setLastDamageDealt(!success ? 0 : hit.getDamage());

		entity.setLastHitSuccess((success) || (entity.isIgnoreHitSuccess()));

		entity.onAttack(attacking, hit.getDamage(), CombatTypes.MELEE, success);

		entity.getCombat().updateTimers(attack.getAttackDelay());

		if (animation != null) {
			entity.getUpdateFlags().sendAnimation(animation);
		}

		entity.doConsecutiveAttacks(attacking);
		finish(attacking, hit);
	}

	public void finish(Entity attacking, Hit hit) {
		/*if (attacking.getCombat().getAttackTimer() == 0) {
			attacking.getCombat().setAttackTimer(4); // might need to be adjusted
		}*/
		
		attacking.getCombat().setInCombat(entity);
		TaskQueue.queue(new HitTask(attack.getHitDelay(), false, hit, attacking));
	}

	public Attack getAttack() {
		return attack;
	}

	public void setAttack(Attack attack, Animation animation) {
		this.attack = attack;
		this.animation = animation;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setNextDamage(int nextDamage) {
		this.nextDamage = nextDamage;
	}

	public void setDamageBoost(double damageBoost) {
		this.damageBoost = damageBoost;
	}
}
