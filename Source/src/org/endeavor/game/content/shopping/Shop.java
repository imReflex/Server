package org.endeavor.game.content.shopping;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.shopping.impl.AgilityShop;
import org.endeavor.game.content.shopping.impl.DonatorShop;
import org.endeavor.game.content.shopping.impl.DungShop;
import org.endeavor.game.content.shopping.impl.PKPShop;
import org.endeavor.game.content.shopping.impl.PestShop;
import org.endeavor.game.content.shopping.impl.RFDChestShop;
import org.endeavor.game.content.shopping.impl.SkillPointsShop;
import org.endeavor.game.content.shopping.impl.SlayerShop;
import org.endeavor.game.content.shopping.impl.TokkulShop;
import org.endeavor.game.content.shopping.impl.VoteShop;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.summoning.Pouch;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.ItemContainer;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Shop extends ItemContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8914557860150970027L;
	public static final int SHOP_SIZE = 40;
	private static Shop[] shops = new Shop[100];
	private final int id;
	private final Item[] defaultItems;
	private boolean general = false;
	private String name;
	private int restock = 50;

	private long update = System.currentTimeMillis();

	public static void declare() {
		shops[99] = new PKPShop();
		shops[98] = new RFDChestShop();
		shops[97] = new DungShop();
		shops[96] = new TokkulShop();
		shops[95] = new VoteShop();
		shops[93] = new SlayerShop();
		shops[92] = new DonatorShop();
		shops[91] = new AgilityShop();
		shops[90] = new SkillPointsShop();
		shops[PestShop.SHOP_ID] = new PestShop();

		Item[] stock = new Item[40];
		Item[] stock2 = new Item[40];

		stock[0] = new Item(18016, 800000);
		stock[1] = new Item(12525, 100000);

		for (int i = 2; i < stock.length; i++) {
			int id = Pouch.values()[(i - 2)].secondIngredientId;

			if ((id != 1635) && (id != 440) && (id != 1519) && (id != 2349) && (id != 249) && (id != 590)
					&& (id != 2351) && (id != 3095)) {
				stock[i] = new Item(Pouch.values()[(i - 2)].secondIngredientId, 50000);
			}
		}
		for (int i = 0; i < stock.length; i++) {
			if (i + 50 >= Pouch.values().length) {
				break;
			}
			int id = Pouch.values()[(i + 50)].secondIngredientId;

			if ((id != 383) && (id != 2363) && (id != 2361) && (id != 1635) && (id != 6155) && (id != 1119)
					&& (id != 1115)) {
				stock2[i] = new Item(id, 50000);
			}
		}
		new Shop(50, stock, false, "Summoning Shop");
		new Shop(51, stock2, false, "Summoning Shop");
	}

	public Shop(Item[] stock, String name) {
		super(40, ItemContainer.ContainerTypes.ALWAYS_STACK, false, true);
		general = false;
		this.name = name;
		id = -1;
		defaultItems = (stock.clone());
	}

	public Shop(int id, Item[] stock, boolean general, String name) {
		super(40, ItemContainer.ContainerTypes.ALWAYS_STACK, true, false);
		this.general = general;
		this.name = name;
		this.id = id;
		shops[id] = this;

		defaultItems = (stock.clone());
		for (int i = 0; i < stock.length; i++) {
			if (stock[i] != null) {
				setSlot(new Item(stock[i]), i);
			}
		}

		shift();

		if ((id == 19) || (id == 20)) {
			setSkillCapeData();
		}

		TaskQueue.queue(new Task(restock) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3423469222510245796L;

			@Override
			public void execute() {
				refreshContainers();
			}

			@Override
			public void onStop() {
			}
		});
	}

	@Override
	public void update() {
		update = System.currentTimeMillis();
	}

	public String getCurrencyName() {
		return null;
	}

	public boolean sell(Player player, int id, int amount) {
		if (id == 995) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot sell coins to a shop."));
			return false;
		}

		if (ItemCheck.isDungeoneeringItem(id)) {
			player.getInventory().remove(id);
			return false;
		}

		if (!Item.getDefinition(id).isTradable()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot sell this item."));
			return false;
		}

		if ((this.id == 21) || ((!general) && (!isDefaultItem(id)))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot sell this item to this shop."));
			return false;
		}

		if (amount > 5000) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can only sell 5000 at a time to these shops."));
			amount = 5000;
		}

		int invAmount = player.getInventory().getItemAmount(id);

		if (invAmount == 0)
			return false;
		if (invAmount < amount) {
			amount = invAmount;
		}

		Item item = new Item(id, amount);

		if (!hasSpaceFor(item)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("The shop does not have enough space to buy this item."));
			return false;
		}

		Item gold = new Item(995, getBuyPrice(id) * amount);

		if (!player.getInventory().hasSpaceOnRemove(item, gold)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough inventory space to sell this item."));
			return false;
		}

		player.getInventory().remove(item);
		
		if (gold.getAmount() > 0) {
			player.getInventory().add(gold);
		}

		add(item);
		update();
		return true;
	}

	public void buy(Player player, int slot, int id, int amount) {
		if (amount > 500) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can only buy 500 at a time from these shops."));
			amount = 500;
		}

		if ((this.id == 21) && (player.getRights() != 1) && (player.getRights() != 2)) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You must be a donator to purchase from this shop."));
			return;
		}

		if (!hasItem(slot, id))
			return;
		if (get(slot).getAmount() == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("The shop is out of stock on that item."));
			return;
		}
		if (amount > get(slot).getAmount()) {
			amount = get(slot).getAmount();
		}

		Item buying = new Item(id, amount);

		Item gold = new Item(995, getSellPrice(id) * amount);

		if (!player.getInventory().hasSpaceOnRemove(gold, buying)) {
			if (!buying.getDefinition().isStackable()) {
				int slots = player.getInventory().getFreeSlots();
				if (slots > 0) {
					buying.setAmount(slots);
					amount = slots;
					gold.setAmount(getSellPrice(id) * amount);
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

		if (gold.getAmount() > 0) {
			if (!player.getInventory().hasItemAmount(gold)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough coins to buy that."));
				return;
			}
		}

		if (this.id != 21) {
			int newAmount = get(slot).getAmount() - amount;

			if (newAmount < 1) {
				if (isDefaultItem(id))
					get(slot).setAmount(0);
				else
					remove(get(slot));
			} else {
				get(slot).setAmount(newAmount);
			}
		}

		if (gold.getAmount() > 0) {
			player.getInventory().remove(gold, false);
		}

		player.getAchievements().incr(player, "Spend 10,000,000 in Shops", gold.getAmount());
		player.getAchievements().incr(player, "Spend 10,000,000 in Shops", gold.getAmount());

		if (((this.id == 19) || (this.id == 20)) && (buying.getDefinition().getName().contains("cape"))
				&& (player.getSkill().hasTwo99s()) && (buying.getId() != 18654)) {
			buying.setId(buying.getId() + 1);
		}

		if (this.id == 13) {
			buying.setId(buying.getDefinition().getNoteId() > -1 ? buying.getDefinition().getNoteId() : buying.getId());
		}

		player.getInventory().add(buying);
		update();
	}

	public void refreshContainers() {
		Item[] items = getItems();

		for (int j = 0; j < 40; j++) {
			if (items[j] == null) {
				break;
			}
			Item stock = getDefaultItem(items[j].getId());

			if (stock != null) {
				if (items[j].getAmount() < stock.getAmount())
					items[j].add(1);
				else if (items[j].getAmount() > stock.getAmount()) {
					items[j].remove(1);
				}
			} else if (items[j].getAmount() > 1)
				items[j].remove(1);
			else {
				remove(getItems()[j]);
			}

		}

		update();
	}

	public boolean isDefaultItem(int id) {
		for (Item i : defaultItems) {
			if ((i != null) && (i.getId() == id)) {
				return true;
			}
		}

		return false;
	}

	public Item getDefaultItem(int id) {
		for (Item i : defaultItems) {
			if ((i != null) && (i.getId() == id)) {
				return i;
			}
		}

		return null;
	}

	public int getBuyPrice(int id) {
		if (this.id == 21) {
			return 0;
		}

		return GameDefinitionLoader.getStoreSellToValue(id);
	}

	public int getSellPrice(int id) {
		if (this.id == 21) {
			return 0;
		}

		return GameDefinitionLoader.getStoreBuyFromValue(id);
	}

	public boolean hasItem(int slot, int id) {
		return (get(slot) != null) && (get(slot).getId() == id);
	}

	public boolean empty(int slot) {
		return (get(slot) == null) || (get(slot).getAmount() == 0);
	}

	public static Shop[] getShops() {
		return shops;
	}

	public String getName() {
		return name;
	}

	public boolean isGeneral() {
		return general;
	}

	@Override
	public void onFillContainer() {
	}

	@Override
	public void onMaxStack() {
	}

	@Override
	public boolean allowZero(int id) {
		return isDefaultItem(id);
	}

	public boolean isUpdate() {
		return System.currentTimeMillis() - update < 1000L;
	}

	public void setSkillCapeData() {
		for (Item i : defaultItems) {
			if (i.getDefinition().getName().contains("hood"))
				GameDefinitionLoader.setValue(i.getId(), 25000, 15000);
			else if (i.getDefinition().getName().contains("cape")) {
				if (i.getId() == 18654) {
					GameDefinitionLoader.setValue(i.getId(), 25000000, 10000000);
				} else {
					GameDefinitionLoader.setValue(i.getId(), 99000, 50000);
					GameDefinitionLoader.setValue(i.getId() + 1, 99000, 50000);
				}
			}

			String skill = i.getDefinition().getName().substring(0, i.getDefinition().getName().indexOf(" "))
					.toLowerCase();

			if (skill.equals("construct.")) {
				skill = "construction";
			}

			if (skill.equals("constitution")) {
				skill = "hitpoints";
			}

			if (skill.equals("ranging")) {
				skill = "ranged";
			}

			if (skill.equals("runecraft")) {
				skill = "runecrafting";
			}

			for (int k = 0; k < SkillConstants.SKILL_NAMES.length; k++) {
				if (SkillConstants.SKILL_NAMES[k].toLowerCase().equals(skill)) {
					if (i.getDefinition().getName().contains("cape")) {
						GameDefinitionLoader.getEquipmentDefinition(i.getId() + 1).setRequirements(new byte[25]);
						GameDefinitionLoader.getEquipmentDefinition(i.getId() + 1).getRequirements()[k] = (byte) (k == SkillConstants.DUNGEONEERING ? 120 : 99);
					}

					GameDefinitionLoader.getEquipmentDefinition(i.getId()).setRequirements(new byte[25]);
					GameDefinitionLoader.getEquipmentDefinition(i.getId()).getRequirements()[k] = (byte) (k == SkillConstants.DUNGEONEERING ? 120 : 99);
					break;
				}
			}

			GameDefinitionLoader.setNotTradable(i.getId());
		}

		GameDefinitionLoader.setRequirements();
	}

	public Item[] getDefaultItems() {
		return defaultItems;
	}

	@Override
	public void onAdd(Item item) {
	}

	@Override
	public void onRemove(Item item) {
	}

	public void setName(String name) {
		this.name = name;
	}
}
