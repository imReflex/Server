package org.endeavor.engine.task.impl.forcewalk;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public class JumpObjectTask extends Task {

	private final Player p;
	private final Location dest;
	private final Controller start;

	public JumpObjectTask(Player p, Location dest) {
		super(p, 1);
		this.p = p;
		this.dest = dest;
		this.start = p.getController();

		p.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);
	}

	@Override
	public void execute() {
		stop();
	}

	@Override
	public void onStop() {
		p.teleport(dest);
		p.setController(start);
	}

}
