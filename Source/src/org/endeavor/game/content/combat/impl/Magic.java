package org.endeavor.game.content.combat.impl;

import java.io.Serializable;

import org.endeavor.engine.task.RunOnceTask;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.GraphicTask;
import org.endeavor.engine.task.impl.HitTask;
import org.endeavor.game.content.combat.Combat;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.CombatConstants;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.World;

public class Magic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5682897165216458913L;
	public static final int FAILED_SPELL_GRAPHIC = 629;
	private final Entity entity;
	private Attack attack = new Attack(4, 5);
	private Animation animation = null;
	private Graphic start = null;
	private Graphic end = null;
	private Projectile projectile = null;
	private byte pDelay = 0;
	private int nextHit = 0;

	private boolean multi = false;

	public Magic(Entity entity) {
		this.entity = entity;
	}

	public void execute(Entity attacking) {
		if (attack == null) {
			return;
		}

		entity.getCombat().updateTimers(attack.getAttackDelay());
		System.out.println(animation);
		if (animation != null) {
			entity.getUpdateFlags().sendAnimation(animation);
		}

		if (start != null) {
			entity.getUpdateFlags().sendGraphic(start);
		}

		if (projectile != null) {
			final int lockon = attacking.isNpc() ? attacking.getIndex() + 1 : -attacking.getIndex() - 1;
			final int offsetX = ((entity.getLocation().getY() - attacking.getLocation().getY()) * -1) - 2;
			final int offsetY = ((entity.getLocation().getX() - attacking.getLocation().getX()) * -1) - 3;
			if (pDelay > 0) {
				TaskQueue.queue(new RunOnceTask(entity, pDelay) {
					/**
					 * 
					 */
					private static final long serialVersionUID = -7672242452325939929L;

					@Override
					public void onStop() {
						World.sendProjectile(projectile, CombatConstants.getOffsetProjectileLocation(entity), lockon, (byte) offsetX, (byte) offsetY);
					}
				});
			} else {
				World.sendProjectile(projectile, CombatConstants.getOffsetProjectileLocation(entity), lockon, (byte) offsetX, (byte) offsetY);
			}
		}

		entity.doConsecutiveAttacks(attacking);
		finish(attacking);
	}

	public void finish(Entity attacking) {
		boolean success = Combat.next(100) <= entity.getAccuracy(attacking, CombatTypes.MAGIC);

		if (nextHit > -1) {
			success = true;
		} else if (nextHit == -1) {
			success = false;
		}
		
		int damage = nextHit == -2 ? entity.getCorrectedDamage(Combat.next(entity.getMaxHit(CombatTypes.MAGIC) + 1)) : nextHit;

		if (nextHit != -2) {
			nextHit = -2;
		}
		
		Hit hit = new Hit(entity, (success || entity.isNpc()) || (entity.isIgnoreHitSuccess()) ? damage : -1, Hit.HitTypes.MAGIC);

		entity.onAttack(attacking, hit.getDamage(), CombatTypes.MAGIC, success || entity.isNpc());

		entity.setLastDamageDealt(hit.getDamage());
		entity.setLastHitSuccess((success || entity.isNpc()) || (entity.isIgnoreHitSuccess()));

		if (hit.getDamage() > -1) {
			TaskQueue.queue(new HitTask(attack.getHitDelay(), false, hit, attacking));
		}

		Graphic end = null;

		if ((success || entity.isNpc()) && (this.end != null))
			end = this.end;
		else if (!success && !entity.isNpc()) {
			end = new Graphic(629, 0, true);
		}
		
		if (!success) {
			attacking.retaliate(entity);
		}

		if (end != null) {
			TaskQueue.queue(new GraphicTask(attack.getHitDelay(), false, end, attacking));
		}
		attacking.getCombat().setInCombat(entity);
	}

	public void setAttack(Attack attack, Animation animation, Graphic start, Graphic end, Projectile projectile) {
		this.attack = attack;
		this.animation = animation;
		this.start = start;
		this.end = end;
		this.projectile = projectile;
	}

	public Attack getAttack() {
		return attack;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public boolean isMulti() {
		return multi;
	}

	public byte getpDelay() {
		return pDelay;
	}

	public void setpDelay(byte pDelay) {
		this.pDelay = pDelay;
	}
	
	public void setNextHit(int hit) {
		nextHit = hit;
	}
}
