package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SkillPointsShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5683740170042100832L;
	public static final int SHOP_ID = 90;

	public SkillPointsShop() {
		super(90, Shop.getShops()[22].getDefaultItems(), false, "Skill Point Shop");
	}

	public static final int getPrice(int id) {
		return 10;
	}

	@Override
	public String getCurrencyName() {
		return "Skill points";
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

		if (player.getSkillPoints() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough Skill points to buy that."));
			return;
		}

		player.setSkillPoints(player.getSkillPoints() - amount * getPrice(id));

		QuestTab.updateSkillPoints(player);

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
