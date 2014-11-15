package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.impl.GroundItem;
import org.endeavor.game.entity.player.Player;

public abstract class WalkToTask extends Task {

	private final int minX;
	private final int maxX;
	private final int minY;
	private final int maxY;
	private Location location;

	public WalkToTask(Player player, int x, int y, int xLength, int yLength) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskConstants.CURRENT_ACTION);
		location = player.getLocation();
		minX = x - 1;
		maxX = minX + xLength + 1;
		minY = y - 1;
		maxY = minY + yLength + 1;
	}

	public WalkToTask(Player player, Player other) {
		this(player, other.getLocation().getX(), other.getLocation().getY(), 1, 1);
	}

	public WalkToTask(Player player, GroundItem ground) {
		this(player, ground.getLocation().getX(), ground.getLocation().getY(), 1, 1);
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

	@Override
	public void onStop() {
	}

	public abstract void onDestination();
}
