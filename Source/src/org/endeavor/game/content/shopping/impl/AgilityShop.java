package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class AgilityShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8835862028245196559L;
	public static final int SHOP_ID = 91;

	public AgilityShop() {
		super(91, new Item[] { new Item(14936, 1), new Item(14938, 1), new Item(4447, 1), new Item(9470, 1),
				new Item(2997, 1), new Item(9472, 1), new Item(3257, 1) }, false, "Agility Shop");

		for (Item i : getItems())
			if (i != null)
				i.getDefinition().setUntradable();
	}

	public static final int getPrice(int id) {
		if ((id >= 14936) && (id <= 14938)) {
			return 800;
		}

		if (id == 3257) {
			return 1250;
		}

		return 500;
	}

	@Override
	public String getCurrencyName() {
		return "Agility tokens";
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

		if (player.getInventory().getItemAmount(2996) < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough Agility tokens to buy that."));
			return;
		}

		player.getInventory().remove(2996, amount * getPrice(id), true);

		QuestTab.updateDonatorPoints(player);

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
