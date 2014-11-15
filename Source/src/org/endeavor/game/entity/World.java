package org.endeavor.game.entity;

import java.util.ArrayList;
import java.util.List;
import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.MobUpdateList;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.combat.CombatConstants;
import org.endeavor.game.content.dwarfcannon.Cannon;
import org.endeavor.game.content.minigames.armsrace.ArmsRaceLobby;
import org.endeavor.game.content.minigames.dungeoneering.DungLobby;
import org.endeavor.game.content.minigames.fightpits.FightPits;
import org.endeavor.game.content.minigames.pestcontrol.PestControl;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobUpdateFlags;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerUpdateFlags;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.impl.SendGameUpdateTimer;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendNPCUpdate;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerUpdate;
import org.endeavor.game.entity.player.net.out.impl.SendProjectile;
import org.endeavor.game.entity.player.net.out.impl.SendStillGraphic;

/**
 * Handles the in-game world
 * 
 * @author Michael Sasse
 * 
 */
public class World {
	
	/**
	 * The maximum amount of players that can be processed
	 */
	public static final short MAX_PLAYERS = 2048;
	
	/**
	 * The maximum amount of mobs available in the in-game world
	 */
	public static final short MAX_MOBS = 8192;
	
	/**
	 * A list of players registered into the game world
	 */
	private static final Player[] players = new Player[MAX_PLAYERS];
	
	/**
	 * A list of mobs registered into the game world
	 */
	private static final Mob[] mobs = new Mob[MAX_MOBS];

	/**
	 * The servers cycles?
	 */
	private static long cycles = 0L;

	/**
	 * A list of updated mobs
	 */
	private static MobUpdateList mobUpdateList = new MobUpdateList();

	/**
	 * A list of cannons in-game
	 */
	private static List<Cannon> cannons = new ArrayList<Cannon>();

	/**
	 * The current server update timer
	 */
	private static short updateTimer = -1;
	
	/**
	 * The server is being updated
	 */
	private static boolean updating = false;

	/**
	 * is the tick ignored
	 */
	private static boolean ignoreTick = false;

	/**
	 * Handles processing the main game world
	 */
	public static void process() {
		PlayerUpdateFlags[] pFlags = new PlayerUpdateFlags[players.length];
		MobUpdateFlags[] nFlags = new MobUpdateFlags[mobs.length];
		try {
			FightPits.tick();
			PestControl.tick();
			// HungerGamesLobby.tick();
			DungLobby.process();
			ArmsRaceLobby.process();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Cannon c : cannons) {
			c.tick();
		}

		for (int i = 1; i < 2048; i++) {
			Player player = players[i];
			try {
				if (player != null) {
					if (!player.isActive()) {
						if (player.getClient().getStage() == Client.Stages.LOGGED_IN) {
							player.setActive(true);
							player.start();
							
							player.getClient().resetLastPacketReceived();
						} else if (getCycles() - player.getClient().getLastPacketTime() > 30) {
							player.logout(true);
						}
					}

					player.getClient().processIncomingPackets();

					player.process();

					player.getClient().reset();

					for (Cannon c : cannons)
						if (c.getLoc().isViewableFrom(player.getLocation()))
							c.rotate(player);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				if (player != null) {
					player.logout(true);
				}
			}

		}

		for (int i = 0; i < mobs.length; i++) {
			Mob mob = mobs[i];
			if (mob != null) {
				try {
					mob.process();
				} catch (Exception e) {
					e.printStackTrace();
					mob.remove();
				}
			}
		}

		for (int i = 1; i < 2048; i++) {
			Player player = players[i];
			if ((player == null) || (!player.isActive()))
				pFlags[i] = null;
			else {
				try {
					player.getMovementHandler().process();
					pFlags[i] = new PlayerUpdateFlags(player);
				} catch (Exception ex) {
					ex.printStackTrace();
					player.logout(true);
				}
			}
		}
		for (int i = 0; i < mobs.length; i++) {
			Mob mob = mobs[i];
			if (mob != null) {
				try {
					mob.processMovement();
					nFlags[mob.getIndex()] = new MobUpdateFlags(mob);
				} catch (Exception e) {
					e.printStackTrace();
					mob.remove();
				}
			}
		}

		for (int i = 1; i < 2048; i++) {
			Player player = players[i];
			if ((player != null) && (pFlags[i] != null) && (player.isActive())) {
				try {
					player.getClient().queueOutgoingPacket(new SendPlayerUpdate(pFlags));
					player.getClient().queueOutgoingPacket(new SendNPCUpdate(nFlags, pFlags[i]));
				} catch (Exception ex) {
					ex.printStackTrace();
					player.logout(true);
				}
			}
		}
		for (int i = 1; i < 2048; i++) {
			Player player = players[i];
			if ((player != null) && (player.isActive())) {
				try {
					player.reset();
				} catch (Exception ex) {
					ex.printStackTrace();
					player.logout(true);
				}
			}
		}
		for (int i = 0; i < mobs.length; i++) {
			Mob mob = mobs[i];
			if (mob != null) {
				try {
					mob.reset();
				} catch (Exception e) {
					e.printStackTrace();
					mob.remove();
				}
			}
		}

		if ((updateTimer > -1) && ((World.updateTimer = (short) (updateTimer - 1)) == 0)) {
			update();
		}

		if (ignoreTick) {
			ignoreTick = false;
		}

		cycles += 1L;
	}

	/**
	 * Sends a global message to all players online
	 * 
	 * @param message
	 *            The message to send to all players
	 * @param format
	 *            Should the message beformatted
	 */
	public static void sendGlobalMessage(String message, boolean format) {
		message = (format ? "<col=255>" : "") + message + (format ? "</col>" : "");

		for (Player p : players)
			if ((p != null) && (p.isActive()))
				p.getClient().queueOutgoingPacket(new SendMessage(message));
	}

	/**
	 * Initiates an in-game update
	 */
	public static void initUpdate() {
		updateTimer = 200;

		for (Player p : players)
			if (p != null)
				p.getClient().queueOutgoingPacket(new SendGameUpdateTimer(updateTimer));
	}

	/**
	 * Resets an in-game update
	 */
	public static void resetUpdate() {
		updateTimer = -1;

		synchronized (players) {
			for (Player p : players)
				if (p != null)
					p.getClient().queueOutgoingPacket(new SendGameUpdateTimer(0));
		}
	}

	/**
	 * Updates the server by disconnecting all players
	 */
	public static void update() {
		updating = true;
		for (Player p : players)
			if (p != null)
				p.logout(true);
		System.exit(0);
	}

	/**
	 * Sets a still graphic to a location
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param location
	 *            The location of the graphic
	 */
	public static void sendStillGraphic(int id, int delay, Location location) {
		for (Player player : players)
			if ((player != null) && (location.isViewableFrom(player.getLocation())))
				player.getClient().queueOutgoingPacket(new SendStillGraphic(id, location, delay));
	}

	/**
	 * Sends a projectile
	 * 
	 * @param projectile
	 *            The id of the graphic
	 * @param pLocation
	 *            The location to send the graphic too
	 * @param lockon
	 *            The lockon index
	 * @param offsetX
	 *            The x offset of the projectile
	 * @param offsetY
	 *            The y offset of the projectile
	 */
	public static void sendProjectile(Projectile projectile, Location pLocation, int lockon, byte offsetX, byte offsetY) {
		for (Player player : players)
			if (player != null) {
				if (pLocation.isViewableFrom(player.getLocation()))
					player.getClient().queueOutgoingPacket(
							new SendProjectile(player, projectile, pLocation, lockon, offsetX, offsetY));
			}
	}

	public static void sendProjectile(Projectile p, Entity e1, Entity e2) {
		int lockon = e2.isNpc() ? e2.getIndex() + 1 : -e2.getIndex() - 1;
		byte offsetX = (byte) ((e1.getLocation().getY() - e2.getLocation().getY()) * -1);
		byte offsetY = (byte) ((e1.getLocation().getX() - e2.getLocation().getX()) * -1);
		sendProjectile(p, CombatConstants.getOffsetProjectileLocation(e1), lockon, offsetX, offsetY);
	}

	/**
	 * Registers a player into the in-game world
	 * 
	 * @param player
	 *            The player to register into the game world
	 * @return
	 */
	public static int register(Player player) {
		int[] ids = new int[players.length];

		int c = 0;

		for (int i = 1; i < players.length; i++) {
			if (players[i] == null) {
				ids[c] = i;
				c++;
			}
		}

		if (c == 0) {
			return -1;
		}
		int index = ids[Misc.randomNumber(c)];

		players[index] = player;

		player.setIndex(index);

		int am = getActivePlayers();

		QuestTab.updatePlayersOnline(player, am);

		for (int k = 1; k < players.length; k++) {
			if ((players[k] != null) && (players[k].isActive())) {
				QuestTab.updatePlayersOnline(players[k], am);
				players[k].getPrivateMessaging().updateOnlineStatus(player, true);
			}
		}
		if (updateTimer > -1) {
			player.getClient().queueOutgoingPacket(new SendGameUpdateTimer(updateTimer));
		}

		return c;
	}

	/**
	 * Registers a mob into the game world
	 * 
	 * @param mob
	 *            The mob to register into the game world
	 * @return
	 */
	public static int register(Mob mob) {
		for (int i = 1; i < mobs.length; i++) {
			if (mobs[i] == null) {
				mobs[i] = mob;
				mob.setIndex(i);
				return i;
			}
		}

		return -1;
	}

	/**
	 * Unregisters a player from the game world
	 * @param player
	 * The player to unregister into the game world
	 */
	public static void unregister(Player player) {
		if ((player.getIndex() == -1) || (players[player.getIndex()] == null)) {
			return;
		}

		players[player.getIndex()] = null;

		int am = getActivePlayers();

		for (int i = 0; i < players.length; i++)
			if ((players[i] != null) && (players[i].isActive())) {
				QuestTab.updatePlayersOnline(players[i], am);
				players[i].getPrivateMessaging().updateOnlineStatus(player, false);
			}
	}

	/**
	 * Unregisters a mob from the game world
	 * 
	 * @param mob
	 *            The mob to unregister from the game world
	 */
	public static void unregister(Mob mob) {
		if (mob.getIndex() == -1) {
			return;
		}
		mobs[mob.getIndex()] = null;
		mobUpdateList.toRemoval(mob);
	}

	/**
	 * The amount of npcs registered into the game world
	 * 
	 * @return
	 */
	public static int npcAmount() {
		int amount = 0;
		for (int i = 1; i < mobs.length; i++) {
			if (mobs[i] != null) {
				amount++;
			}
		}
		return amount;
	}

	/**
	 * Checks if a player is within range to be registered 
	 * @param playerIndex
	 * The index of the player
	 * @return
	 */
	public static boolean isPlayerWithinRange(int playerIndex) {
		return (playerIndex > -1) && (playerIndex < players.length);
	}

	/**
	 * Checks if a mobs index is within range
	 * @param mobIndex
	 * @return
	 */
	public static boolean isMobWithinRange(int mobIndex) {
		return (mobIndex > -1) && (mobIndex < mobs.length);
	}

	/**
	 * Gets the list of players 
	 * @return
	 */
	public static Player[] getPlayers() {
		return players;
	}

	/**
	 * Gets a player by their username
	 * 
	 * @param username
	 *            The players username
	 * @return
	 */
	public static Player getPlayerByName(String username) {
		if (username == null) {
			return null;
		}

		long n = Misc.nameToLong(username.toLowerCase());

		for (Player p : players) {
			if ((p != null) && (p.isActive()) && (p.getUsernameToLong() == n)) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Gets a player by their name as a long
	 * 
	 * @param n
	 *            The players username as a long
	 * @return
	 */
	public static Player getPlayerByName(long n) {
		for (Player p : players) {
			if ((p != null) && (p.isActive()) && (p.getUsernameToLong() == n)) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Gets the list of in-game mobs
	 * @return
	 */
	public static Mob[] getNpcs() {
		return mobs;
	}

	public static void remove(List<Mob> local) {
	}

	/**
	 * Adds a cannon to the list
	 * 
	 * @param cannon
	 */
	public static void addCannon(Cannon cannon) {
		cannons.add(cannon);
	}

	/**
	 * Removes a cannon from the list
	 * 
	 * @param cannon
	 */
	public static void removeCannon(Cannon cannon) {
		cannons.remove(cannon);
	}

	/**
	 * Is the server being updated
	 * @return
	 */
	public static boolean isUpdating() {
		return updating;
	}
	
	private static final int randomAmount = Misc.randomNumber(3);

	/**
	 * Gets the active amount of players online
	 * 
	 * @return
	 */
	public static int getActivePlayers() {
		int r = 0;

		for (Player p : players) {
			if (p != null) {
				r++;
			}
		}

		return r;
	}

	/**
	 * Is the tick ignored
	 * 
	 * @return
	 */
	public static boolean isIgnoreTick() {
		return ignoreTick;
	}

	/**
	 * Sets the tick to be ignored
	 * 
	 * @param ignore
	 *            Should the tick be ignored
	 */
	public static void setIgnoreTick(boolean ignore) {
		ignoreTick = ignore;
	}

	/**
	 * Gets the cycles
	 * 
	 * @return
	 */
	public static long getCycles() {
		return cycles;
	}
}
