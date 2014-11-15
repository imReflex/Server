package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Entity;

/**
 * Represents a hit delay function.
 * 
 * @author Michael Sasse
 * 
 */
public class HitTask extends Task {

	/**
	 * The graphic.
	 */
	private final Hit hit;
	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * Creates a new graphic to queue.
	 * 
	 * @param graphic
	 *            the graphic.
	 * @param delay
	 *            the action delay.
	 */
	public HitTask(int delay, boolean immediate, Hit hit, Entity entity) {
		super(entity, delay, immediate, StackType.STACK, BreakType.NEVER, 0);
		this.hit = hit;
		this.entity = entity;
		if (delay <= 1) {
			sendBlockAnimation();
		} else {
			final Task t = this;
			
			TaskQueue.queue(new Task(delay - 1) {

				@Override
				public void execute() {
					if (t.stopped()) {
						stop();
						return;
					}
					
					sendBlockAnimation();
					stop();
				}

				@Override
				public void onStop() {
				}
			});
		}
	}

	@Override
	public void execute() {
		entity.hit(hit);
		stop();
	}

	public void sendBlockAnimation() {
		if (hit.getAttacker() != null && entity.getCombat().getBlockAnimation() != null && !entity.isDead()) {
			int a = entity.getCombat().getAttackTimer();
			if (a != entity.getCombat().getAttackCooldown() || entity.getCombat().getAttacking() == null) {
				entity.getUpdateFlags().sendAnimation(entity.getCombat().getBlockAnimation());
			}
		}
	}

	@Override
	public void onStop() {
	}
	
	public Entity getEntity() {
		return entity;
	}
}
