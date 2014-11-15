package org.endeavor.game.content.minigames.armsrace;

import java.util.ArrayList;
import java.util.List;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class ArmsRaceLobby {
	private static final List<Player> waiting = new ArrayList<Player>();

	private static int timer = 300;

	private static ARGame game = null;

	private static boolean disabled = false;

	public static void process() {
		if (game != null) {
			game.process();
		}

		if (waiting.size() == 0) {
			if (timer != 15) {
				timer = 15;
			}
		} else if (game == null) {
			timer -= 1;

			sendPacketToLobby(new SendString("Time until next game: " + getMinutesUntilNextGame(),
					org.endeavor.game.content.minigames.fightpits.FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));

			if (timer == 0) {
				start();
			}
		}
	}

	public static void onEndGame() {
		game = null;
	}

	public static void start() {
		timer = 300;

		if (waiting.size() < 2) {
			for (Player p : waiting) {
				p.getClient().queueOutgoingPacket(new SendMessage("There aren't enough players to start the mini-game!"));
			}
			
			return;
		}
		
		Player[] next = new Player[waiting.size()];

		for (int i = 0; i < next.length; i++) {
			next[i] = (waiting.get(i));
		}

		game = new ARGame(next);

		waiting.clear();
	}

	public static void add(Player p) {
		if (!p.isBetaTester() && !PlayerConstants.isOwner(p) && p.getRights() < 2) {
			p.getClient().queueOutgoingPacket(new SendMessage("You are not allowed to play this minigame yet."));
			return;
		}
		
		if (p.getSummoning().hasFamiliar() || p.getPets().hasPet()) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot take familiars into here."));
			return;
		}
		
		if (disabled) {
			p.getClient().queueOutgoingPacket(new SendMessage("This minigame is disabled."));
			return;
		}
		
		for (Item i : p.getInventory().getItems()) {
			if (i != null) {
				p.getClient().queueOutgoingPacket(new SendMessage("You cannot take any items into this minigame."));
				return;
			}
		}
		
		for (Item i : p.getEquipment().getItems()) {
			if (i != null) {
				p.getClient().queueOutgoingPacket(new SendMessage("You cannot take any items into this minigame."));
				return;
			}
		}
		
		waiting.add(p);
		p.setController(ARConstants.AR_WAIT_CONTROLLER);
		p.teleport(ARConstants.TO_WAITING);
		
		sendPacketToLobby(new SendString("Waiting: " + getPlayersWaiting(),
				org.endeavor.game.content.minigames.fightpits.FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[0]));
	}

	public static void remove(Player p) {
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
		p.teleport(ARConstants.FROM_WAITING);
		waiting.remove(p);

		sendPacketToLobby(new SendString("Waiting: " + getPlayersWaiting(),
				org.endeavor.game.content.minigames.fightpits.FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[0]));
	}

	public static void sendPacketToLobby(OutgoingPacket o) {
		for (Player p : waiting)
			p.getClient().queueOutgoingPacket(o);
	}

	public static boolean clickObject(Player p, int id) {
		switch (id) {
		case 30224:
			if (!waiting.contains(p))
				add(p);
			else {
				remove(p);
			}
			return true;
		}

		return false;
	}

	public static boolean isGameActive() {
		return game != null;
	}

	public static int getPlayersWaiting() {
		return waiting.size();
	}

	public static int getMinutesUntilNextGame() {
		return timer / 100 + 1 + (game != null ? game.getMinutesTillEnd() : 0);
	}

	public static ARGame getGame() {
		return game;
	}

	public static void setDisabled(boolean set) {
		disabled = set;
	}
}
