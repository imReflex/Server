package org.endeavor.game.content.minigames.zombies;

import java.util.ArrayList;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class ZombiesLobby {
	
	private static final ArrayList<Player> lobby = new ArrayList<Player>();
	
	private static int timer = 120;
	
	public static void process() {
		if (lobby.size() == 0) {
			timer = 120;
		} else {
			if (timer-- <= 0) {
				start();
				timer = 120;
			} else {
				for (Player p : lobby) {
					p.getClient().queueOutgoingPacket(new SendString("Next game: " + timer / 2, 17601));
					p.getClient().queueOutgoingPacket(new SendString("Players ready: " + lobby.size(), 17602));
				}
			}
		}
	}
	
	public static void start() {
		while (lobby.size() > 0) {
			Player[] players = new Player[lobby.size() > 5 ? 5 : lobby.size()];
			
			for (int i = 0; i < players.length; i++) {
				players[i] = lobby.get(0);
				lobby.remove(0);
			}
			
			new ZombiesGame(players);
		}
	}
	
	public static boolean clickObject(Player player, int id) {
		switch (id) {
		case 12816:
			if (lobby.contains(player)) {
				remove(player);
				player.teleport(new Location(3485, 3244));
			} else {
				add(player);
				player.teleport(new Location(3485, 3243));
			}
			return true;
		}
		
		return false;
	}
	
	public static void add(Player player) {
		if (!lobby.contains(player)) {
			lobby.add(player);
			player.setController(ZombiesConstants.ZOMBIES_LOBBY_CONTROLLER);
		}
	}
	
	public static void remove(Player player) {
		lobby.remove(player);
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

}
