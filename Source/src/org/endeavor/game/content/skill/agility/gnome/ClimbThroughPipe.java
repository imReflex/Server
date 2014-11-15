package org.endeavor.game.content.skill.agility.gnome;

import org.endeavor.engine.task.impl.forcewalk.ForceMovementTask;
import org.endeavor.game.content.skill.agility.GnomeAgilityObjects;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public class ClimbThroughPipe extends ForceMovementTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8958519425832107552L;
	private final Player p;
	private final int walk;
	private final boolean run;
	private final double exp;
	private final Location to;

	public ClimbThroughPipe(Player player, Location to, Location start, double exp) {
		super(player, to, ControllerManager.DEFAULT_CONTROLLER);
		walk = player.getAnimations().getWalkEmote();
		run = player.getRunEnergy().isRunning();
		this.exp = exp;
		this.to = to;
		p = player;
		if (!stopped()) {
			player.teleport(start);
			player.setAppearanceUpdateRequired(true);
			player.getAnimations().setWalkEmote(844);
			player.getRunEnergy().setRunning(false);
		}
	}

	@Override
	public void onDestination() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.setAppearanceUpdateRequired(true);
		player.getSkill().addExperience(16, exp);
		player.teleport(new Location(to.getX(), to.getY() + 2));
		GnomeAgilityObjects.onFinishCourse(p);
	}
}
