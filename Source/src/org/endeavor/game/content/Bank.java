package org.endeavor.game.content;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.ItemContainer;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendInventory;
import org.endeavor.game.entity.player.net.out.impl.SendInventoryInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

public class Bank extends ItemContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2707350476461191526L;
	public static final int ONE_TAB_SIZE = 120;
	public static final int SIZE = 1080;
	public static final int TAB_AMOUNT = 9;
	public static final int BANK_TAB_ITEM_CONFIG = 877;
	private transient Player player;
	public RearrangeTypes rearrangeType = RearrangeTypes.SWAP;
	public WithdrawTypes withdrawType = WithdrawTypes.ITEM;

	private byte tab = 0;

	public Bank(Player player) {
		super(1080, ItemContainer.ContainerTypes.ALWAYS_STACK, true, false);
		this.player = player;
	}

	public void openBank() {
		if (player.isBusy()) {
			return;
		}

		update();
		player.getClient().queueOutgoingPacket(new SendSound(1457, 0, 0));
		player.getClient().queueOutgoingPacket(new SendInventoryInterface(5292, 5063));
	}

	public void shiftAll() {
		for (int i = 0; i < TAB_AMOUNT; i++) {
			shift(i * ONE_TAB_SIZE, (i * ONE_TAB_SIZE) + ONE_TAB_SIZE - 1);
		}
	}

	public void setTab(int tab) {
		if ((tab > 1) && (getItems()[(ONE_TAB_SIZE * tab)] == null) && (getItems()[(ONE_TAB_SIZE * (tab - 1))] == null)) {
			return;
		}

		if (this.tab == tab) {
			return;
		}

		this.tab = ((byte) tab);
		player.getClient().queueOutgoingPacket(new SendConfig(876, tab));
		update();
	}

	public void deposit(Item i, int slot) {
		deposit(i.getId(), i.getAmount(), slot);
	}

	public byte getTabForItem(int id) {
		main: for (int i = 0; i < TAB_AMOUNT; i++) {
			
			//search this tab's items
			for (int k = i * ONE_TAB_SIZE; k < (i * ONE_TAB_SIZE) + ONE_TAB_SIZE; k++) {
				
				if (getItems()[k] != null) {
					if (getItems()[k].getId() == id) {
						return (byte) i;
					}
				} else {
					
					continue main;
				}
				
			}
			
		}
		
		return -1;
	}

	public void deposit(int id, int amount, int slot) {
		if (!player.getInterfaceManager().hasBankOpen()) {
			return;
		}

		if (!player.getInventory().slotContainsItem(slot, id)) {
			return;
		}

		int invAmount = player.getInventory().getItemAmount(id);

		if (invAmount < amount) {
			amount = invAmount;
		}
		
		int tabWithItem = getTabForItem(Item.getDefinition(id).isNote() ? Item.getDefinition(id).getNoteId() : id);
		int added = 0;
		
		if (tabWithItem != -1) {
			added = add(new Item(id, amount), tabWithItem * ONE_TAB_SIZE, (tabWithItem * ONE_TAB_SIZE) + ONE_TAB_SIZE - 1, true);
		} else {
			added = add(new Item(id, amount), tab * ONE_TAB_SIZE, (tab * ONE_TAB_SIZE) + ONE_TAB_SIZE - 1, true);
		}

		if (added > 0) {
			if (amount == 1 && !Item.getDefinition(id).isStackable()) {
				player.getInventory().setSlot(null, slot);
			} else {
				player.getInventory().remove(id, added);
			}
		} else {
			added = add(new Item(id, amount), true);
			
			if (added > 0) {
				if (amount == 1 && !Item.getDefinition(id).isStackable()) {
					player.getInventory().setSlot(null, slot);
				} else {
					player.getInventory().remove(id, added);
				}
			}
		}
	}

	public int depositFromNoting(int id, int amount, int slot, boolean update) {
		int added = add(new Item(id, amount), true);

		return added;
	}

	public void withdraw(int slot, int amount) {
		if (!player.getInterfaceManager().hasBankOpen()) {
			return;
		}

		slot += tab * ONE_TAB_SIZE;

		if (!slotHasItem(slot)) {
			return;
		}

		int id = getSlotId(slot);
		int invId = id;
		int bankAmount = getSlotAmount(slot);

		if (bankAmount < amount) {
			amount = bankAmount;
		}

		if (withdrawType == WithdrawTypes.NOTE) {
			if (!Item.getDefinition(id).canNote())
				player.getClient().queueOutgoingPacket(new SendMessage("This item cannot be withdrawn as a note."));
			else {
				invId = Item.getDefinition(id).getNoteId();
			}
		}

		int added = player.getInventory().add(invId, amount, false);

		if (added > 0) {
			remove(new Item(id, added), tab * ONE_TAB_SIZE, (tab * ONE_TAB_SIZE) + ONE_TAB_SIZE - 1, true);
		}
	}

	public boolean clickButton(int buttonId) {
		if (!player.getInterfaceManager().hasBankOpen()) {
			return false;
		}

		switch (buttonId) {
		case 82028:
			if (player.getSummoning().hasFamiliar()) {
				if (player.getSummoning().isFamilarBOB()) {
					Item[] e = player.getSummoning().getContainer().getItems();

					for (int i = 0; i < e.length; i++) {
						if (e[i] != null) {
							int deposited = depositFromNoting(e[i].getId(), e[i].getAmount(), -1, false);

							if (deposited != 0) {
								if (e[i].getAmount() == deposited)
									player.getSummoning().getContainer().getItems()[i] = null;
								else if (e[i].getAmount() > deposited) {
									e[i].remove(deposited);
								}
							}
						}
					}
					player.getSummoning().getContainer().shift();
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Your familiar does not carry items."));
				}
			} else
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));

			return true;
		case 82020:
			Item[] k = player.getInventory().getItems();
			for (int i = 0; i < k.length; i++) {
				if (k[i] != null) {
					if (!hasSpaceFor(new Item(k[i])))
						break;
					deposit(k[i], i);
				}

			}

			return true;
		case 82024:
			Item[] e = player.getEquipment().getItems();
			for (int i = 0; i < e.length; i++) {
				if (e[i] != null) {
					int deposited = depositFromNoting(e[i].getId(), e[i].getAmount(), -1, false);

					if (deposited != 0) {
						if (e[i].getAmount() == deposited)
							player.getEquipment().getItems()[i] = null;
						else if (e[i].getAmount() > deposited) {
							e[i].remove(deposited);
						}
					}
				}
			}
			update();
			player.getEquipment().onLogin();
			player.setAppearanceUpdateRequired(true);
			return true;
		case 82008:
			rearrangeType = (rearrangeType == RearrangeTypes.INSERT ? RearrangeTypes.SWAP : RearrangeTypes.INSERT);
			return true;
		case 82016:
			withdrawType = (withdrawType == WithdrawTypes.ITEM ? WithdrawTypes.NOTE : WithdrawTypes.ITEM);
			return true;
		}

		return false;
	}

	@Override
	public void update() {
		player.getClient().queueOutgoingPacket(new SendUpdateItems(5064, player.getInventory().getItems()));
		player.getClient().queueOutgoingPacket(new SendUpdateItems(5382, getSubArrayForTab()));
		player.getClient().queueOutgoingPacket(new SendInventory(player.getInventory().getItems()));

		Item[] tops = new Item[8];

		for (int i = 1; i < tops.length + 1; i++) {
			tops[(i - 1)] = getItems()[(i * ONE_TAB_SIZE)];
		}

		player.getClient().queueOutgoingPacket(new SendUpdateItems(BANK_TAB_ITEM_CONFIG, tops));
	}

	public Item[] getSubArrayForTab() {
		Item[] item = new Item[ONE_TAB_SIZE];

		for (int i = ONE_TAB_SIZE * tab; i <= ONE_TAB_SIZE * tab + ONE_TAB_SIZE - 1; i++) {
			item[(i - ONE_TAB_SIZE * tab)] = getItems()[i];
		}

		return item;
	}

	@Override
	public void swap(int to, int from) {
		to += tab * ONE_TAB_SIZE;
		from += tab * ONE_TAB_SIZE;

		if ((getItems()[to] == null) || (getItems()[from] == null)) {
			return;
		}

		if (rearrangeType == RearrangeTypes.SWAP) {
			Item item = getItems()[to];
			getItems()[to] = getItems()[from];
			getItems()[from] = item;
		} else if (rearrangeType == RearrangeTypes.INSERT) {
			if (from == to) {
				update();
				return;
			}
			
			final Item[] itemsCopy = getItems().clone();

			int slot = from;
			final int end = to;
			boolean reverse = from > to;

			while (slot != end) {
				if (reverse) {
					getItems()[slot] = itemsCopy[slot - 1];
					slot--;
				} else {
					getItems()[slot] = itemsCopy[slot + 1];
					slot++;
				}
			}

			getItems()[end] = itemsCopy[from];
		}

		update();
	}

	@Override
	public void onFillContainer() {
		player.getClient().queueOutgoingPacket(new SendMessage("Your bank is now full."));
	}

	@Override
	public void onMaxStack() {
		player.getClient().queueOutgoingPacket(new SendMessage("Your bank won't be able to hold all that!"));
	}

	@Override
	public boolean allowZero(int id) {
		return false;
	}

	@Override
	public void onAdd(Item item) {
	}

	@Override
	public void onRemove(Item item) {
	}

	public static enum RearrangeTypes {
		SWAP, INSERT;
	}

	public static enum WithdrawTypes {
		ITEM, NOTE;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
