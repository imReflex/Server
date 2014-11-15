package org.endeavor.game.content.minigames.fightpits;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class FightPits {
	private static final List<Player> waiting = new LinkedList<Player>();

	private static final List<Player> inGame = new LinkedList<Player>();

	private static String champion = "TzHaar-Xil-Huz";

	private static int startTime = 50;

	private static int endTime = 0;

	public static final void tick() {
		if ((waiting.size() == 0) && (endTime == 0)) {
			if (startTime != 50) {
				startTime = 50;
			}
			return;
		}

		if ((startTime > 0) && (endTime == 0))
			startTime -= 1;
		else if ((startTime == 0) && (endTime == 0)) {
			if (waiting.size() + inGame.size() > 1)
				startGame();
			else {
				sendStartFailure();
			}
		}

		if (endTime > 0) {
			if (inGame.size() == 1) {
				endGame(false);
				return;
			}
			if (endTime > 1) {
				endTime -= 1;
				if (inGame.size() == 0)
					endGame(true);
			} else if (endTime == 1) {
				endGame(true);
			}
		}
	}

	public static final void startGame() {
		for (Iterator<Player> i = inGame.iterator(); i.hasNext();) {
			Player p = (Player) i.next();

			if (!p.isActive()) {
				i.remove();
			} else {
				updatePlayerForGame(p);
			}
		}
		for (Iterator<Player> i = waiting.iterator(); i.hasNext();) {
			Player p = (Player) i.next();

			if (!p.isActive()) {
				i.remove();
			} else {
				updatePlayerForGame(p);

				p.teleport(getRandomizedStartLocation());
				p.setController(ControllerManager.FIGHT_PITS_CONTROLLER);

				inGame.add(p);
				i.remove();
			}
		}
		endTime = 500;
		startTime = 50;
	}

	public static final void sendStartFailure() {
		startTime = 50;

		for (Iterator<Player> i = waiting.iterator(); i.hasNext();) {
			Player p = (Player) i.next();

			if (!p.isActive()) {
				i.remove();
			} else {
				p.getClient().queueOutgoingPacket(new SendMessage("There are not enough players to start Fight Pits!"));
			}
		}
	}

	public static final void endGame(boolean failure) {
		endTime = 0;

		if (failure) {
			for (Iterator<Player> i = inGame.iterator(); i.hasNext();) {
				Player p = (Player) i.next();

				p.teleport(ControllerManager.FIGHT_PITS_CONTROLLER.getRespawnLocation(p));
				p.setController(ControllerManager.FIGHT_PITS_WAITING_CONTROLLER);
				i.remove();
				waiting.add(p);
			}
		} else {
			Player winner = inGame.get(0);
			if (winner == null) {
				return;
			}

			champion = winner.getUsername();
			winner.getSkulling().setSkullIcon(winner, 1);
		}
	}

	public static final void updatePlayerForGame(Player p) {
		if (p.isPoisoned()) {
			p.curePoison(0);
		}

		p.getSkill().updateCombatLevel();
	}

	public static final void onPlayerDeath(Player p) {
		inGame.remove(p);
		waiting.add(p);
		p.setController(ControllerManager.FIGHT_PITS_WAITING_CONTROLLER);
	}

	public static final Location getRandomizedStartLocation() {
		int x = 2383 + Misc.randomNumber(30);
		int y = 5133 + Misc.randomNumber(27);

		while (Region.getStaticClip(x, y, 0) != 0) {
			x = 2383 + Misc.randomNumber(30);
			y = 5133 + Misc.randomNumber(27);
		}

		return new Location(x, y);
	}

	public static final boolean clickObject(Player p, int id) {
		switch (id) {
		case 9369:
			if ((!p.getController().equals(ControllerManager.FIGHT_PITS_WAITING_CONTROLLER))
					&& (p.getController().equals(ControllerManager.DEFAULT_CONTROLLER))) {
				p.setController(ControllerManager.FIGHT_PITS_WAITING_CONTROLLER);
				p.teleport(FightPitsConstants.INTO_WAITING_LOCATION);
				waiting.add(p);
			} else {
				removeFromWaitingRoom(p);
			}
			return true;
		case 9368:
			if (!p.getController().equals(ControllerManager.FIGHT_PITS_CONTROLLER)) {
				p.getClient().queueOutgoingPacket(new SendMessage("You will be teleported in when the game starts."));
			} else {
				inGame.remove(p);
				waiting.add(p);
				p.teleport(p.getController().getRespawnLocation(p));
				p.setController(ControllerManager.FIGHT_PITS_WAITING_CONTROLLER);
			}
			return true;
		}

		return false;
	}

	public static final void updateInterface(Player p) {
		p.getClient().queueOutgoingPacket(
				new SendString("Current Champion: " + champion, FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[0]));
		if (endTime == 0) {
			p.getClient().queueOutgoingPacket(
					new SendString("Time until next game: " + startTime / 2,
							FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));
			p.getClient().queueOutgoingPacket(
					new SendString("Players waiting: " + (waiting.size() + inGame.size()),
							FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[2]));
		} else {
			p.getClient().queueOutgoingPacket(
					new SendString("Time until game ends: " + endTime / 2,
							FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));
			p.getClient().queueOutgoingPacket(
					new SendString("Foes Remaining: " + inGame.size(),
							FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[2]));
		}
	}

	public static final void removeFromWaitingRoom(Player p) {
		waiting.remove(p);
		p.teleport(FightPitsConstants.OUT_OF_WAITING_LOCATION);
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
		if (p.getSkulling().getSkullIcon() == 1) {
			if (p.getSkulling().isSkulled())
				p.getSkulling().setSkullIcon(p, 0);
			else
				p.getSkulling().setSkullIcon(p, -1);
		}
	}

	public static void removeFromGame(Player p) {
		inGame.remove(p);
		p.teleport(FightPitsConstants.OUT_OF_WAITING_LOCATION);
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
	}
}
