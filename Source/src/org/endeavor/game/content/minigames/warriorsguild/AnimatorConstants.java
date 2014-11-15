package org.endeavor.game.content.minigames.warriorsguild;

public class AnimatorConstants {
	protected static final int ANIMATOR_ID = 15621;
	public static final int WARRIORS_GUILD_TOKEN = 8851;
	protected static final int[][] ANIMATOR_LOCATIONS = { { 2851, 3536 }, { 2857, 3536 } };

	protected static final int[][] ARMOR_SETS = { { 1155, 1117, 1075 }, { 1153, 1115, 1067 }, { 1157, 1119, 1069 },
			{ 1165, 1125, 1077 }, { 1159, 1121, 1071 }, { 1161, 1123, 1073 }, { 1163, 1127, 1079 } };

	public static final int[] TOKENS = { 5, 10, 15, 40, 50, 60, 80 };

	protected static final int[] ANIMATED_ARMOR = { 4278, 4279, 4280, 4281, 4282, 4283, 4284 };

	protected static final String[] ARMOR_TYPE = { "Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Rune" };

	public static final int[] DEFENDERS = { 8844, 8845, 8846, 8847, 8848, 8849, 8850, 18653 };

	public static boolean isAnimatedArmour(int id) {
		for (int i : ANIMATED_ARMOR) {
			if (id == i)
				return true;
		}
		return false;
	}
}
