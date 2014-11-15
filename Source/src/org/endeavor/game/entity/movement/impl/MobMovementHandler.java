package org.endeavor.game.entity.movement.impl;

import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.Walking;
import org.endeavor.game.entity.movement.MovementHandler;

public class MobMovementHandler extends MovementHandler {
	private final Mob mob;

	public MobMovementHandler(Mob mob) {
		super(mob);
		this.mob = mob;
	}

	@Override
	public void process() {
	}

	@Override
	public boolean moving() {
		return mob.getFollowing().isFollowing();
	}

	@Override
	public boolean canMoveTo(int direction) {
		return Walking.canMoveTo(mob, direction);
	}

	@Override
	public boolean canMoveTo(int x, int y, int size, int direction) {
		return Walking.canMoveTo(mob, x, y, direction, size);
	}
}
