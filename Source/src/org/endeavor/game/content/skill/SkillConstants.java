package org.endeavor.game.content.skill;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class SkillConstants {
	public static final int LEVEL_99_SONG = 99;
	public static final int[] LEVEL_UP_SOUNDS = { 112, 107, 104, 10, 21, 24 };
	public static final int SKILL_COUNT = 25;
	public static final Graphic LEVELUP_GRAPHIC = Graphic.highGraphic(199, 0);
	public static final int[] EXPERIENCE_RATES = new int[25];
	public static final int MAX_EXPERIENCE = 200000000;
	public static final String[] SKILL_NAMES = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Summoning", "Hunter",
			"Construction", "Dungeoneering" };
	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 8;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	public static final int SUMMONING = 21;
	public static final int HUNTER = 22;
	public static final int CONSTRUCTION = 23;
	public static final int DUNGEONEERING = 24;
	public static int[][] CHAT_INTERFACES = { { 0, 6247 }, { 1, 6253 }, { 2, 6206 }, { 3, 6216 },
			{ 4, 4443, 5453, 6114 }, { 5, 6242 }, { 6, 6211 }, { 7, 6226 }, { 8, 4272 }, { 9, 6231 }, { 10, 6258 },
			{ 11, 4282 }, { 12, 6263 }, { 13, 6221 }, { 14, 4416, 4417, 4438 }, { 15, 6237 }, { 16, 4277 },
			{ 17, 4261, 4263, 4264 }, { 18, 12122 }, { 19, 4887, 4889, 4890 }, { 20, 4267 } };

	public static final int[][] REFRESH_DATA = { { 0, 4004, 4005, 4044, 4045, 18792, 18790 },
			{ 1, 4008, 4009, 4056, 4057, 18817, 18815 }, { 2, 4006, 4007, 4050, 4051, 18798, 18796 },
			{ 3, 4016, 4017, 18853, 18854, 18859, 18857 }, { 4, 4010, 4011, 4062, 4063, 18822, 18820 },
			{ 5, 4012, 4013, 4068, 4069, 18827, 18825 }, { 6, 4014, 4015, 18832, 18833, 18838, 18836 },
			{ 7, 4034, 4035, 19042, 19043, 19048, 19046 }, { 8, 4038, 4039, 19084, 19085, 19090, 19088 },
			{ 9, 4026, 4027, 18958, 18959, 18964, 18962 }, { 10, 4032, 4033, 19021, 19022, 19027, 19025 },
			{ 11, 4036, 4037, 19063, 19064, 19069, 19067 }, { 12, 4024, 4025, 18937, 18938, 18943, 18941 },
			{ 13, 4030, 4031, 19422, 19423, 19428, 19426 }, { 14, 4028, 4029, 18979, 18980, 18985, 18983 },
			{ 15, 4020, 4021, 18895, 18896, 18901, 18899 }, { 16, 4018, 4019, 18874, 18875, 18880, 18878 },
			{ 17, 4022, 4023, 18916, 18917, 18922, 18920 }, { 18, 18809, 18810, 19126, 19127, 19132, 19130 },
			{ 19, 18811, 18812, 19275, 19276, 19281, 19279 }, { 20, 18807, 18808, 19105, 19106, 19111, 19109 },
			{ 21, 18165, 18169, 19232, 19233, 19238, 19236 }, { 22, 18166, 18170, 19211, 19212, 19217, 19215 },
			{ 23, 18167, 18171, 19190, 19191, 19196, 19194 }, { 24, 18168, 18172, 19253, 19254, 19259, 19257 } };
	public static final int CURRENT_HEALTH_UPDATE_ID = 19001;//4016
	public static final int MAX_HEALTH_UPDATE_ID = 4017;
	public static final int CURRENT_PRAYER_UPDATE_ID = 4012;
	public static final int MAX_PRAYER_UPDATE_ID = 4013;

	public static void declare() {
		EXPERIENCE_RATES[ATTACK] = 150;
		EXPERIENCE_RATES[DEFENCE] = 150;
		EXPERIENCE_RATES[STRENGTH] = 150;
		EXPERIENCE_RATES[HITPOINTS] = 150;
		EXPERIENCE_RATES[RANGED] = 150;
		EXPERIENCE_RATES[PRAYER] = 85;
		EXPERIENCE_RATES[MAGIC] = 150;
		EXPERIENCE_RATES[COOKING] = 17;
		EXPERIENCE_RATES[WOODCUTTING] = 20;
		EXPERIENCE_RATES[FLETCHING] = 15;
		EXPERIENCE_RATES[FISHING] = 16;
		EXPERIENCE_RATES[FIREMAKING] = 20;
		EXPERIENCE_RATES[CRAFTING] = 17;
		EXPERIENCE_RATES[SMITHING] = 25;
		EXPERIENCE_RATES[MINING] = 25;
		EXPERIENCE_RATES[HERBLORE] = 17;
		EXPERIENCE_RATES[AGILITY] = 50;
		EXPERIENCE_RATES[THIEVING] = 15;
		EXPERIENCE_RATES[SLAYER] = 20;
		EXPERIENCE_RATES[FARMING] = 20;
		EXPERIENCE_RATES[RUNECRAFTING] = 17;
		EXPERIENCE_RATES[DUNGEONEERING] = 25;
		EXPERIENCE_RATES[SUMMONING] = 27;
	}

	public static final boolean isSuccess(Player p, int skillId, int levelRequired) {
		double level = p.getMaxLevels()[skillId];
		double req = levelRequired;
		double successChance = Math.ceil((level * 50.0D - req * 15.0D) / req / 3.0D * 4.0D);
		int roll = Misc.randomNumber(99);

		if (successChance >= roll) {
			return true;
		}

		return false;
	}

	public static final boolean isSuccess(int skill, int levelRequired) {
		double level = skill;
		double req = levelRequired;
		double successChance = Math.ceil((level * 50.0D - req * 15.0D) / req / 3.0D * 4.0D);
		int roll = Misc.randomNumber(99);

		if (successChance >= roll) {
			return true;
		}

		return false;
	}

	public static final boolean isSuccess(Player p, int skillId, int levelRequired, int toolLevelRequired) {
		double level = (p.getMaxLevels()[skillId] + toolLevelRequired) / 2.0D;
		double req = levelRequired;
		double successChance = Math.ceil((level * 50.0D - req * 15.0D) / req / 3.0D * 4.0D);
		int roll = Misc.randomNumber(99);

		if (successChance >= roll) {
			return true;
		}

		return false;
	}
}
