package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public abstract class PullLeverTask extends Task {
	private final Player player;
	private final int minX;
	private final int maxX;
	private final int minY;
	private final int maxY;
	private Location location;
	private byte wait = 0;

	public PullLeverTask(Player player, int x, int y, int xLength, int yLength) {
		super(player, 1, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		location = player.getLocation();
		minX = (x - 1);
		maxX = (minX + xLength + 1);
		minY = (y - 1);
		maxY = (minY + yLength + 1);
	}

	@Override
	public void execute() {
		int pX = location.getX();
		int pY = location.getY();

		if ((pX >= minX) && (pX <= maxX) && (pY >= minY) && (pY <= maxY)) {
			if (wait == 0) {
				player.getUpdateFlags().sendAnimation(2140, 0);
			}

			if (wait == 2) {
				onDestination();
				stop();
			}

			wait = ((byte) (wait + 1));
		}
	}

	@Override
	public void onStop() {
	}

	public abstract void onDestination();
}
