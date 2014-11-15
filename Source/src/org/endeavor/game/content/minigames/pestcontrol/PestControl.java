package org.endeavor.game.content.minigames.pestcontrol;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PestControl {
	private static final List<PestControlGame> games = new LinkedList<PestControlGame>();

	private static final Queue<Player> waiting = new ArrayDeque<Player>();

	private static short time = 10;

	public static void tick() {
		if (waiting.size() > 0) {
			time--;
			
			if (time == 0 || waiting.size() == 25) {
				startGame();
				time = 200;
			}
		} else if (time != 200) {
			time = 200;
		}

		if (games.size() > 0) {
			for (Iterator<PestControlGame> i = games.iterator(); i.hasNext();) {
				i.next().process();
			}
		}
	}
	
	public static void onGameEnd(PestControlGame game) {
		games.remove(game);
	}

	public static void startGame() {
		if (waiting.size() < 2) {
			sendMessageToWaiting("There are not enough players to start Pest Control.");
			return;
		}

		if (games.size() == 3) {
			sendMessageToWaiting("There are already 3 Pest Control games running, you must wait!");
			return;
		}
		
		List<Player> toPlay = new LinkedList<Player>();

		int playing = 0;
		Player p;

		while ((playing < 25) && ((p = waiting.poll()) != null)) {
			toPlay.add(p);
			playing++;
		}
		
		if (waiting.size() > 0) {
			for (Player k : waiting) {
				k.getClient().queueOutgoingPacket(new SendMessage("You couldn't be added to the last game, you've moved up in priority for the next."));
			}
		}
		
		games.add(new PestControlGame(toPlay, toPlay.get(0).getIndex() << 2));
	}

	public static boolean clickObject(Player p, int id) {
		switch (id) {
		case 14315:
			if (!p.getController().equals(ControllerManager.PEST_WAITING_ROOM_CONTROLLER)) {
				p.setController(ControllerManager.PEST_WAITING_ROOM_CONTROLLER);
				p.teleport(new Location(2661, 2639));
				
				if (!waiting.contains(p)) {
					waiting.add(p);
				}
			}
			return true;
		case 14314:
			if (!p.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
				p.setController(ControllerManager.DEFAULT_CONTROLLER);
				p.teleport(new Location(2657, 2639));
				waiting.remove(p);
			}
			return true;
		}

		return false;
	}

	public static void sendMessageToWaiting(String message) {
		for (Player p : waiting) {
			p.getClient().queueOutgoingPacket(new SendMessage(message));
		}
	}

	public static int getMinutesTillDepart() {
		return time / 100;
	}

	public static int getPlayersReady() {
		return waiting.size();
	}
}
