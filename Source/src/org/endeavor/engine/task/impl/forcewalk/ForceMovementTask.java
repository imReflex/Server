package org.endeavor.engine.task.impl.forcewalk;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public abstract class ForceMovementTask extends Task {

	protected final Player player;
	protected final Controller start;
	protected final Controller to;

	protected final Location dest;
	protected final int xMod;
	protected final int yMod;

	public ForceMovementTask(Player player, Location dest, Controller to) {
		super(player, 1);
		this.player = player;
		this.dest = dest;
		this.to = to;
		start = player.getController();
		player.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);

		int xDiff = player.getLocation().getX() - dest.getX();
		int yDiff = player.getLocation().getY() - dest.getY();

		if (xDiff != 0)
			xMod = (xDiff < 0 ? 1 : -1);
		else
			xMod = 0;
		if (yDiff != 0)
			yMod = (yDiff < 0 ? 1 : -1);
		else
			yMod = 0;
		if (xDiff != 0 && yDiff != 0) {
			stop();
			player.setController(start);
		} else {
			player.getMovementHandler().reset();
		}
	}

	@Override
	public void execute() {
		player.getMovementHandler().setForceMove(true);
		player.getMovementHandler().walkTo(xMod, yMod);
		if (player.getLocation().getX() + xMod == dest.getX() && player.getLocation().getY() + yMod == dest.getY()) {
			onDestination();
			player.setController(to);
			player.getUpdateFlags().sendFaceToDirection(player.getLocation().getX() - xMod,
					player.getLocation().getY() - yMod);
			stop();
		}
	}

	public abstract void onDestination();

	@Override
	public void onStop() {
	}
}
