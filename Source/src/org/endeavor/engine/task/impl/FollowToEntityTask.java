package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.following.Following.FollowType;

public abstract class FollowToEntityTask extends Task {

	private final Entity p;
	private final Location location;

	private final int minX;
	private final int maxX;
	private final int minY;
	private final int maxY;

	public FollowToEntityTask(Entity p, Entity e) {
		super(p, 1, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskConstants.CURRENT_ACTION);
		this.p = p;
		p.getFollowing().setFollow(e, FollowType.FOLLOW_TO);
		location = p.getLocation();

		minX = e.getLocation().getX() - 1;
		maxX = minX + e.getSize() + 1;
		minY = e.getLocation().getY() - 1;
		maxY = minY + e.getSize() + 1;
	}

	@Override
	public void execute() {
		int pX = location.getX();
		int pY = location.getY();

		if (pX >= minX && pX <= maxX && pY >= minY && pY <= maxY) {
			onDestination();
			stop();
		}
	}

	public abstract void onDestination();

	@Override
	public void onStop() {
		p.getFollowing().reset();
	}

}
