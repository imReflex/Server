package org.endeavor.engine.task.impl.forcewalk;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;

public abstract class CrossGangPlankTask extends ForceMovementTask {

	private byte steps = 0;
	private final boolean on;

	public CrossGangPlankTask(Player player, Location dest, Controller to, boolean on) {
		super(player, new Location(dest.getX(), dest.getY(), 1), to);
		this.on = on;
	}

	@Override
	public void execute() {
		if (++steps == 2) {
			player.teleport(new Location(player.getLocation().getX(), player.getLocation().getY(), on ? 1 : 0));
		} else if (steps > 6) {// just in case
			player.getMovementHandler().reset();
			player.teleport(dest);
			onDestination();
			player.setController(to);
			player.getUpdateFlags().sendFaceToDirection(player.getLocation().getX() - xMod,
					player.getLocation().getY() - yMod);
			stop();
			return;
		}

		player.getMovementHandler().walkTo(xMod, yMod);
		if (player.getLocation().getX() + xMod == dest.getX() && player.getLocation().getY() + yMod == dest.getY()) {
			onDestination();
			player.setController(to);
			player.getUpdateFlags().sendFaceToDirection(player.getLocation().getX() - xMod,
					player.getLocation().getY() - yMod);
			stop();
		}
	}
}
