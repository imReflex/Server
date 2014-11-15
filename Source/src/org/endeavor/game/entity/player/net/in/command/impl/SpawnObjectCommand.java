package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class SpawnObjectCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		ObjectManager.register(new GameObject(Integer.parseInt(args[1]), player.getLocation().getX(), player
				.getLocation().getY(), player.getLocation().getZ(), 10, 0));
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
