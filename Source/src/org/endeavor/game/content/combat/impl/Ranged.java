package org.endeavor.game.content.combat.impl;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.GraphicTask;
import org.endeavor.engine.task.impl.HitTask;
import org.endeavor.game.content.combat.Combat;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.World;

public class Ranged implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 77184259482533157L;
	private final Entity entity;
	private Attack attack = null;
	private Animation animation = null;
	private Graphic start = null;
	private Graphic end = null;
	private Projectile projectile = null;

	private int pOffset = 0;
	private byte gOffset = 0;

	public Ranged(Entity entity) {
		this.entity = entity;
	}

	public void execute(Entity attacking) {
		if ((attack == null) || (attacking == null) || (attacking.isDead())) {
			return;
		}

		boolean success = entity.getAccuracy(attacking, CombatTypes.RANGED) >= Combat.next(100);

		int damage = entity.getCorrectedDamage(Combat.next(entity.getMaxHit(CombatTypes.RANGED) + 1));

		Hit hit = new Hit(entity, (success) || (entity.isIgnoreHitSuccess()) ? damage : 0, Hit.HitTypes.RANGED);

		entity.setLastDamageDealt(hit.getDamage());
		entity.setLastHitSuccess((success) || (entity.isIgnoreHitSuccess()));

		entity.getCombat().updateTimers(attack.getAttackDelay());

		if (animation != null) {
			entity.getUpdateFlags().sendAnimation(animation);
		}

		if (start != null) {
			executeStartGraphic();
		}

		if (projectile != null) {
			executeProjectile(attacking);
		}

		TaskQueue.queue(new HitTask(attack.getHitDelay(), false, hit, attacking));

		if (end != null) {
			TaskQueue.queue(new GraphicTask(attack.getHitDelay(), false, end, attacking));
		}

		attacking.getCombat().setInCombat(entity);
		entity.doConsecutiveAttacks(attacking);
		entity.onAttack(attacking, hit.getDamage(), CombatTypes.RANGED, success);
	}

	public void executeStartGraphic() {
		if (gOffset > 0) {
			final Graphic g = new Graphic(start);

			TaskQueue.queue(new Task(gOffset) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 7234141034936698731L;

				@Override
				public void execute() {
					entity.getUpdateFlags().sendGraphic(g);
					stop();
				}

				@Override
				public void onStop() {
				}
			});
		} else {
			entity.getUpdateFlags().sendGraphic(start);
		}
	}

	public void executeProjectile(Entity target) {
		final int lockon = target.isNpc() ? target.getIndex() + 1 : -target.getIndex() - 1;
		final byte offsetX = (byte) ((entity.getLocation().getY() - target.getLocation().getY()) * -1);
		final byte offsetY = (byte) ((entity.getLocation().getX() - target.getLocation().getX()) * -1);

		if (pOffset > 0) {
			final Projectile p = new Projectile(projectile);
			TaskQueue.queue(new Task(pOffset) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 6529900211190431056L;

				@Override
				public void execute() {
					World.sendProjectile(p, entity.getLocation(), lockon, offsetX, offsetY);
					stop();
				}

				@Override
				public void onStop() {
				}
			});
		} else {
			World.sendProjectile(projectile, entity.getLocation(), lockon, offsetX, offsetY);
		}
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

	public void setAttack(Attack attack) {
		this.attack = attack;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setStart(Graphic start) {
		this.start = start;
	}

	public void setEnd(Graphic end) {
		this.end = end;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public void setProjectileOffset(int pOffset) {
		this.pOffset = pOffset;
	}

	public int getProjectileOffset() {
		return pOffset;
	}

	public void setStartGfxOffset(byte gOffset) {
		this.gOffset = gOffset;
	}
}
