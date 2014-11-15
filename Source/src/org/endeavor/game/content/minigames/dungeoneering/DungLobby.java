package org.endeavor.game.content.minigames.dungeoneering;

import java.util.ArrayList;
import java.util.Iterator;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.pets.Pets;
import org.endeavor.game.content.skill.prayer.BoneBurying;
import org.endeavor.game.content.skill.summoning.SummoningConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class DungLobby {
	public static final DungLobbyController DWR_CONTROLLER = new DungLobbyController();

	private static final ArrayList<Player> waiting = new ArrayList<Player>();

	private static int timer = 50;

	private static boolean disabled = false;

	public static void process() {
		if (waiting.size() == 0) {
			if (timer != 50)
				timer = 50;
		} else {
			timer -= 1;

			if (timer <= 0) {
				start();
				timer = 50;
			}
		}
	}

	public static void start() {
		ArrayList<Player> toGame = new ArrayList<Player>();

		int c = 0;

		for (Iterator<Player> i = waiting.iterator(); i.hasNext();) {
			toGame.add(i.next());

			c++;
			if ((c == 6) || (!i.hasNext())) {
				c = 0;
				new DungGame(toGame);
				toGame = new ArrayList<Player>();
			}

			i.remove();
		}
	}

	public static boolean clickNpc(Player p, Mob m, int id, int op) {
		if ((p.getDungGame() != null) && (p.getDungGame().clickMob(p, m, id, op))) {
			return true;
		}

		return false;
	}

	public static boolean clickObject(Player p, int id, int op, int x, int y, int z) {
		if (id == 4469) {
			if (p.getController().equals(ControllerManager.DEFAULT_CONTROLLER) || p.getController().equals(DWR_CONTROLLER)) {
				doWalkThroughDoor(p, p.getLocation().getX() >= 1832);
			}

			return true;
		}

		if ((p.getDungGame() != null) && (p.getDungGame().clickObject(p, op, id, x, y, z))) {
			return true;
		}

		return false;
	}

	public static void doWalkThroughDoor(Player p, boolean in) {
		if (disabled) {
			p.getClient().queueOutgoingPacket(new SendMessage("Dungeoneering is disabled for now."));
			return;
		}

		if ((p.getPets().hasPet()) || (p.getSummoning().hasFamiliar())) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot take familiars into the minigame."));
			return;
		}

		for (Item i : p.getInventory().getItems()) {
			if (((i != null) && (Pets.isItemPet(i.getId())))
					|| ((i != null) && (SummoningConstants.getFamiliarForPouch(i.getId()) != null))) {
				p.getClient().queueOutgoingPacket(new SendMessage("You cannot take Familiars into the minigame."));
				return;
			}
		}

		for (Item i : p.getInventory().getItems()) {
			if ((i != null) && (BoneBurying.Bones.forId(i.getId()) != null)) {
				p.getClient().queueOutgoingPacket(new SendMessage("You cannot take bones into this minigame."));
				return;
			}
		}

		if (ItemCheck.hasConsumables(p)) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot take consumables into this minigame."));
			return;
		}

		if (ItemCheck.hasHerbloreIngredients(p)) {
			p.getClient().queueOutgoingPacket(
					new SendMessage("You cannot take herblore ingredients into this minigame."));
			return;
		}

		p.teleport(in ? new Location(1831, 5215) : new Location(1832, 5215));

		if (in) {
			add(p);
			p.setController(DWR_CONTROLLER);
		} else {
			remove(p);
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
		}
	}

	public static void add(Player p) {
		if (!waiting.contains(p)) {
			waiting.add(p);
			updatePlayersWaiting();
		}
	}

	public static void remove(Player p) {
		waiting.remove(p);
		updatePlayersWaiting();
	}

	public static void updatePlayersWaiting() {
		Misc.sendPacketToPlayers(new SendString("Players ready: " + waiting.size(), 17602), waiting);
	}

	public static int getPlayersReady() {
		return waiting.size();
	}

	public static int getSecondsToNextGame() {
		return timer / 2;
	}

	public static boolean isDisabled() {
		return disabled;
	}

	public static void setDisabled(boolean set) {
		disabled = set;
	}
}
