package org.endeavor.game.content.skill.smithing;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendItemOnInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

public class SmithingConstants {
	private static Item[][][] SMITHING_ITEMS = {
			{ { new Item(1205), new Item(1277), new Item(1321), new Item(1291), new Item(1307) },
					{ new Item(1351), new Item(1422), new Item(1337), new Item(1375), new Item(3095) },
					{ new Item(1103), new Item(1075), new Item(1087), new Item(1117) },
					{ new Item(1139), new Item(1155), new Item(1173), new Item(1189), new Item(4819, 15) },
					{ new Item(819, 15), new Item(39, 15), new Item(864, 15) } },
			{ { new Item(1203), new Item(1279), new Item(1323), new Item(1293), new Item(1309) },
					{ new Item(1349), new Item(1420), new Item(1335), new Item(1363), new Item(3096) },
					{ new Item(1101), new Item(1067), new Item(1081), new Item(1115) },
					{ new Item(1137), new Item(1153), new Item(1175), new Item(1191), new Item(4820, 15) },
					{ new Item(820, 15), new Item(40, 15), new Item(863, 15) } },
			{ { new Item(1207), new Item(1281), new Item(1325), new Item(1295), new Item(1311) },
					{ new Item(1353), new Item(1424), new Item(1339), new Item(1365), new Item(3097) },
					{ new Item(1105), new Item(1069), new Item(1083), new Item(1119) },
					{ new Item(1141), new Item(1157), new Item(1177), new Item(1193), new Item(1539, 15) },
					{ new Item(821, 15), new Item(41, 15), new Item(865, 15) } },
			{ { new Item(1209), new Item(1285), new Item(1329), new Item(1299), new Item(1315) },
					{ new Item(1355), new Item(1428), new Item(1343), new Item(1369), new Item(3099) },
					{ new Item(1109), new Item(1071), new Item(1085), new Item(1121) },
					{ new Item(1143), new Item(1159), new Item(1181), new Item(1197), new Item(4822, 15) },
					{ new Item(822, 15), new Item(42, 15), new Item(866, 15) } },
			{ { new Item(1211), new Item(1287), new Item(1331), new Item(1301), new Item(1317) },
					{ new Item(1357), new Item(1430), new Item(1345), new Item(1371), new Item(3100) },
					{ new Item(1111), new Item(1073), new Item(1091), new Item(1123) },
					{ new Item(1145), new Item(1161), new Item(1183), new Item(1199), new Item(4823, 15) },
					{ new Item(823, 15), new Item(43, 15), new Item(867, 15) } },
			{ { new Item(1213), new Item(1289), new Item(1333), new Item(1303), new Item(1319) },
					{ new Item(1359), new Item(1432), new Item(1347), new Item(1373), new Item(3101) },
					{ new Item(1113), new Item(1079), new Item(1093), new Item(1127) },
					{ new Item(1147), new Item(1163), new Item(1185), new Item(1201), new Item(4824, 15) },
					{ new Item(824, 15), new Item(44, 15), new Item(868, 15) } } };

	public static final int[] BARS = { 2349, 2351, 2353, 2359, 2361, 2363 };

	public static boolean clickAnvil(Player p, int id) {
		if (id == 4306 || id == 2783) {
			for (Item i : p.getInventory().getItems()) {
				if (i != null) {
					for (int k = 0; k < BARS.length; k++) {
						if (i.getId() == BARS[k]) {
							sendSmithingInterface(p, k);
							return true;
						}
					}
				}
			}

			p.getClient().queueOutgoingPacket(new SendMessage("You do not have any bars to smith with."));

			return true;
		}

		return false;
	}

	public static boolean useBarOnAnvil(Player p, int object, int item) {
		if (object == 4306 || object == 2783) {
			for (int k = 0; k < BARS.length; k++) {
				if (item == BARS[k]) {
					sendSmithingInterface(p, k);
					return true;
				}
			}

			return true;
		}

		return false;
	}

	public static int getBarAmount(int interfaceId, int slot) {
		switch (interfaceId) {
		case 1119:
			switch (slot) {
			case 0:
				return 1;
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 2;
			case 4:
				return 3;
			}
			break;
		case 1120:
			switch (slot) {
			case 0:
				return 1;
			case 1:
				return 1;
			case 2:
				return 3;
			case 3:
				return 3;
			case 4:
				return 2;
			}
			break;
		case 1121:
			switch (slot) {
			case 0:
				return 3;
			case 1:
				return 3;
			case 2:
				return 3;
			case 3:
				return 5;
			case 4:
				return 1;
			}
			break;
		case 1122:
			switch (slot) {
			case 0:
				return 1;
			case 1:
				return 2;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 1;
			}
			break;
		case 1123:
			switch (slot) {
			case 0:
				return 1;
			case 1:
				return 1;
			case 2:
				return 1;
			case 3:
				return 1;
			case 4:
				return 1;
			}
			break;
		}

		return 1;
	}

	public static void sendSmeltSelectionInterface(Player p) {
		p.getClient().queueOutgoingPacket(
				new SendUpdateItems(2404, new Item[] { new Item(2349), new Item(2351), new Item(2353), new Item(2355),
						new Item(2357), new Item(2359), new Item(2361), new Item(2363) }));

		for (int i = 0; i < 9; i++) {
			p.getClient().queueOutgoingPacket(new SendItemOnInterface(2405 + i, 65535, 9999));
		}

		p.getClient().queueOutgoingPacket(new SendChatInterface(2400));
	}

	public static final int[][] TEXT_BAR_IDS = { { 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 13357 },// 1
			{ 1089, 1112, 1113, 1114, 1116, 8428 },// 2
			{ 1090, 1095, 1109, 1110, 1111, 1115, 1118 },// 3
			{},// 4
			{ 1112 },// 5
	};

	public static void sendSmithingInterface(Player p, int id) {
		if ((id < 0) || (id > 5)) {
			return;
		}

		for (int i = 0; i < 5; i++) {
			p.getClient().queueOutgoingPacket(new SendUpdateItems(1119 + i, SMITHING_ITEMS[id][i]));
		}

		final int amount = p.getInventory().getItemAmount(BARS[id]);

		for (int i = 0; i < TEXT_BAR_IDS.length; i++) {
			if (amount >= i + 1) {
				for (int k = 0; k < TEXT_BAR_IDS[i].length; k++) {
					p.getClient().queueOutgoingPacket(
							new SendString("@gre@" + (i + 1) + (i == 0 ? "bar" : "bars"), TEXT_BAR_IDS[i][k]));
				}
			} else {
				for (int k = 0; k < TEXT_BAR_IDS[i].length; k++) {
					p.getClient().queueOutgoingPacket(
							new SendString("@red@" + (i + 1) + (i == 0 ? "bar" : "bars"), TEXT_BAR_IDS[i][k]));
				}
			}
		}

		p.getClient().queueOutgoingPacket(new SendString("", 11461));
		p.getClient().queueOutgoingPacket(new SendString("", 11459));
		p.getClient().queueOutgoingPacket(new SendString("", 1135));
		p.getClient().queueOutgoingPacket(new SendString("", 1134));
		p.getClient().queueOutgoingPacket(new SendString("", 1096));
		p.getClient().queueOutgoingPacket(new SendString("", 1132));

		p.getClient().queueOutgoingPacket(new SendInterface(994));
	}

	public static boolean clickSmeltSelection(Player p, int id) {
		switch (id) {
		case 15147:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.BRONZE_BAR));
			return true;
		case 15146:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.BRONZE_BAR));
			return true;
		case 10247:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.BRONZE_BAR));
			return true;
		case 9110:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.BRONZE_BAR));
			return true;
		case 15151:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.IRON_BAR));
			return true;
		case 15150:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.IRON_BAR));
			return true;
		case 15149:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.IRON_BAR));
			return true;
		case 15148:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.IRON_BAR));
			return true;
		case 15155:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.SILVER_BAR));
			return true;
		case 15154:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.SILVER_BAR));
			return true;
		case 15153:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.SILVER_BAR));
			return true;
		case 15152:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.SILVER_BAR));
			return true;
		case 15159:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.STEEL_BAR));
			return true;
		case 15158:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.STEEL_BAR));
			return true;
		case 15157:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.STEEL_BAR));
			return true;
		case 15156:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.STEEL_BAR));
			return true;
		case 15163:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.GOLD_BAR));
			return true;
		case 15162:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.GOLD_BAR));
			return true;
		case 15161:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.GOLD_BAR));
			return true;
		case 15160:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.GOLD_BAR));
			return true;
		case 29017:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.MITHRIL_BAR));
			return true;
		case 29016:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.MITHRIL_BAR));
			return true;
		case 24253:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.MITHRIL_BAR));
			return true;
		case 16062:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.MITHRIL_BAR));
			return true;
		case 29022:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.ADAMANITE_BAR));
			return true;
		case 29020:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.ADAMANITE_BAR));
			return true;
		case 29019:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.ADAMANITE_BAR));
			return true;
		case 29018:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.ADAMANITE_BAR));
			return true;
		case 29026:
			TaskQueue.queue(new Smelting(p, 1, SmeltingData.RUNITE_BAR));
			return true;
		case 29025:
			TaskQueue.queue(new Smelting(p, 5, SmeltingData.RUNITE_BAR));
			return true;
		case 29024:
			TaskQueue.queue(new Smelting(p, 10, SmeltingData.RUNITE_BAR));
			return true;
		case 29023:
			TaskQueue.queue(new Smelting(p, 28, SmeltingData.RUNITE_BAR));
			return true;
		}
		return false;
	}

	public static int getLevel(int item) {
		if ((item == 1205) || (item == 1351)) {
			return 1;
		}
		if (item == 1422) {
			return 2;
		}
		if (item == 1139) {
			return 3;
		}
		if ((item == 1277) || (item == 819)) {
			return 4;
		}
		if ((item == 1321) || (item == 39)) {
			return 5;
		}
		if (item == 1291) {
			return 6;
		}
		if ((item == 1155) || (item == 864)) {
			return 7;
		}
		if (item == 1173) {
			return 8;
		}
		if (item == 1337) {
			return 9;
		}
		if (item == 1375) {
			return 10;
		}
		if (item == 1103) {
			return 11;
		}
		if (item == 1189) {
			return 12;
		}
		if (item == 3095) {
			return 13;
		}
		if (item == 1307) {
			return 14;
		}
		if (item == 1203) {
			return 15;
		}
		if ((item == 1087) || (item == 1075) || (item == 1349)) {
			return 16;
		}
		if (item == 1420) {
			return 17;
		}
		if ((item == 1117) || (item == 1137)) {
			return 18;
		}
		if ((item == 1279) || (item == 820) || (item == 4820)) {
			return 19;
		}
		if ((item == 1323) || (item == 40)) {
			return 20;
		}
		if (item == 1293) {
			return 21;
		}
		if ((item == 1153) || (item == 863)) {
			return 22;
		}
		if (item == 1175) {
			return 23;
		}
		if (item == 1335) {
			return 24;
		}
		if (item == 1363) {
			return 25;
		}
		if ((item == 1101) || (item == 4540)) {
			return 26;
		}
		if (item == 1191) {
			return 27;
		}
		if (item == 3096) {
			return 28;
		}
		if (item == 1309) {
			return 29;
		}
		if (item == 1207) {
			return 30;
		}
		if ((item == 1067) || (item == 1081) || (item == 1353)) {
			return 31;
		}
		if (item == 1424) {
			return 32;
		}
		if ((item == 1115) || (item == 1141)) {
			return 33;
		}
		if ((item == 1281) || (item == 1539) || (item == 821)) {
			return 34;
		}
		if ((item == 1325) || (item == 41)) {
			return 35;
		}
		if ((item == 1295) || (item == 2370)) {
			return 36;
		}
		if ((item == 1157) || (item == 865)) {
			return 37;
		}
		if (item == 1177) {
			return 38;
		}
		if (item == 1339) {
			return 39;
		}
		if (item == 1365) {
			return 40;
		}
		if (item == 1105) {
			return 41;
		}
		if (item == 1193) {
			return 42;
		}
		if (item == 3097) {
			return 43;
		}
		if (item == 1311) {
			return 44;
		}
		if ((item == 1069) || (item == 1083)) {
			return 46;
		}
		if (item == 1119) {
			return 48;
		}
		if (item == 4544) {
			return 49;
		}
		if (item == 1209) {
			return 50;
		}
		if (item == 1355) {
			return 51;
		}
		if (item == 1428) {
			return 52;
		}
		if (item == 1143) {
			return 53;
		}
		if ((item == 1285) || (item == 822) || (item == 4822)) {
			return 54;
		}
		if ((item == 1329) || (item == 42)) {
			return 55;
		}
		if (item == 1299) {
			return 56;
		}
		if ((item == 1159) || (item == 866)) {
			return 57;
		}
		if (item == 1181) {
			return 58;
		}
		if (item == 1343) {
			return 59;
		}
		if (item == 1369) {
			return 60;
		}
		if (item == 1109) {
			return 61;
		}
		if (item == 1197) {
			return 62;
		}
		if (item == 3099) {
			return 63;
		}
		if (item == 1315) {
			return 64;
		}
		if ((item == 1071) || (item == 1085)) {
			return 66;
		}
		if (item == 1121) {
			return 68;
		}
		if (item == 1211) {
			return 70;
		}
		if (item == 1430) {
			return 72;
		}
		if (item == 1145) {
			return 73;
		}
		if ((item == 1287) || (item == 823) || (item == 4823)) {
			return 74;
		}
		if ((item == 1331) || (item == 43)) {
			return 75;
		}
		if (item == 1301) {
			return 76;
		}
		if ((item == 1161) || (item == 867)) {
			return 77;
		}
		if (item == 1183) {
			return 78;
		}
		if (item == 1371) {
			return 79;
		}
		if (item == 1111) {
			return 81;
		}
		if (item == 1199) {
			return 82;
		}
		if (item == 3100) {
			return 83;
		}
		if (item == 1317) {
			return 84;
		}
		if (item == 1213) {
			return 85;
		}
		if ((item == 1073) || (item == 1091) || (item == 1359)) {
			return 86;
		}
		if (item == 1432) {
			return 87;
		}
		if ((item == 1123) || (item == 1147)) {
			return 88;
		}
		if ((item == 1289) || (item == 824) || (item == 4824)) {
			return 89;
		}
		if ((item == 1333) || (item == 44)) {
			return 90;
		}
		if (item == 1303) {
			return 91;
		}
		if ((item == 1163) || (item == 868)) {
			return 92;
		}
		if (item == 1185) {
			return 93;
		}
		if (item == 1347) {
			return 94;
		}
		if (item == 1373) {
			return 95;
		}
		if (item == 1113) {
			return 96;
		}
		if (item == 1201) {
			return 97;
		}
		if (item == 3101) {
			return 98;
		}
		if (item == 1319) {
			return 99;
		}
		if (item == 1079) {
			return 99;
		}
		if ((item == 1079) || (item == 1093) || (item == 1319) || (item == 1127)) {
			return 99;
		}

		return 1;
	}
}
