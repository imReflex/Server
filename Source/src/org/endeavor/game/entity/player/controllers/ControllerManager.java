package org.endeavor.game.entity.player.controllers;

import org.endeavor.engine.task.impl.forcewalk.ForceMovementController;
import org.endeavor.game.content.minigames.duelarena.DuelArenaController;
import org.endeavor.game.content.minigames.duelarena.DuelStakeController;
import org.endeavor.game.content.minigames.duelarena.DuelingController;
import org.endeavor.game.content.minigames.fightcave.TzharrController;
import org.endeavor.game.content.minigames.fightpits.FightPits;
import org.endeavor.game.content.minigames.fightpits.FightPitsController;
import org.endeavor.game.content.minigames.fightpits.FightPitsWaitingController;
import org.endeavor.game.content.minigames.godwars.GodWarsController;
import org.endeavor.game.content.minigames.pestcontrol.PestWaitingRoomController;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;

public class ControllerManager {
	public static final DefaultController DEFAULT_CONTROLLER = new DefaultController();
	public static final WildernessController WILDERNESS_CONTROLLER = new WildernessController();
	public static final DuelArenaController DUEL_ARENA_CONTROLLER = new DuelArenaController();
	public static final DuelingController DUELING_CONTROLLER = new DuelingController();
	public static final DuelStakeController DUEL_STAKE_CONTROLLER = new DuelStakeController();
	public static final RollingDiceController ROLLING_DICE_CONTROLLER = new RollingDiceController();
	public static final FightPitsController FIGHT_PITS_CONTROLLER = new FightPitsController();
	public static final FightPitsWaitingController FIGHT_PITS_WAITING_CONTROLLER = new FightPitsWaitingController();
	public static final TzharrController TZHARR_CAVES_CONTROLLER = new TzharrController();
	public static final PestWaitingRoomController PEST_WAITING_ROOM_CONTROLLER = new PestWaitingRoomController();
	public static final ForceMovementController FORCE_MOVEMENT_CONTROLLER = new ForceMovementController();
	public static final CWSafePKController CW_SAFE_PK_CONTROLLER = new CWSafePKController();
	// public static final HGLobbyController HG_LOBBY_CONTROLLER = new
	// HGLobbyController();
	// public static final HGPreController HG_PRE_CONTROLLER = new
	// HGPreController();
	// public static final HGController HG_CONTROLLER = new HGController();
	public static final GodWarsController GOD_WARS_CONTROLLER = new GodWarsController();

	public static void setControllerOnWalk(Player player) {
		if ((player.getController() != null) && (!player.getController().transitionOnWalk(player))) {
			return;
		}

		Controller c = DEFAULT_CONTROLLER;

		if (player.inWilderness())
			c = WILDERNESS_CONTROLLER;
		else if (player.inDuelArena())
			c = DUEL_ARENA_CONTROLLER;
		else if (player.inGodwars()) {
			c = GOD_WARS_CONTROLLER;
		}

		if ((c == null) || (player.getController() == null) || (!player.getController().equals(c))) {
			player.setController(c);
		}
	}

	public static void onForceLogout(Player player) {
		Controller c = player.getController();
		if (c.equals(DUELING_CONTROLLER)) {
			player.getDueling().onForceLogout();
		} else if (c.equals(ROLLING_DICE_CONTROLLER)) {
			player.getMinigames().getBetManager().reset();
		} else if (c.equals(FIGHT_PITS_WAITING_CONTROLLER)) {
			player.teleport(new Location(2399, 5177));
			FightPits.removeFromWaitingRoom(player);
		} else if (c.equals(FIGHT_PITS_CONTROLLER)) {
			player.teleport(new Location(2399, 5177));
			FightPits.removeFromGame(player);
		} else if (c.equals(PEST_WAITING_ROOM_CONTROLLER)) {
			// TODO
		} else if (c.equals(FORCE_MOVEMENT_CONTROLLER)) {
			player.teleport(PlayerConstants.HOME);
		}/*
		 * else if (c.equals(HG_LOBBY_CONTROLLER)) { player.teleport(new
		 * Location(3027, 3217));
		 * HungerGamesLobby.removeFromWaitingRoom(player); } else if
		 * (c.equals(HG_PRE_CONTROLLER) || c.equals(HG_CONTROLLER)) {
		 * player.teleport(new Location(3027, 3217));
		 * HungerGamesLobby.getCurrent().remove(player); }
		 */
	}
}
