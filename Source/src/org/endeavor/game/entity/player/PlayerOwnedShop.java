package org.endeavor.game.entity.player;

import org.endeavor.GameSettings;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.out.impl.SendEnterString;
import org.endeavor.game.entity.player.net.out.impl.SendEnterXInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

/*public class PlayerOwnedShop extends Shop {
	
	private static final long serialVersionUID = -715639113783052L;
	public static boolean disabled = false;
	public static final String ADDING_ITEM_KEY = "addingitemkey";
	private final Player owner;
	private int[] prices = new int[40];

	private String search = null;

	public PlayerOwnedShop(Player owner) {
		super(new Item[40], owner.getUsername());
		this.owner = owner;
	}

	@Override
	public void refreshContainers() {
	}

	public int getItemCount() {
		int c = 0;

		for (Item i : getItems()) {
			if (i != null) {
				c++;
			}
		}

		return c;
	}

	public boolean hasAnyItems() {
		for (Item i : getItems()) {
			if (i != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getSellPrice(int id) {
		int slot = getItemSlot(id);

		if (slot == -1) {
			return 0;
		}
		
		return prices[slot];
	}

	@Override
	public boolean isDefaultItem(int id) {
		return false;
	}

	public boolean hasItemWithText(String text) {
		for (Item i : getItems()) {
			if ((i != null) && (i.getDefinition().getName().toLowerCase().contains(text.toLowerCase()))) {
				return true;
			}
		}

		return false;
	}

	public void doSearch() {
		int c = 0;

		owner.getClient().queueOutgoingPacket(new SendString("Searched: " + search, 8144));

		while (c <= 50) {
			owner.getClient().queueOutgoingPacket(new SendString("", 8145 + c));
			c++;
		}

		c = 0;

		while (c <= 49) {
			owner.getClient().queueOutgoingPacket(new SendString("", 12174 + c));
			c++;
		}

		c = 0;

		for (Player p : World.getPlayers()) {
			if ((p != null) && (p.isActive()) && (p.getPlayerShop().hasItemWithText(search))) {
				if (c <= 50)
					owner.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 8147 + c));
				else {
					owner.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 12174 + c - 50));
				}

				c++;
			}
		}

		owner.getClient().queueOutgoingPacket(new SendInterface(8134));

		owner.setEnterXInterfaceId(55777);
		owner.getClient().queueOutgoingPacket(new SendEnterString());
	}

	@Override
	public boolean sell(Player player, int id, int amount) {
		if (disabled) {
			player.getClient().queueOutgoingPacket(new SendMessage("Player owned shopping is disabled at the moment."));
			return false;
		}

		if ((PlayerConstants.isOwner(player)) && (!GameSettings.DEV_MODE)) {
			player.getClient().queueOutgoingPacket(new SendMessage("Owners cannot sell items."));
			return false;
		}

		if (!player.equals(owner)) {
			return false;
		}

		if ((!Item.getDefinition(id).isTradable()) || (id == 995)) {
			owner.getClient().queueOutgoingPacket(new SendMessage("You cannot sell this item."));
			return false;
		}

		if (((getItemCount() == 16) && (player.getMemberRank() <= 0)) || ((getItemCount() == 32) && (player.getMemberRank() > 0))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot put anymore items into your shop."));
			return false;
		}

		int invAmount = player.getInventory().getItemAmount(id);

		if (invAmount == 0)
			return false;
		if (amount > invAmount) {
			amount = invAmount;
		}

		player.getAttributes().set("addingitemkey", new Item(id, amount));

		player.getClient().queueOutgoingPacket(new SendEnterXInterface(15460, 0));

		return true;
	}

	public void onSetPrice(Player player, int price) {
		if (disabled) {
			player.getClient().queueOutgoingPacket(new SendMessage("Player owned shopping is disabled at the moment."));
			return;
		}
		
		if (price < 1) {
			player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive price for this item."));
			return;
		}

		if (player.getAttributes().get("addingitemkey") == null) {
			return;
		}

		final Item item = (Item) player.getAttributes().get("addingitemkey");
		final int setPrice = price;

		if (item == null) {
			return;
		}

		player.start(new ConfirmDialogue(player, new String[] { "Are you sure you want to sell:",
				item.getDefinition().getName(), "For: " + Misc.formatCoins(setPrice) + "?" }) {
			@Override
			public void onConfirm() {
				if (player.getAttributes().get("addingitemkey") == null) {
					return;
				}

				int invAmount = player.getInventory().getItemAmount(item.getId());

				if (invAmount == 0)
					return;
				if (item.getAmount() > invAmount) {
					item.setAmount(invAmount);
				}

				add(new Item(item));
				player.getInventory().remove(new Item(item));

				if (setPrice + (int) (setPrice * 0.01D) < 0)
					prices[getItemSlot(item.getId())] = setPrice;
				else {
					prices[getItemSlot(item.getId())] = setPrice;
				}

				player.getClient().queueOutgoingPacket(new SendUpdateItems(3900, getItems()));
				player.getClient().queueOutgoingPacket(new SendUpdateItems(3823, player.getInventory().getItems()));

				update();

				player.getShopping().open(player);
			}
		});
	}

	@Override
	public void buy(Player player, final int slot, final int id, int amount) {
		if (disabled) {
			player.getClient().queueOutgoingPacket(new SendMessage("Player owned shopping is disabled at the moment."));
			return;
		}

		if ((PlayerConstants.isOwner(player)) && (!GameSettings.DEV_MODE)) {
			player.getClient().queueOutgoingPacket(new SendMessage("Owners cannot buy items."));
			return;
		}

		if ((!Item.getDefinition(id).isTradable()) && (!player.equals(owner))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot buy this item."));
			return;
		}

		if (!hasItem(slot, id))
			return;
		if (get(slot).getAmount() == 0)
			return;
		if (amount > get(slot).getAmount()) {
			amount = get(slot).getAmount();
		}

		if (!player.getInventory().hasSpaceFor(new Item(id, amount))) {
			if (Item.getDefinition(id).isStackable()) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough inventory space to buy this item."));
				return;
			}

			int slots = player.getInventory().getFreeSlots();

			if (slots > 0) {
				amount = slots;
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough inventory space to buy this item."));
				return;
			}
		}

		final Item buying = new Item(id, amount);
		final Item gold = new Item(995, getSellPrice(id) * amount);

		if (player.equals(owner)) {
			remove(new Item(buying));
			player.getInventory().add(buying);
			update();
			return;
		}

		final int price = getSellPrice(id);
		final int goldAmount = prices[slot] * amount;

		player.start(new ConfirmDialogue(player, new String[] { "Are you sure you want to buy:",
				Item.getDefinition(id).getName() + " x " + amount, "For: " + Misc.formatCoins(price) + " each?" }) {
			@Override
			public void onConfirm() {
				if ((getItems()[slot] == null) || (getItems()[slot].getId() != id) || (getSellPrice(id) != price)) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("The price or item has changed, please try again."));
					player.getShopping().open(owner);
					return;
				}

				if (!player.getInventory().hasItemAmount(new Item(gold))) {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You do not have enough coins to buy that."));
					return;
				}

				if (!owner.getBank().hasSpaceFor(new Item(gold))) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("The other player is out of bank space, please notify him."));
					return;
				}

				remove(new Item(buying));

				player.getInventory().remove(new Item(gold), false);
				
				player.getAchievements().incr(player, "Spend 10,000,000 in Shops", gold.getAmount());
				player.getAchievements().incr(player, "Spend 10,000,000 in Shops", gold.getAmount());
				
				player.getInventory().add(buying);

				gold.setAmount(goldAmount);

				owner.getBank().add(new Item(gold));

				owner.getClient().queueOutgoingPacket(
						new SendMessage(Misc.formatCoins(gold.getAmount())
								+ " has been added to your bank from your shop sales."));

				update();

				player.getShopping().open(owner);
			}
		});
	}

	public int[] getPrices() {
		return prices;
	}

	public void setPrices(int[] prices) {
		this.prices = prices;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public boolean hasSearch() {
		return search != null;
	}

	public void resetSearch() {
		search = null;
	}

	public static void setDisabled(boolean set) {
		disabled = set;
	}
}*/
