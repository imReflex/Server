package org.endeavor.game.content.shopping.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.pets.PetData;
import org.endeavor.game.content.pets.Pets;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class VoteShop extends Shop {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1122401510657340903L;
	public static final int SHOP_ID = 95;

	public VoteShop() {
		super(95, new Item[] { new Item(2528, 1), new Item(6183, 1), new Item(7927, 1), new Item(6858, 1), new Item(6859, 1),new Item(12473), new Item(12471), new Item(12469), new Item(12475),
				new Item(10129, 1),
				new Item(11021, 1), new Item(11019, 1), new Item(11020, 1), new Item(11022, 1), new Item(15349),
				new Item(13107), new Item(13109), new Item(13111), new Item(13113),
				new Item(13115), new Item(18644), new Item(18645), new Item(18646), new Item(18647),
				new Item(18649), new Item(18650), new Item(18652), new Item(18648), new Item(13166, 1), 
				}, false, "Vote Shop");
	}

	public static final int getPrice(int id) {
		if ((id >= 10330) && (id <= 10352)) {
			return 30;
		}
		
		if (id >= 12469 && id <= 12473) {
			return 30;
		}
		
		if (id >= 18644 && id <= 18652 && id != 18648) {
			return 25;
		}
		
		if (id >= 13107 && id <= 13115) {
			return 40;
		}

		switch (id) {
		case 6858:
		case 6859:
			return 10;
		
		case 15349:
			return 25;
		case 18648:
			return 50;
		case 12475:
			return 50;
		case 13166:
			return 60;
		case 6183:
		case 11019:
		case 11020:
		case 11021:
		case 11022:
			return 5;
		case 7927:
		case 12473:
			return 20;
		case 10129:
			return 10;
		}

		return 5;
	}

	@Override
	public String getCurrencyName() {
		return "Vote points";
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
		
		if (id > 18644 && id <= 18652) {
			if (!player.getInventory().hasItemId(getItems()[slot - 1].getId())) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You need to have the " + getItems()[slot - 1].getDefinition().getName() + " in your inventory to buy this!"));
				return;
			}
		}
		
		if (id >= 12469 && id <= 12475 && id != 12473) {
			if (!player.getInventory().hasItemId(getItems()[slot - 1].getId())) {
				boolean alt = false;
				
				if (player.getPets().hasPet()) {
					if (PetData.isSamePet(PetData.getPetDataForMob(player.getPets().getPet().getMob().getId()).itemId, 
							getItems()[slot - 1].getId())) {
						alt = true;
					}
				}
				
				if (!alt) {
					for (Item i : player.getInventory().getItems()) { 
						if (i != null && Pets.isItemPet(i.getId())) {
							if (PetData.isSamePet(getItems()[slot - 1].getId(), i.getId())) {
								alt = true;
								break;
							}
						}
					}
				}
				
				if (!alt) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You need to have the " + getItems()[slot - 1].getDefinition().getName() + " in your inventory to buy this!"));
					return;
				}
			}
		}

		if (player.getVotePoints() < amount * getPrice(id)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough Vote points to buy that."));
			return;
		}

		player.setVotePoints(player.getVotePoints() - amount * getPrice(id));

		QuestTab.updateVotePoints(player);

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
