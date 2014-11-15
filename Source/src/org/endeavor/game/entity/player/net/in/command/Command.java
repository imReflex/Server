package org.endeavor.game.entity.player.net.in.command;

import org.endeavor.game.entity.player.Player;

public abstract interface Command {
	public abstract void handleCommand(Player paramPlayer, String paramString);

	public abstract int rightsRequired();

	public abstract boolean meetsRequirements(Player paramPlayer);
}
