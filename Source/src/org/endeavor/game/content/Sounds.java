package org.endeavor.game.content;

import org.endeavor.game.entity.player.Player;

public class Sounds {
	public static final int LEVELUP = 67;
	public static final int DUELWON = 77;
	public static final int DUELLOST = 76;
	public static final int FOODEAT = 317;
	public static final int DROPITEM = 376;
	public static final int COOKITEM = 357;
	public static final int FLETCHING = 375;
	public static final int LIGHTINGLOG = 811;
	public static final int SHOOT_ARROW = 370;
	public static final int TELEPORT = 202;
	public static final int BONE_BURY = 380;
	public static final int DRINK_POTION = 334;

	public static int getPlayerBlockSounds(Player player) {
		int blockSound = 511;

		if ((player.getEquipment().getItems()[4].getId() == 2499)
				|| (player.getEquipment().getItems()[4].getId() == 2501)
				|| (player.getEquipment().getItems()[4].getId() == 2503)
				|| (player.getEquipment().getItems()[4].getId() == 4746)
				|| (player.getEquipment().getItems()[4].getId() == 4757)
				|| (player.getEquipment().getItems()[4].getId() == 10330)) {
			blockSound = 24;
		} else if ((player.getEquipment().getItems()[4].getId() == 10551)
				|| (player.getEquipment().getItems()[4].getId() == 10438)) {
			blockSound = 32;
		} else if ((player.getEquipment().getItems()[4].getId() == 10338)
				|| (player.getEquipment().getItems()[4].getId() == 7399)
				|| (player.getEquipment().getItems()[4].getId() == 6107)
				|| (player.getEquipment().getItems()[4].getId() == 4091)
				|| (player.getEquipment().getItems()[4].getId() == 4101)
				|| (player.getEquipment().getItems()[4].getId() == 4111)
				|| (player.getEquipment().getItems()[4].getId() == 1035)
				|| (player.getEquipment().getItems()[4].getId() == 12971))
			blockSound = 14;
		else if (player.getEquipment().getItems()[5].getId() == 4224) {
			blockSound = 30;
		} else if ((player.getEquipment().getItems()[4].getId() == 1101)
				|| (player.getEquipment().getItems()[4].getId() == 1103)
				|| (player.getEquipment().getItems()[4].getId() == 1105)
				|| (player.getEquipment().getItems()[4].getId() == 1107)
				|| (player.getEquipment().getItems()[4].getId() == 1109)
				|| (player.getEquipment().getItems()[4].getId() == 1111)
				|| (player.getEquipment().getItems()[4].getId() == 1113)
				|| (player.getEquipment().getItems()[4].getId() == 1115)
				|| (player.getEquipment().getItems()[4].getId() == 1117)
				|| (player.getEquipment().getItems()[4].getId() == 1119)
				|| (player.getEquipment().getItems()[4].getId() == 1121)
				|| (player.getEquipment().getItems()[4].getId() == 1123)
				|| (player.getEquipment().getItems()[4].getId() == 1125)
				|| (player.getEquipment().getItems()[4].getId() == 1127)
				|| (player.getEquipment().getItems()[4].getId() == 4720)
				|| (player.getEquipment().getItems()[4].getId() == 4728)
				|| (player.getEquipment().getItems()[4].getId() == 4749)
				|| (player.getEquipment().getItems()[4].getId() == 4712)
				|| (player.getEquipment().getItems()[4].getId() == 11720)
				|| (player.getEquipment().getItems()[4].getId() == 11724)
				|| (player.getEquipment().getItems()[4].getId() == 3140)
				|| (player.getEquipment().getItems()[4].getId() == 2615)
				|| (player.getEquipment().getItems()[4].getId() == 2653)
				|| (player.getEquipment().getItems()[4].getId() == 2661)
				|| (player.getEquipment().getItems()[4].getId() == 2669)
				|| (player.getEquipment().getItems()[4].getId() == 2623)
				|| (player.getEquipment().getItems()[4].getId() == 3841)
				|| (player.getEquipment().getItems()[4].getId() == 1127)) {
			blockSound = 15;
		} else
			blockSound = 511;

		return blockSound;
	}

	public static int GetWeaponSound(Player player) {
		String wep = "blank";

		if (player.getEquipment().getItems()[3].getId() == 4718) {
			return 1320;
		}
		if (player.getEquipment().getItems()[3].getId() == 4734) {
			return 1081;
		}
		if (player.getEquipment().getItems()[3].getId() == 4747) {
			return 1330;
		}
		if (player.getEquipment().getItems()[3].getId() == 4710) {
			return 2555;
		}
		if (player.getEquipment().getItems()[3].getId() == 4755) {
			return 1323;
		}
		if (player.getEquipment().getItems()[3].getId() == 4726) {
			return 2562;
		}

		if ((player.getEquipment().getItems()[3].getId() == 772)
				|| (player.getEquipment().getItems()[3].getId() == 1379)
				|| (player.getEquipment().getItems()[3].getId() == 1381)
				|| (player.getEquipment().getItems()[3].getId() == 1383)
				|| (player.getEquipment().getItems()[3].getId() == 1385)
				|| (player.getEquipment().getItems()[3].getId() == 1387)
				|| (player.getEquipment().getItems()[3].getId() == 1389)
				|| (player.getEquipment().getItems()[3].getId() == 1391)
				|| (player.getEquipment().getItems()[3].getId() == 1393)
				|| (player.getEquipment().getItems()[3].getId() == 1395)
				|| (player.getEquipment().getItems()[3].getId() == 1397)
				|| (player.getEquipment().getItems()[3].getId() == 1399)
				|| (player.getEquipment().getItems()[3].getId() == 1401)
				|| (player.getEquipment().getItems()[3].getId() == 1403)
				|| (player.getEquipment().getItems()[3].getId() == 1405)
				|| (player.getEquipment().getItems()[3].getId() == 1407)
				|| (player.getEquipment().getItems()[3].getId() == 1409)
				|| (player.getEquipment().getItems()[3].getId() == 9100)) {
			return 394;
		}
		if ((player.getEquipment().getItems()[3].getId() == 839)
				|| (player.getEquipment().getItems()[3].getId() == 841)
				|| (player.getEquipment().getItems()[3].getId() == 843)
				|| (player.getEquipment().getItems()[3].getId() == 845)
				|| (player.getEquipment().getItems()[3].getId() == 847)
				|| (player.getEquipment().getItems()[3].getId() == 849)
				|| (player.getEquipment().getItems()[3].getId() == 851)
				|| (player.getEquipment().getItems()[3].getId() == 853)
				|| (player.getEquipment().getItems()[3].getId() == 855)
				|| (player.getEquipment().getItems()[3].getId() == 857)
				|| (player.getEquipment().getItems()[3].getId() == 859)
				|| (player.getEquipment().getItems()[3].getId() == 861)
				|| (player.getEquipment().getItems()[3].getId() == 4734)
				|| (player.getEquipment().getItems()[3].getId() == 2023)
				|| (player.getEquipment().getItems()[3].getId() == 4212)
				|| (player.getEquipment().getItems()[3].getId() == 4214)
				|| (player.getEquipment().getItems()[3].getId() == 4934)
				|| (player.getEquipment().getItems()[3].getId() == 9104)
				|| (player.getEquipment().getItems()[3].getId() == 9107)) {
			return 370;
		}
		if ((player.getEquipment().getItems()[3].getId() == 1363)
				|| (player.getEquipment().getItems()[3].getId() == 1365)
				|| (player.getEquipment().getItems()[3].getId() == 1367)
				|| (player.getEquipment().getItems()[3].getId() == 1369)
				|| (player.getEquipment().getItems()[3].getId() == 1371)
				|| (player.getEquipment().getItems()[3].getId() == 1373)
				|| (player.getEquipment().getItems()[3].getId() == 1375)
				|| (player.getEquipment().getItems()[3].getId() == 1377)
				|| (player.getEquipment().getItems()[3].getId() == 1349)
				|| (player.getEquipment().getItems()[3].getId() == 1351)
				|| (player.getEquipment().getItems()[3].getId() == 1353)
				|| (player.getEquipment().getItems()[3].getId() == 1355)
				|| (player.getEquipment().getItems()[3].getId() == 1357)
				|| (player.getEquipment().getItems()[3].getId() == 1359)
				|| (player.getEquipment().getItems()[3].getId() == 1361)
				|| (player.getEquipment().getItems()[3].getId() == 9109)) {
			return 399;
		}
		if ((player.getEquipment().getItems()[3].getId() == 4718)
				|| (player.getEquipment().getItems()[3].getId() == 7808)) {
			return 400;
		}
		if ((player.getEquipment().getItems()[3].getId() == 6609)
				|| (player.getEquipment().getItems()[3].getId() == 1307)
				|| (player.getEquipment().getItems()[3].getId() == 1309)
				|| (player.getEquipment().getItems()[3].getId() == 1311)
				|| (player.getEquipment().getItems()[3].getId() == 1313)
				|| (player.getEquipment().getItems()[3].getId() == 1315)
				|| (player.getEquipment().getItems()[3].getId() == 1317)
				|| (player.getEquipment().getItems()[3].getId() == 1319)) {
			return 425;
		}
		if ((player.getEquipment().getItems()[3].getId() == 1321)
				|| (player.getEquipment().getItems()[3].getId() == 1323)
				|| (player.getEquipment().getItems()[3].getId() == 1325)
				|| (player.getEquipment().getItems()[3].getId() == 1327)
				|| (player.getEquipment().getItems()[3].getId() == 1329)
				|| (player.getEquipment().getItems()[3].getId() == 1331)
				|| (player.getEquipment().getItems()[3].getId() == 1333)
				|| (player.getEquipment().getItems()[3].getId() == 4587)) {
			return 396;
		}
		if (wep.contains("halberd")) {
			return 420;
		}
		if (wep.contains("long")) {
			return 396;
		}
		if (wep.contains("knife")) {
			return 368;
		}
		if (wep.contains("javelin")) {
			return 364;
		}

		if (player.getEquipment().getItems()[3].getId() == 9110) {
			return 401;
		}
		if (player.getEquipment().getItems()[3].getId() == 4755) {
			return 1059;
		}
		if (player.getEquipment().getItems()[3].getId() == 4153) {
			return 1079;
		}
		if (player.getEquipment().getItems()[3].getId() == 9103) {
			return 385;
		}
		if (player.getEquipment().getItems()[3].getId() == -1) {
			return 417;
		}
		if ((player.getEquipment().getItems()[3].getId() == 2745)
				|| (player.getEquipment().getItems()[3].getId() == 2746)
				|| (player.getEquipment().getItems()[3].getId() == 2747)
				|| (player.getEquipment().getItems()[3].getId() == 2748)) {
			return 390;
		}
		if (player.getEquipment().getItems()[3].getId() == 4151) {
			return 1080;
		}
		return 401;
	}

	public static int specialSounds(int id) {
		if (id == 4151) {
			return 1081;
		}
		if (id == 5698) {
			return 793;
		}
		if (id == 1434) {
			return 387;
		}
		if (id == 3204) {
			return 420;
		}
		if (id == 4153) {
			return 1082;
		}
		if (id == 7158) {
			return 426;
		}
		if (id == 4587) {
			return 1305;
		}
		if (id == 1215) {
			return 793;
		}
		if (id == 1305) {
			return 390;
		}
		if (id == 861) {
			return 386;
		}
		if (id == 11235) {
			return 386;
		}

		if (id == 1377) {
			return 389;
		}
		return -1;
	}
}
