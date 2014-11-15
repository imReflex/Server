package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class TeleportCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		switch (args[0]) {
		case "xteleto":
			String username = command.substring(8);
			for (Player players : World.getPlayers()) {
				if (players != null) {
					if (players.getUsername().equalsIgnoreCase(username)) {
						if (!PlayerConstants.isOwner(player) && players.getController().equals(DungConstants.DUNG_CONTROLLER)) {
							player.send(new SendMessage("You cannot teleport to a player in Dungeoneering, contact management."));
							return;
						} else
						player.teleport(players.getLocation());
					}
				}
			}
			break;
		case "xteletome":
			String user = command.substring(10);
			for (Player players : World.getPlayers()) {
				if (players != null) {
					if (players.getUsername().equalsIgnoreCase(user)) {
						if (!PlayerConstants.isOwner(player) && players.getController().equals(DungConstants.DUNG_CONTROLLER)) {
							player.send(new SendMessage("You cannot teleport a player out of Dungeoneering, contact management."));
							return;
						} else
						players.teleport(player.getLocation());
					}
				}
			}
			break;
		}
	}

	@Override
	public int rightsRequired() {
		return 3;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
