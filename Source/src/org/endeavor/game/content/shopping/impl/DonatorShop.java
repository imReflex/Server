package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class DonatorShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5944432299324105167L;
	public static final int SHOP_ID = 92;

	public DonatorShop() {
		super(92, new Item[] { 
				/*new Item(13663, 1), new Item(13664, 1), */new Item(10551, 1), 
				new Item(1769, 1), new Item(4079, 1), new Item(6570, 1),
				
				new Item(19111),//tokhaar
				
				new Item(1037),//bunny ears
				
				new Item(11872, 1),//void knight set
				new Item(11858),//third age melee
				new Item(11860),//third age ranged
				new Item(11862),//third age mage
				
				
				new Item(1053, 1), //hween
				new Item(1055, 1), //hween
				new Item(1057, 1), //hween
				
				new Item(13899),//vesta longsword
				new Item(13902),//statius's warhammer
				new Item(13867),//zuriel's staff
				
				new Item(13738), //arcane ss
				new Item(13744), //spectral ss
				
				new Item(13740),//divine ss
				new Item(13742),//elysium ss
				
				new Item(1050),//santa
				
				new Item(18351), new Item(18357), new Item(18335), new Item(18349),//chaotics
				
				new Item(1038, 1), new Item(1040, 1), new Item(1042, 1),//phats
				new Item(1044, 1), new Item(1046, 1), new Item(1048, 1),
				
				new Item(14484, 1),
		}, false, "Donator Shop");
	}

	public static final int getPrice(int id) {
		if (id >= 1038 && id <= 1048) {
			return 20;
		}
		
		if (id >= 18335 && id <= 18357) {
			return 20;
		}

		switch (id) {
		case 14484:
			return 30;
		
		case 13738:
		case 13744:
		case 13899:
		case 13902:
		case 13867:
			return 10;
		
		case 13740:
		case 13742:
			return 15;
		
		case 19111:
			return 3;
		
		case 1037:
			return 3;
		
		case 1050:
			return 15;
		
		case 13663:
			return 4;
		case 4079:
			return 1;
		case 1769:
		case 10551:
			return 1;
		case 6570:
			return 2;
		case 11872:
		case 11858:
		case 11860:
		case 11862:
			return 5;
		case 1053:
		case 1055:
		case 1057:
		case 5020:
		case 15426:
			return 5;
		}

		return 2147483647;
	}

	@Override
	public String getCurrencyName() {
		return "Donation points";
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

		if (player.getDonationPoints() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough Donation points to buy that."));
			return;
		}

		player.setDonationPoints(player.getDonationPoints() - amount * getPrice(id));

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
