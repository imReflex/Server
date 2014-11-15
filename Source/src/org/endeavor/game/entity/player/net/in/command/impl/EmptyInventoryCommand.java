package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class EmptyInventoryCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		player.getInventory().clear();
	}

	@Override
	public int rightsRequired() {
		return 4;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
