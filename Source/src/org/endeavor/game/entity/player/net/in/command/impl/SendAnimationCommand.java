package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class SendAnimationCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		player.getUpdateFlags().sendAnimation(Integer.parseInt(command.substring(5)), 0);
	}

	@Override
	public int rightsRequired() {
		return 7;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
