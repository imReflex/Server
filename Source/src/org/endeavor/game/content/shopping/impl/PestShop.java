package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PestShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3325078535398635008L;
	public static final int SHOP_ID = 89;

	public PestShop() {
		super(SHOP_ID, new Item[] { new Item(15501), new Item(10499, 1), new Item(10548, 1), new Item(10547, 1),
				new Item(10549, 1), new Item(10550, 1), new Item(10551, 1), new Item(8842, 1), new Item(8839, 1), new Item(8840, 1), new Item(11674, 1),
				new Item(11675, 1), new Item(11676, 1)}, false,
				"Pest Points Shop");
	}

	public static final int getPrice(int id) {
		switch (id) {
		case 15501:
			return 4 * 3;
		case 10499:
		case 8842:
			return 14;
		case 10548:
		case 10547:
		case 10549:
		case 10550:
		case 10551:
			return 2 * 15;
		case 8839:
		case 8840:
		case 11674:
		case 11675:
		case 11676:
			return 35;
			
		case 17273:
			return 45;
		}

		return 2147483647;
	}

	@Override
	public String getCurrencyName() {
		return "Pest points";
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

		if (player.getPestPoints() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough Pest points to buy that."));
			return;
		}

		player.setPestPoints(player.getPestPoints() - (amount * getPrice(id)));

		QuestTab.updatePestPoints(player);

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
