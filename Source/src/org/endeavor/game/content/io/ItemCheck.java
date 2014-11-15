package org.endeavor.game.content.io;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class ItemCheck {
	public static final ItemReset[] ITEM_RESETS = { new ItemReset(6529, 2013, 310), new ItemReset(6570, 2013, 310) };

	public static Item check(Player player, Item item) {
		try {
			if (item == null) {
				return null;
			}

			if (item.getId() == 2513) {
				item.setId(3140);
				return item;
			}

			if (item.getId() == 4212) {
				item.setId(4214);
				return item;
			}

			for (int k : org.endeavor.game.content.minigames.dungeoneering.DungConstants.CHEST_LOOT_TABLE[1]) {
				if (item.getId() == k) {
					return null;
				}

			}

			for (ItemReset k : ITEM_RESETS) {
				if ((player.getLastLoginDay() <= k.day) && (player.getLastLoginYear() <= k.year)
						&& (item.getId() == k.id))
					return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return item;
	}

	public static class ItemReset {
		public final int id;
		public final int year;
		public final int day;

		public ItemReset(int id, int year, int day) {
			this.id = id;
			this.year = year;
			this.day = day;
		}
	}
}
