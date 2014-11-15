package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PKPShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2653991044794413559L;
	public static final int PKP_SHOP_ID = 99;

	public PKPShop() {
		super(99, new Item[] { new Item(995, 1000000), new Item(13734), new Item(13754), new Item(14499, 1), new Item(14497, 1),
				new Item(14501, 1), new Item(4151, 1), new Item(11335, 1),
				new Item(11732, 1), new Item(6585, 1), new Item(11235, 1), new Item(11848, 1), new Item(15241, 1),
				new Item(11696, 1), new Item(11698, 1), new Item(11700, 1), new Item(11694, 1), 
				new Item(13746, 1), new Item(13752, 1), new Item(13748, 1), new Item(13750, 1), new Item(14484, 1), }, false,
				"Pk Point Shop");
	}

	public static final int getPrice(int id) {
		switch (id) {
		case 13734:
		case 13754:
			return 15;
	
		case 11694:
			return 65;
		case 13748:
		case 13750:
		case 14484:
			return 100;
			
		case 13746:
		case 13752:
			return 75;
		
		case 11696:
		case 11698:
		case 11700:
			return 45;
		case 11848:
		case 15241:
			return 20;
		case 8839:
			return 45;
		case 8840:
			return 45;
		case 8842:
		case 6585:
		case 10551:
			return 20;
		case 11674:
		case 11675:
		case 11676:
			return 25;
		case 10547:
		case 10548:
		case 10549:
		case 10550:
		case 10552:
		case 10553:
		case 4151:
		case 11335:
		case 11732:
		case 14497:
		case 14499:
		case 14501:
		case 11235:
			return 20;
		case 995:
		case 10499:
			return 10;
		case 11858:
			return 100;
		case 11942:
			return 250;
		}

		return 2147483647;
	}

	@Override
	public String getCurrencyName() {
		return "PKP";
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

		if (player.getEarningPotential().getPkp() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough PKP to buy that."));
			return;
		}

		player.getEarningPotential().decreasePKP(amount * getPrice(id));

		QuestTab.updatePKP(player);

		if (buying.getId() == 995) {
			buying.setAmount(1000000);
		}

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
