package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class SetSpecialCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		player.getSpecialAttack().setSpecialAmount(100);
		player.getEquipment().updateSidebar();
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
