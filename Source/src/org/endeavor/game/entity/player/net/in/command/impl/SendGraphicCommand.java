package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class SendGraphicCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(Integer.parseInt(command.substring(4)), 0));
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
