package org.endeavor.game.content.skill.agility.gnome;

import org.endeavor.engine.task.impl.forcewalk.ForceMovementTask;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public class BalanceOverObject extends ForceMovementTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7187628264462909043L;
	private final int walk;
	private final boolean run;
	private final double exp;

	public BalanceOverObject(Player player, Location l, double exp) {
		super(player, l, ControllerManager.DEFAULT_CONTROLLER);
		walk = player.getAnimations().getWalkEmote();
		run = player.getRunEnergy().isRunning();
		this.exp = exp;
		if (!stopped()) {
			player.setAppearanceUpdateRequired(true);
			player.getAnimations().setWalkEmote(762);
			player.getRunEnergy().setRunning(false);
		}
	}

	@Override
	public void onDestination() {
		player.getAnimations().setWalkEmote(walk);
		player.getRunEnergy().setRunning(run);
		player.setAppearanceUpdateRequired(true);
		player.getSkill().addExperience(16, exp);
	}
}
