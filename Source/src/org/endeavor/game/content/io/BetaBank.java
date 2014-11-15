package org.endeavor.game.content.io;

import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;

public class BetaBank {
	private static final Item[] bank = new Item[1080];

	public static final int[] SHOP_IDS = { 4, 1, 5, 2, 3, 6 };

	public static final Item[] getBank() {
		Item[] b = new Item[1080];

		for (int i = 0; i < 1080; i++) {
			if ((i > bank.length - 1) || (bank[i] == null))
				b[i] = null;
			else {
				b[i] = new Item(bank[i]);
			}
		}

		return b;
	}

	public static final void declare() {
		for (int i : SHOP_IDS) {
			Shop s = Shop.getShops()[i];

			for (Item k : s.getItems()) {
				if (k == null) {
					break;
				}
				for (int j = 0; j < bank.length; j++)
					if (bank[j] == null) {
						Item add = new Item(k.getId(), 2147483647);

						if (add.getDefinition().isNote()) {
							add.unNote();
						}

						bank[j] = new Item(add.getId(), 50000);
						break;
					}
			}
		}
	}
}
