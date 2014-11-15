package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SlayerShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5849905103464267479L;
	public static final int SHOP_ID = 93;

	public SlayerShop() {
		super(93, new Item[] {
				new Item(15467),//free skip slayer task
				
				new Item(12673),//inbued helm
				new Item(12675),//inbued helm
				new Item(12677),//inbued helm
				new Item(12679),//inbued helm
				new Item(12681),//inbued helm
				
				new Item(1765, 1),//yellow whip dye
				new Item(1767, 1), //blue whip dye
				new Item(1771, 1),//green whip dye
				
				new Item(10551),//fighter torso
				new Item(10548),//fighter hat
				
				new Item(15378),//barrows icon
				
				new Item(8921),//black mask
				
				new Item(18653),//dragon defender
				
				new Item(4151, 1), //whip
				new Item(4087),//d legs
				
				
				new Item(15018),//imbued ring
				new Item(15019),//imbued ring
				new Item(15020),//imbued ring
				new Item(15220),//imbued ring
				
				new Item(13263, 1), //slayer helm
				new Item(2572, 1), //ROW
		
				new Item(11872),//void set
		
			}, false, "Slayer Shop");
	}

	public static final int getPrice(int id) {
		if (id >= 12673 && id <= 12681) {
			return 25;
		}
		
		switch (id) {
		case 2572:
			return 100;
			
		case 11872://void set
			return 130;
			
		case 13263:
			return 75;
		case 1765:
		case 1767:
		case 1771:
			return 35;
			
		case 15378://barrows icon
		case 8921://black mask
		case 10551://fighter troso
		case 10548://fighter hat
		case 18653://d defender
			return 40;
			
		case 4151://whip
		case 4087://d legs
			return 50;
			
		case 15018://imbued rings
		case 15019:
		case 15020:
		case 15220:
			return 60;
			
		case 15467://free skip slayer task
			return 8;
		}

		return 2147483647;
	}

	@Override
	public String getCurrencyName() {
		return "Slayer points";
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

		if (player.getSlayerPoints() < amount * getPrice(id)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough Slayer points to buy that."));
			return;
		}

		player.setSlayerPoints(player.getSlayerPoints() - amount * getPrice(id));

		QuestTab.updateSlayerPoints(player);

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
