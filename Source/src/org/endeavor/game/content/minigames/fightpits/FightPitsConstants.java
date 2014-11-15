package org.endeavor.game.content.minigames.fightpits;

import org.endeavor.game.entity.Location;

public class FightPitsConstants {
	public static final int RESPAWN_X = 2399;
	public static final int RESPAWN_Y = 5169;
	public static final Location INTO_WAITING_LOCATION = new Location(2399, 5175);
	public static final Location OUT_OF_WAITING_LOCATION = new Location(2399, 5177);
	public static final int GAME_START_TIME = 50;
	public static final int GAME_END_TIME = 500;
	public static final String NOT_ENOUGH_PLAYERS_MESSAGE = "There are not enough players to start Fight Pits!";
	public static final int FIGHT_PITS_INTERFACE_ID = 17600;
	public static final int[] FIGHT_PITS_INTERFACE_STRINGS = { 17601, 17602, 17603, 17604, 17605 };
	public static final String CHAMPION_STRING = "Current Champion: ";
	public static final String GAME_END_TIME_STRING = "Time until game ends: ";
	public static final String NEXT_GAME_STRING = "Time until next game: ";
	public static final int INTO_ARENA_OBJECT = 9368;
	public static final int INTO_WAITING_ROOM_OBJECT = 9369;
	public static final String INTO_ARENA_FAILURE_MESSAGE = "You will be teleported in when the game starts.";
	private static final int TOP_BOUNDARY_X = 2413;
	private static final int TOP_BOUNDARY_Y = 5160;
	public static final int BOTTOM_BOUNDARY_X = 2383;
	public static final int BOTTOM_BOUNDARY_Y = 5133;
	public static final int SPAWN_X_MOD = 30;
	public static final int SPAWN_Y_MOD = 27;
}
