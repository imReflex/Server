package org.endeavor.game.content.skill.fletching;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendItemOnInterface;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class Fletching {
	public static final int KNIFE = 946;
	public static final int BOW_STRING = 1777;
	public static final String FLETCHING_ITEM_KEY = "fletchingitemkey";
	public static final String FLETCHING_TYPE_KEY = "fletchingtypekey";
	public static final int FLETCHING_TYPE_LOG_CUT = 0;
	public static final int FLETCHING_TYPE_STRING_BOW = 1;
	public static final int FLETCHING_TYPE_MAKE_BOLT_TIPS = 2;

	public static boolean useItemOnItem(Player p, int item1, int item2) {
		if ((item1 == 946) || (item2 == 946)) {
			int logId = -1;

			if (FletchLogData.forId(item1) != null)
				logId = item1;
			else if (FletchLogData.forId(item2) != null) {
				logId = item2;
			}

			if (logId != -1) {
				FletchLogData log = FletchLogData.forId(logId);

				if (log == null) {
					return false;
				}

				p.getAttributes().set("fletchingitemkey", Integer.valueOf(logId));
				p.getAttributes().set("fletchingtypekey", Integer.valueOf(0));

				if (logId == 1511) {
					if ((log.getRewards() == null) || (log.getRewards().length < 4)) {
						return false;
					}

					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8902, 170, log.getRewards()[0]));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8903, 170, log.getRewards()[1]));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8904, 170, log.getRewards()[2]));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8905, 170, log.getRewards()[3]));
					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat("15 Arrow Shafts"), 8909));
					p.getClient().queueOutgoingPacket(new SendString("\\n \\n \\n \\n \\n".concat("Short Bow"), 8913));
					p.getClient().queueOutgoingPacket(new SendString("\\n \\n \\n \\n \\n".concat("Long Bow"), 8917));
					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat("Crossbow Stock"), 8921));
					p.getClient().queueOutgoingPacket(new SendChatInterface(8899));
				} else if (logId == 1513) {
					if ((log.getRewards() == null) || (log.getRewards().length < 2)) {
						return false;
					}

					String prefix = Item.getDefinition(log.getRewards()[0]).getName().split(" ")[0];

					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8869, 170, log.getRewards()[0]));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8870, 170, log.getRewards()[1]));

					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat(prefix + " Short Bow"), 8871));
					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat(prefix + " Long Bow"), 8875));

					p.getClient().queueOutgoingPacket(new SendChatInterface(8866));
				} else {
					if ((log.getRewards() == null) || (log.getRewards().length < 3)) {
						return false;
					}

					String prefix = Item.getDefinition(log.getRewards()[0]).getName().split(" ")[0];
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8883, 170, log.getRewards()[0]));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8884, 170, log.getRewards()[1]));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(8885, 170, log.getRewards()[2]));
					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat(prefix + " Short Bow"), 8889));
					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat(prefix + " Long Bow"), 8893));
					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n".concat("Crossbow Stock"), 8897));
					p.getClient().queueOutgoingPacket(new SendChatInterface(8880));
				}

				return true;
			}
		} else {
			if ((item1 == 1777) || (item2 == 1777)) {
				int logId = -1;

				if (StringBowData.forId(item1) != null)
					logId = item1;
				else if (StringBowData.forId(item2) != null) {
					logId = item2;
				}

				if (logId != -1) {
					StringBowData log = StringBowData.forId(logId);

					p.getAttributes().set("fletchingitemkey", Integer.valueOf(logId));
					p.getAttributes().set("fletchingtypekey", Integer.valueOf(1));

					p.getClient()
							.queueOutgoingPacket(
									new SendString("\\n \\n \\n \\n \\n"
											+ Item.getDefinition(log.getProduct()).getName(), 2799));
					p.getClient().queueOutgoingPacket(new SendChatInterface(4429));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(1746, 170, log.getProduct()));
				}

				return true;
			}
			if ((item1 == 1755) || (item2 == 1755)) {
				BoltTipData data = BoltTipData.forId(item1);

				int id = item1;

				if (data == null) {
					data = BoltTipData.forId(item2);
					id = item2;
				}

				if (data != null) {
					p.getAttributes().set("fletchingitemkey", Integer.valueOf(id));
					p.getAttributes().set("fletchingtypekey", Integer.valueOf(2));

					p.getClient().queueOutgoingPacket(
							new SendString("\\n \\n \\n \\n \\n" + data.reward.getDefinition().getName(), 2799));
					p.getClient().queueOutgoingPacket(new SendChatInterface(4429));
					p.getClient().queueOutgoingPacket(new SendItemOnInterface(1746, 170, data.reward.getId()));
				}
			}
		}
		return false;
	}

	public static boolean clickButton(Player p, int id) {
		if (p.getAttributes().get("fletchingtypekey") == null) {
			return false;
		}

		int type = p.getAttributes().getInt("fletchingtypekey");

		if (type == 0) {
			if (p.getAttributes().get("fletchingitemkey") == null) {
				return false;
			}

			FletchLogData data = FletchLogData.forId(p.getAttributes().getInt("fletchingitemkey"));

			p.getAttributes().remove("fletchingitemkey");

			if (data == null) {
				return false;
			}

			switch (id) {
			case 34203:
			case 34204:
			case 34205:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34205 == 0 ? 1 : -((id - 34205) * 5)), data, 0));
				p.getAttributes().remove("fletchingtypekey");
				return true;
			case 34202:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 0));
				return true;
			case 34207:
			case 34208:
			case 34209:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34209 == 0 ? 1 : -((id - 34209) * 5)), data, 1));
				return true;
			case 34206:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 1));
				return true;
			case 34211:
			case 34212:
			case 34213:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34213 == 0 ? 1 : -((id - 34213) * 5)), data, 2));
				p.getAttributes().remove("fletchingtypekey");
				return true;
			case 34210:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 2));
				return true;
			case 34215:
			case 34216:
			case 34217:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34217 == 0 ? 1 : -((id - 34217) * 5)), data, 3));
				p.getAttributes().remove("fletchingtypekey");
				return true;
			case 34214:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 3));
				return true;
			case 34183:
			case 34184:
			case 34185:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34185 == 0 ? 1 : -((id - 34185) * 5)), data, 0));
				p.getAttributes().remove("fletchingtypekey");
				return true;
			case 34182:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 0));
				return true;
			case 34187:
			case 34188:
			case 34189:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34189 == 0 ? 1 : -((id - 34189) * 5)), data, 1));
				p.getAttributes().remove("fletchingtypekey");
				return true;
			case 34186:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 1));
				return true;
			case 34191:
			case 34192:
			case 34193:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34193 == 0 ? 1 : -((id - 34193) * 5)), data, 2));
				p.getAttributes().remove("fletchingtypekey");
				return true;
			case 34190:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 2));
				return true;
			case 34168:
			case 34169:
			case 34170:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34170 == 0 ? 1 : -((id - 34193) * 5)), data, 0));
				p.getAttributes().remove("fletchingtypekey");
				break;
			case 34167:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 0));
				p.getAttributes().remove("fletchingtypekey");
				break;
			case 34172:
			case 34173:
			case 34174:
				TaskQueue.queue(new FletchLogTask(p, (short) (id - 34174 == 0 ? 1 : -((id - 34193) * 5)), data, 1));
				p.getAttributes().remove("fletchingtypekey");
				break;
			case 34171:
				TaskQueue.queue(new FletchLogTask(p, (short) 28, data, 1));
				p.getAttributes().remove("fletchingtypekey");
			case 34175:
			case 34176:
			case 34177:
			case 34178:
			case 34179:
			case 34180:
			case 34181:
			case 34194:
			case 34195:
			case 34196:
			case 34197:
			case 34198:
			case 34199:
			case 34200:
			case 34201:
			default:
				break;
			}
		} else if (type == 1) {
			if (p.getAttributes().get("fletchingitemkey") == null) {
				return false;
			}

			StringBowData data = StringBowData.forId(p.getAttributes().getInt("fletchingitemkey"));

			p.getAttributes().remove("fletchingitemkey");

			if (data == null) {
				return false;
			}

			switch (id) {
			case 10239:
				TaskQueue.queue(new StringBowTask(p, (short) 1, data));
				return true;
			case 10238:
				TaskQueue.queue(new StringBowTask(p, (short) 5, data));
				return true;
			case 6212:
				TaskQueue.queue(new StringBowTask(p, (short) 28, data));
				return true;
			case 6211:
				TaskQueue.queue(new StringBowTask(p, (short) 28, data));
				return true;
			}
		} else if (type == 2) {
			if (p.getAttributes().get("fletchingitemkey") == null) {
				return false;
			}

			BoltTipData data = BoltTipData.forId(p.getAttributes().getInt("fletchingitemkey"));

			p.getAttributes().remove("fletchingitemkey");

			if (data == null) {
				return false;
			}

			switch (id) {
			case 10239:
				TaskQueue.queue(new BoltTipTask(p, (short) 1, data));
				return true;
			case 10238:
				TaskQueue.queue(new BoltTipTask(p, (short) 5, data));
				return true;
			case 6211:
			case 6212:
				TaskQueue.queue(new BoltTipTask(p, (short) 28, data));
				return true;
			}
		}

		return false;
	}
}
