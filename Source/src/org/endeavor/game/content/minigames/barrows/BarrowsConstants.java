package org.endeavor.game.content.minigames.barrows;

import org.endeavor.game.entity.Location;

public class BarrowsConstants {
	public static final String BARROWS_ATTRIBUTE_KEY = "barrowsActive";
	public static final int[][] BARROWS_CRYPT_DIG_BOUNDS = { { 3553, 3295, 3560, 3301 }, { 3572, 3295, 3578, 3301 },
			{ 3562, 3286, 3568, 3292 }, { 3575, 3280, 3580, 3285 }, { 3563, 3273, 3568, 3278 },
			{ 3551, 3280, 3556, 3285 } };

	public static final int[] BARROWS_NPC_IDS = { 2030, 2026, 2025, 2027, 2028, 2029 };

	public static final Location[] BARROWS_CRYPT_DIG_DEST = { new Location(3578, 9706, 3), new Location(3556, 9718, 3),
			new Location(3557, 9703, 3), new Location(3534, 9704, 3), new Location(3546, 9684, 3),
			new Location(3568, 9683, 3) };

	public static final Location[] BARROWS_STAIRCASE_DEST = { new Location(3557, 3298), new Location(3575, 3298),
			new Location(3564, 3289), new Location(3577, 3282), new Location(3565, 3275), new Location(3554, 3283) };

	public static final int[] CRYPT_COFFIN_IDS = { 6823, 6771, 6821, 6773, 6822, 6772 };

	public static final int[] CRYPT_STAIRCASE_IDS = { 6707, 6703, 6702, 6704, 6705, 6706 };

	public static final int getIndexForId(int id) {
		for (int i = 0; i < BARROWS_NPC_IDS.length; i++) {
			if (BARROWS_NPC_IDS[i] == id) {
				return i;
			}
		}

		return -1;
	}
}
