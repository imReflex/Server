package org.endeavor.game.content.minigames.barrows;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;

public class BarrowsRewardTable {
	private static final Reward[] COMMON = { new Reward(995, 50000, 75000), new Reward(558, 250, 750),
			new Reward(562, 250, 500), new Reward(560, 100, 200), new Reward(565, 50, 150), new Reward(4740, 50, 145) };

	private static final Reward[] UNCOMMON = { new Reward(165, 1, 1), new Reward(141, 1, 1), new Reward(129, 1, 1),
			new Reward(385, 4, 4) };

	private static final Reward[] RARE = { new Reward(985, 1, 1), new Reward(987, 1, 1), new Reward(1149, 1, 1),
			new Reward(4708, 1, 1), new Reward(4710, 1, 1), new Reward(4712, 1, 1), new Reward(4714, 1, 1),
			new Reward(4716, 1, 1), new Reward(4718, 1, 1), new Reward(4720, 1, 1), new Reward(4722, 1, 1),
			new Reward(4724, 1, 1), new Reward(4726, 1, 1), new Reward(4728, 1, 1), new Reward(4730, 1, 1),
			new Reward(4732, 1, 1), new Reward(4734, 1, 1), new Reward(4736, 1, 1), new Reward(4738, 1, 1),
			new Reward(4745, 1, 1), new Reward(4747, 1, 1), new Reward(4749, 1, 1), new Reward(4751, 1, 1),
			new Reward(4753, 1, 1), new Reward(4755, 1, 1), new Reward(4757, 1, 1), new Reward(4759, 1, 1) };

	public static Item[] roll() {
		int amount = 2 + (Misc.randomNumber(2) == 0 ? 1 : 0);
		Item[] rewards = new Item[amount];

		int c = 0;

		if (Misc.randomNumber(3) == 0) {
			Reward r = RARE[Misc.randomNumber(RARE.length)];
			rewards[c] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);

			c++;

			if (Misc.randomNumber(7) == 0) {
				r = RARE[Misc.randomNumber(RARE.length)];
				rewards[c] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);

				c++;
			}
		}

		while (c < rewards.length) {
			if (Misc.randomNumber(2) == 0) {
				Reward r = COMMON[Misc.randomNumber(COMMON.length)];
				rewards[c] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);
			} else {
				Reward r = UNCOMMON[Misc.randomNumber(UNCOMMON.length)];
				rewards[c] = new Item(r.id, r.max > r.min ? r.min + Misc.randomNumber(r.max - r.min) : r.min);
			}

			c++;
		}

		return rewards;
	}

	public static class Reward {
		private final int id;
		private final int min;
		private final int max;

		public Reward(int id, int min, int max) {
			this.id = id;
			this.min = min;
			this.max = max;
		}
	}
}
