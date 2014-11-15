package org.endeavor.game.content.skill.prayer;

public class PrayerConstants {
	public static final int PRAYER_ID = 5;
	public static final String OUT_OF_PRAYER_MESSAGE = "You have run out of prayer points; you must recharge at an altar.";
	public static final int THICK_SKIN = 0;
	public static final int BURST_OF_STRENGTH = 1;
	public static final int CLARITY_OF_THOUGHT = 2;
	public static final int SHARP_EYE = 3;
	public static final int MYSTIC_WILL = 4;
	public static final int ROCK_SKIN = 5;
	public static final int SUPERHUMAN_STRENGTH = 6;
	public static final int IMPROVED_REFLEXES = 7;
	public static final int RAPID_RESTORE = 8;
	public static final int RAPID_HEAL = 9;
	public static final int DEFAULT_PROTECT_ITEM = 10;
	public static final int HAWK_EYE = 11;
	public static final int MYSTIC_LORE = 12;
	public static final int STEEL_SKIN = 13;
	public static final int ULTIMATE_STRENGTH = 14;
	public static final int INCREDIBLE_REFLEXES = 15;
	public static final int PROTECT_FROM_MAGIC = 16;
	public static final int PROTECT_FROM_RANGED = 17;
	public static final int PROTECT_FROM_MELEE = 18;
	public static final int EAGLE_EYE = 19;
	public static final int MYSTIC_MIGHT = 20;
	public static final int RETRIBUTION = 21;
	public static final int REDEMPTION = 22;
	public static final int SMITE = 23;
	public static final int CHIVALRY = 24;
	public static final int PIETY = 25;
	public static final double[] DEFAULT_DRAIN_RATES = { 12.0D, 12.0D, 12.0D, 12.0D, 12.0D, 8.0D, 8.0D, 8.0D, 60.0D,
			60.0D, 30.0D, 6.0D, 6.0D, 6.0D, 6.0D, 6.0D, 4.0D, 4.0D, 4.0D, 6.0D, 6.0D, 4.0D, 3.0D, 4.0D, 3.0D, 3.0D };

	public static final int[] DEFAULT_CONFIG_IDS = { 83, 84, 85, 700, 701, 86, 87, 88, 89, 90, 91, 702, 703, 92, 93,
			94, 95, 96, 97, 704, 705, 98, 99, 100, 706, 707 };

	public static final String[] DEFAULT_NAMES = { "Thick Skin", "Burst of Strength", "Clarity of Thought",
			"Sharp Eye", "Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore",
			"Rapid Heal", "Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength",
			"Incredible Reflexes", "Protect from Magic", "Protect from Range", "Protect from Melee", "Eagle Eye",
			"Mystic Might", "Retribution", "Redemption", "Smite", "Chivalry", "Piety" };

	public static final int[] DEFAULT_REQUIREMENTS = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37,
			40, 43, 44, 45, 46, 49, 52, 60, 70 };

	public static final int[] DEFAULT_PROTECTION_PRAYERS = { 16, 17, 18 };

	public static final int[][] DEFAULT_DISABLED_PRAYERS = new int[DEFAULT_CONFIG_IDS.length][];

	public static final void declare() {
		for (int i = 0; i < DEFAULT_CONFIG_IDS.length; i++)
			DEFAULT_DISABLED_PRAYERS[i] = getDefaultDisabledPrayers(i);
	}

	private static final int[] getDefaultDisabledPrayers(int id) {
		int[] turnOff = new int[0];
		switch (id) {
		case 0:
			turnOff = new int[] { 5, 13, 24, 25 };
			break;
		case 5:
			turnOff = new int[] { 0, 13, 24, 25 };
			break;
		case 13:
			turnOff = new int[] { 0, 5, 24, 25 };
			break;
		case 2:
			turnOff = new int[] { 7, 15, 3, 4, 11, 12, 19, 20, 24, 25 };
			break;
		case 7:
			turnOff = new int[] { 2, 15, 3, 4, 11, 12, 19, 20, 24, 25 };
			break;
		case 15:
			turnOff = new int[] { 7, 2, 3, 4, 11, 12, 19, 20, 24, 25 };
			break;
		case 1:
			turnOff = new int[] { 6, 14, 3, 4, 11, 12, 19, 20, 24, 25 };
			break;
		case 6:
			turnOff = new int[] { 1, 14, 3, 4, 11, 12, 19, 20, 24, 25 };
			break;
		case 14:
			turnOff = new int[] { 6, 1, 3, 4, 11, 12, 19, 20, 24, 25 };
			break;
		case 3:
			turnOff = new int[] { 4, 11, 12, 19, 20, 1, 6, 14, 2, 7, 15, 24, 25 };
			break;
		case 11:
			turnOff = new int[] { 4, 3, 12, 19, 20, 1, 6, 14, 2, 7, 15, 24, 25 };
			break;
		case 19:
			turnOff = new int[] { 4, 11, 12, 3, 20, 1, 6, 14, 2, 7, 15, 24, 25 };
			break;
		case 4:
			turnOff = new int[] { 3, 11, 12, 19, 20, 1, 6, 14, 2, 7, 15, 24, 25 };
			break;
		case 12:
			turnOff = new int[] { 4, 11, 3, 19, 20, 1, 6, 14, 2, 7, 15, 24, 25 };
			break;
		case 20:
			turnOff = new int[] { 4, 11, 12, 19, 3, 1, 6, 14, 2, 7, 15, 24, 25 };
			break;
		case 16:
			turnOff = new int[] { 22, 23, 21, 17, 18 };
			break;
		case 17:
			turnOff = new int[] { 22, 23, 21, 16, 18 };
			break;
		case 18:
			turnOff = new int[] { 22, 23, 21, 17, 16 };
			break;
		case 21:
			turnOff = new int[] { 22, 23, 18, 17, 16 };
			break;
		case 22:
			turnOff = new int[] { 21, 23, 18, 17, 16 };
			break;
		case 23:
			turnOff = new int[] { 22, 21, 18, 17, 16 };
			break;
		case 24:
			turnOff = new int[] { 3, 4, 11, 12, 19, 20, 1, 6, 14, 2, 7, 15, 25, 0, 5, 13 };
			break;
		case 25:
			turnOff = new int[] { 3, 4, 11, 12, 19, 20, 1, 6, 14, 2, 7, 15, 24, 0, 5, 13 };
		case 8:
		case 9:
		case 10:
		}
		return turnOff;
	}
}
