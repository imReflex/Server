package org.endeavor.game.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.entity.item.Item;

public enum BoltTipData {
	SAPPHIRE(new int[] { 1607 }, new Item(9189, 12), 8.0D, 56), EMERALD(new int[] { 1605 }, new Item(9190, 12), 11.0D,
			58), RUBY(new int[] { 1603 }, new Item(9191, 12), 13.0D, 63), DIAMOND(new int[] { 1601 },
			new Item(9192, 12), 14.0D, 65), DRAGONSTONE(new int[] { 1615 }, new Item(9193, 18), 8.199999999999999D, 71), ONYX(
			new int[] { 6574 }, new Item(9194, 24), 22.0D, 73);

	public final int[] req;
	public final Item reward;
	public final double exp;
	public final int level;
	private static Map<Integer, BoltTipData> boltTips = new HashMap<Integer, BoltTipData>();

	private BoltTipData(int[] req, Item reward, double exp, int level) {
		this.req = req;
		this.reward = reward;
		this.exp = exp;
		this.level = level;
	}

	public static int getAnimForData(BoltTipData data) {
		switch (data) {
		case DIAMOND:
			return 888;
		case DRAGONSTONE:
			return 889;
		case EMERALD:
			return 887;
		case ONYX:
			return 890;
		case RUBY:
			return 890;
		case SAPPHIRE:
			return 2717;
		}

		return 888;
	}

	public static final void declare() {
		for (BoltTipData tip : values()) {
			boltTips.put(Integer.valueOf(tip.req[0]), tip);
		}
	}

	public static BoltTipData forId(int item) {
		return boltTips.get(Integer.valueOf(item));
	}
}
