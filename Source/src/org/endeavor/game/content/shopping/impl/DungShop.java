package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class DungShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3733648305666670674L;
	public static final int DUNG_SHOP_ID = 97;

	public DungShop() {
		super(97, new Item[] { new Item(18334, 1), new Item(18349, 1), new Item(18351, 1), new Item(18353, 1),
				new Item(18355, 1), new Item(18357, 1), new Item(18335, 1), new Item(18361, 1), new Item(18363, 1) },
				false, "Dungeoneering Shop");

		for (Item i : getDefaultItems())
			if (i != null)
				i.getDefinition().setUntradable();
	}

	public static final int getPrice(int id) {
		switch (id) {
		case 18349:
		case 18351:
		case 18353:
		case 18355:
		case 18357:
		case 18361:
		case 18363:
			return 300;
		case 18334:
			return 100;
		case 18335:
			return 175;
		case 10499:
			return 15;
		case 8850:
		case 10551:
		case 11674:
		case 11675:
		case 11676:
			return 75;
		case 8842:
		case 10547:
		case 10548:
		case 10549:
		case 10550:
		case 10552:
		case 10553:
			return 50;
		case 8839:
		case 8840:
			return 100;
		}

		return 2147483647;
	}

	@Override
	public String getCurrencyName() {
		return "Dungeoneering points";
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

		if (player.getDungPoints() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough Dungeoneering points to buy that."));
			return;
		}

		player.setDungPoints(player.getDungPoints() - amount * getPrice(id));

		QuestTab.updateDungPoints(player);

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
