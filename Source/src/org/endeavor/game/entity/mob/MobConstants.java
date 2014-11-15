package org.endeavor.game.entity.mob;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.skill.fishing.Fishing;
import org.endeavor.game.entity.player.Player;

/**
 * Holds constant variables for mobs
 * @author Michael Sasse
 *
 */
public final class MobConstants {
	
	/**
	 * The random chance a mob will walk
	 */
	public static final byte RANDOM_WALK_CHANCE = 12;
	
	/**
	 * The maximum random walking distance from their location
	 */
	public static final byte MAX_RANDOM_WALK_DISTANCE = 2;
	
	/**
	 * The default respawn rate for npcs
	 */
	public static final byte DEFAULT_RESPAWN_TIME = 50;
	
	/**
	 * An array containing aggressive npcs
	 */
	private static final byte[] aggressive = new byte[18000];

	/**
	 * A list containing non aggressive npcs
	 */
	private static String[] NON_AGGRESSIVE_NPCS = { "man", "woman", "gnome", "dwarf", "cow", "guard" };

	/**
	 * A list of mobs that don't follow
	 */
	private static final int[] NO_FOLLOW_MOBS = { 1457, 3943, 3847 };

	/**
	 * A list of flying mobs
	 */
	public static final int[] FLYING_MOBS = { 6230, 6229, 6231 };

	/**
	 * Gets if the mob shouldn't follow or not
	 * @param mob
	 * @return
	 */
	public static final boolean noFollow(Mob mob) {
		for (int i : NO_FOLLOW_MOBS) {
			if (mob.getId() == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets if the npc is a dagannoth king
	 * @param m
	 * The mob to check
	 * @return
	 */
	public static boolean isDagannothKing(Mob m) {
		int id = m.getId();
		return (id == 2881) || (id == 2882) || (id == 2883);
	}

	public static final boolean face(int id) {
		return (id != 3636) && (Fishing.FishingSpots.forId(id) == null);
	}

	/**
	 * gets if the mob is a dragon or not
	 * @param mob
	 * @return
	 */
	public static boolean isDragon(Mob mob) {
		if (mob == null) {
			return false;
		}

		int id = mob.getId();

		return (id == 51) || (id == 53) || (id == 54) || (id == 55) || (id == 941) || (id == 1590) || (id == 1591)
				|| (id == 1592) || (id == 5363) || (id == 50) || (id == 10775) || id == 50;
	}

	public static final void declare() {
		int count = 0;

		for (int i = 0; i < 10000; i++) {
			NpcDefinition def = GameDefinitionLoader.getNpcDefinition(i);

			if ((def != null) && (def.getName() != null)) {
				String name = def.getName().toLowerCase();

				for (String k : NON_AGGRESSIVE_NPCS) {
					if (name.contains(k.toLowerCase())) {
						aggressive[i] = 1;
						count++;
						break;
					}
				}
			}
		}
		System.out.println("Loaded " + count + " non-aggressive mobs.");
	}

	public static final boolean isAggressive(int id) {
		return aggressive[id] != 1;
	}

	public static boolean isAgressiveFor(Mob mob, Player player) {
		return (mob.getDungGame() != null)
				|| ((player.getController().canAttackNPC()) && (player.getSkill().getCombatLevel() <= mob
						.getDefinition().getLevel() * 2 + 1));
	}

	public static enum MobDissapearDelay {
		BARRELCHEST(5666, (byte) 15), WAR_MONGER(10141, (byte) 8), ROCK_CRAB(12826, (byte) 2), DESERT_STR_WORM(9465,
				(byte) 7), DUNG_BOSS_A(9733, (byte) 7);

		private final int id;
		private final byte delay;
		public static final Map<Integer, Byte> data = new HashMap<Integer, Byte>();

		private MobDissapearDelay(int id, byte delay) {
			this.id = id;
			this.delay = delay;
		}

		public static final void declare() {
			for (MobDissapearDelay i : values())
				data.put(Integer.valueOf(i.id), Byte.valueOf(i.delay));
		}

		public static final int getDelay(int id) {
			return data.get(Integer.valueOf(id)) != null ? data.get(Integer.valueOf(id)).byteValue() : 5;
		}
	}
}
