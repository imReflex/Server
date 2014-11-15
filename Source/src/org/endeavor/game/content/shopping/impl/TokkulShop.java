package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class TokkulShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4218495758127213208L;
	public static final int TOKKUL = 6529;
	public static final int SHOP_ID = 96;

	public TokkulShop() {
		super(96, new Item[] { new Item(6571, 1),}, 
				false,
				"Tokkul Shop");
	}

	public static final int getPrice(int id) {
		switch (id) {
		case 6571:
			return 25000;
		case 6568:
			return 15000;
		case 6524:
		case 6528:
			return 20000;
		}

		return 2147483647;
	}

	@Override
	public String getCurrencyName() {
		return "Tokkul";
	}

	@Override
	public boolean sell(Player player, int id, int amount) {
		player.getClient().queueOutgoingPacket(new SendMessage("You cannot sell items to this shop."));
		return false;
	}

	@Override
	public void buy(Player player, int slot, int id, int amount) {
		if (!hasItem(slot, id))
			return;
		if (get(slot).getAmount() == 0)
			return;
		if (amount > get(slot).getAmount()) {
			amount = get(slot).getAmount();
		}

		Item buying = new Item(id, amount);

		if (!player.getInventory().hasSpaceFor(buying)) {
			if (!buying.getDefinition().isStackable()) {
				int slots = player.getInventory().getFreeSlots();
				if (slots > 0) {
					buying.setAmount(slots);
					amount = slots;
				} else {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough inventory space to buy this item."));
				}
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough inventory space to buy this item."));
				return;
			}
		}

		if (player.getInventory().getItemAmount(6529) < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough Tokull to buy that."));
			return;
		}

		player.getInventory().remove(6529, amount * getPrice(id));

		player.getInventory().add(buying);
		update();
	}

	@Override
	public int getBuyPrice(int id) {
		return 0;
	}

	@Override
	public int getSellPrice(int id) {
		return getPrice(id);
	}
}
