package org.endeavor.game.entity.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemContainer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7887270550044566152L;
	private final int size;
	private final ContainerTypes type;
	private Item[] items;
	private final boolean shift;
	private final boolean acceptNote;

	public abstract void update();

	public abstract void onFillContainer();

	public abstract void onMaxStack();

	public abstract boolean allowZero(int paramInt);

	public abstract void onAdd(Item paramItem);

	public abstract void onRemove(Item paramItem);

	public ItemContainer(int size, ContainerTypes type, boolean shift, boolean acceptNote) {
		this.size = size;
		this.type = type;
		this.shift = shift;
		this.acceptNote = acceptNote;
		items = new Item[size];
	}

	public Item get(int slot) {
		if (slot < 0 || slot >= items.length) {
			return null;
		}
		
		return items[slot];
	}

	public int add(int id, int amount) {
		return add(new Item(id, amount), true);
	}

	public int add(int id, int amount, boolean update) {
		return add(new Item(id, amount), update);
	}

	public int add(Item item) {
		return add(item, true);
	}

	public void add(Item[] items, boolean update) {
		for (Item i : items) {
			add(i, false);
		}

		if (update)
			update();
	}

	public int insert(int id, int amount) {
		return insert(new Item(id, amount));
	}

	public int insert(Item i) {
		return add(i, false);
	}

	public int add(Item item, boolean update) {
		if (item.getDefinition() == null) {
			System.out.println("Null ItemDefinition for " + item.getId());
			return 0;
		}

		if ((item.getDefinition().isNote()) && (!acceptNote)) {
			item.unNote();
		}

		int id = item.getId();
		boolean stackable = isStackable(id);

		int empty = -1;
		int amount = item.getAmount();
		int am = 0;
		boolean added = false;

		int free = getFreeSlots();

		if (amount <= 0) {
			return 0;
		}

		if ((free == 0) && (!stackable)) {
			onFillContainer();
			return 0;
		}

		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				if (stackable) {
					if (empty == -1) {
						empty = i;
					}
					if (shift)
						break;
				} else {
					items[i] = new Item(id, 1);
					amount--;
					am++;

					if (amount == 0) {
						break;
					}
					free--;
					if (free == 0) {
						onFillContainer();
						break;
					}
				}
			} else if ((type != ContainerTypes.NEVER_STACK) && (stackable) && (items[i].getId() == id)) {
				if ((items[i].getAmount() <= 0) && (!allowZero(items[i].getId()))) {
					Exception e = new Exception("ZERO AMOUNT ON ITEM");
					e.printStackTrace();
					return am;
				}

				long newAmount = ((long) items[i].getAmount()) + ((long) amount);

				if (newAmount > Integer.MAX_VALUE) {
					Exception e = new Exception("MAX STACK");
					e.printStackTrace();
					am += Integer.MAX_VALUE - items[i].getAmount();
					items[i].setAmount(Integer.MAX_VALUE);
					onMaxStack();
				} else {
					am += amount;
					items[i].setAmount((int) newAmount);
				}

				added = true;
				break;
			}

		}

		if ((empty != -1) && (!added)) {
			items[empty] = new Item(id, amount);
			am += amount;
		}

		if (update) {
			update();
		}

		return am;
	}
	
	public int add(Item item, int startSlot, int endSlot, boolean update) {
		if (item.getDefinition() == null) {
			System.out.println("Null ItemDefinition for " + item.getId());
			return 0;
		}

		if ((item.getDefinition().isNote()) && (!acceptNote)) {
			item.unNote();
		}

		int id = item.getId();
		boolean stackable = isStackable(id);

		int empty = -1;
		int amount = item.getAmount();
		int am = 0;
		boolean added = false;

		int free = getFreeSlots();

		if (amount <= 0) {
			return 0;
		}

		if ((free == 0) && (!stackable)) {
			onFillContainer();
			return 0;
		}

		for (int i = startSlot; i <= endSlot; i++) {
			if (items[i] == null) {
				if (stackable) {
					if (empty == -1) {
						empty = i;
					}
					if (shift)
						break;
				} else {
					items[i] = new Item(id, 1);
					amount--;
					am++;

					if (amount == 0) {
						break;
					}
					free--;
					if (free == 0) {
						onFillContainer();
						break;
					}
				}
			} else if ((type != ContainerTypes.NEVER_STACK) && (stackable) && (items[i].getId() == id)) {
				if ((items[i].getAmount() <= 0) && (!allowZero(items[i].getId()))) {
					Exception e = new Exception("ZERO AMOUNT ON ITEM");
					e.printStackTrace();
					return am;
				}

				long newAmount = ((long) items[i].getAmount()) + ((long) amount);

				if (newAmount > Integer.MAX_VALUE) {
					Exception e = new Exception("MAX STACK");
					e.printStackTrace();
					am += Integer.MAX_VALUE - items[i].getAmount();
					items[i].setAmount(Integer.MAX_VALUE);
					onMaxStack();
				} else {
					am += amount;
					items[i].setAmount((int) newAmount);
				}

				added = true;
				break;
			}

		}

		if ((empty != -1) && (!added)) {
			items[empty] = new Item(id, amount);
			am += amount;
		}

		if (update) {
			update();
		}

		return am;
	}

	public int removeFromSlot(int slot, int expectedId, int amount) {
		if ((items[slot] == null) || (items[slot].getId() != expectedId)) {
			return 0;
		}

		if (items[slot].getAmount() <= amount) {
			int am = items[slot].getAmount();
			items[slot] = null;
			return am;
		}

		items[slot].remove(amount);
		return amount;
	}

	public int remove(int item) {
		return remove(item, 1, true);
	}

	public int remove(Item item) {
		return remove(item, true);
	}

	public int remove(int id, int amount, boolean update) {
		return remove(new Item(id, amount), update);
	}

	public int remove(int id, int amount) {
		return remove(new Item(id, amount), true);
	}

	public void remove(Item[] items, boolean update) {
		for (Item i : items) {
			remove(i, false);
		}

		if (update)
			update();
	}

	public int remove(Item item, boolean update) {
		if (item == null) {
			return 0;
		}

		int id = item.getId();
		boolean stackable = isStackable(id);
		int removed = 0;
		int amount = item.getAmount();

		if (amount <= 0) {
			return 0;
		}

		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				if (shift) {
					break;
				}
			} else if (items[i].getId() == id) {
				if (stackable) {
					
					
					if (items[i].getAmount() <= amount) {
						removed += items[i].getAmount();
						amount -= items[i].getAmount();

						if (!allowZero(id)) {
							onRemove(new Item(id, amount));
							items[i] = null;
							if (shift)
								shift(i);
						} else {
							items[i].setAmount(0);
						}
					} else {
						onRemove(new Item(id, amount));
						removed += amount;
						items[i].remove(amount);
						amount = 0;
					}
				} else {
					onRemove(new Item(id, 1));
					items[i] = null;
					amount--;
					removed++;
					if (amount <= 0) {
						break;
					}
				}
			}
		}

		if ((!stackable) && (shift) && (removed > 0)) {
			shift();
		}

		if (update) {
			update();
		}

		return removed;
	}
	
	public int remove(Item item, int startSlot, int endSlot, boolean update) {
		if (item == null) {
			return 0;
		}

		int id = item.getId();
		boolean stackable = isStackable(id);
		int removed = 0;
		int amount = item.getAmount();

		if (amount <= 0) {
			return 0;
		}

		for (int i = startSlot; i <= endSlot; i++) {
			if (items[i] == null) {
				if (shift) {
					break;
				}
			} else if (items[i].getId() == id) {
				if (stackable) {
					
					
					if (items[i].getAmount() <= amount) {
						removed += items[i].getAmount();
						amount -= items[i].getAmount();

						if (!allowZero(id)) {
							onRemove(new Item(id, amount));
							items[i] = null;
							//if (shift)
								//shift(i);
						} else {
							items[i].setAmount(0);
						}
					} else {
						onRemove(new Item(id, amount));
						removed += amount;
						items[i].remove(amount);
						amount = 0;
					}
				} else {
					onRemove(new Item(id, 1));
					items[i] = null;
					amount--;
					removed++;
					if (amount <= 0) {
						break;
					}
				}
			}
		}

		//if ((!stackable) && (shift) && (removed > 0)) {
			shift(startSlot, endSlot);
		//}

		if (update) {
			update();
		}

		return removed;
	}

	public boolean hasSpaceFor(Item item) {
		boolean stackable = isStackable(item.getId());
		int amount = item.getAmount();

		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				if (stackable) {
					return true;
				}
				amount--;
				if (amount == 0) {
					return true;
				}

			} else if ((items[i].getId() == item.getId()) && (stackable)) {
				long newAmount = ((long) items[i].getAmount()) + ((long) item.getAmount());
				if (newAmount > Integer.MAX_VALUE) {
					return false;
				}
				return true;
			}

		}

		return false;
	}

	public int getItemAmount(int id) {
		int amount = 0;

		boolean stackable = isStackable(id);

		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				if (shift) {
					break;
				}

			} else if (items[i].getId() == id) {
				amount += items[i].getAmount();
				if (stackable) {
					return amount;
				}
			}
		}

		return amount;
	}

	public int hasItems(Item[] delete) {

		for (int i = 0; i < delete.length; i++) {
			if (delete[i] != null) {
				boolean stackable = isStackable(delete[i].getId());

				for (int j = 0; j < items.length; j++) {
					if (items[j] != null) {
						if (items[j].getId() == delete[i].getId()) {
							if (stackable) {
								if (items[j].getAmount() >= delete[i].getAmount()) {
									delete[i] = null;
									break;
								}
								return delete[i].getId();
							}

							delete[i].remove(1);
							if (delete[i].getAmount() <= 0) {
								delete[i] = null;
								break;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < delete.length; i++) {
			if (delete[i] != null) {
				return delete[i].getId();
			}
		}

		return -1;
	}

	public boolean hasSpaceOnRemove(Item remove, Item add) {
		Item[] itemsCopy = new Item[items.length];

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				itemsCopy[i] = new Item(items[i]);
			}
		}
		remove(remove, false);

		if (hasSpaceFor(add)) {
			items = itemsCopy;
			return true;
		}

		items = itemsCopy;
		return false;
	}

	public boolean hasSpaceFor(Item[] check) {
		Item[] copy = items.clone();
		int count = check.length;

		for (int i = 0; i < check.length; i++) {
			if (check[i] == null) {
				count--;
			} else {
				int id = check[i].getId();
				boolean stackable = isStackable(id);

				int first = -1;

				for (int j = 0; j < copy.length; j++) {
					if (copy[j] == null) {
						if (!stackable) {
							if (check[i].getAmount() == 1) {
								count--;
								copy[j] = check[i];
							} else {
								copy[j] = check[i];
								check[i].setAmount(check[i].getAmount() - 1);
								if (check[i].getAmount() <= 1) {
									count--;
									break;
								}
							}
						} else if (first == -1) {
							first = i;
						}

					} else if ((stackable) && (copy[j].getId() == id)) {
						long newAmount = ((long) copy[j].getAmount()) + ((long) check[i].getAmount());
						if (newAmount > Integer.MAX_VALUE) {
							return false;
						}
						count--;
						break;
					}

				}

				if (first != -1) {
					copy[first] = check[i];
					count--;
				}
			}
		}
		return count <= 0;
	}

	public boolean hasSpaceFor(Object[] check) {
		Item[] copy = new Item[items.length];
		System.arraycopy(items, 0, copy, 0, items.length);

		int count = check.length;

		for (int i = 0; i < check.length; i++) {
			if (check[i] == null) {
				count--;
			} else {
				Item item = (Item) check[i];

				int id = item.getId();
				boolean stackable = isStackable(id);

				int first = -1;

				for (int j = 0; j < copy.length; j++) {
					if (copy[j] == null) {
						if (!stackable) {
							if (item.getAmount() == 1) {
								count--;
								copy[j] = item;
							} else {
								copy[j] = item;
								item.setAmount(item.getAmount() - 1);
								if (item.getAmount() <= 1) {
									count--;
									break;
								}
							}
						} else if (first == -1) {
							first = i;
						}

					} else if ((stackable) && (copy[j].getId() == id)) {
						long newAmount = ((long) copy[j].getAmount()) + ((long) item.getAmount());
						if (newAmount > Integer.MAX_VALUE) {
							return false;
						}
						copy[j].setAmount((int) newAmount);
						count--;
						break;
					}

				}

				if (first != -1) {
					copy[first] = item;
					count--;
				}
			}
		}
		return count <= 0;
	}

	public void shift() {
		List<Item> all = new ArrayList<Item>();

		for (int i = 0; i < size; i++) {
			if (items[i] != null) {
				all.add(items[i]);
			}
		}

		items = new Item[size];

		int index = 0;
		for (Item i : all) {
			items[index] = i;
			index++;
		}
	}

	public void shift(int start, int end) {
		List<Item> all = new ArrayList<Item>();

		for (int i = start; i <= end; i++) {
			if (items[i] != null) {
				all.add(items[i]);
				items[i] = null;
			}

		}

		int index = start;
		for (Item i : all) {
			items[index] = i;
			index++;
		}
	}

	public void shift(int slot) {
		if ((slot > size) || (slot < 0)) {
			return;
		}

		for (int i = slot + 1; i < size; i++) {
			if (items[i] == null) {
				break;
			}
			items[(i - 1)] = items[i];
			items[i] = null;
		}
	}

	public boolean hasItemId(int id) {
		return hasItemAmount(new Item(id, 1));
	}

	public boolean hasItemAmount(int id, int amount) {
		return hasItemAmount(new Item(id, amount));
	}

	public boolean hasItemAmount(Item item) {
		boolean stackable = isStackable(item.getId());

		item = new Item(item);
		
		for (Item i : items) {
			if (i != null && i.getId() == item.getId()) {
				if (stackable) {
					return i.getAmount() >= item.getAmount();
				}
				
				item.remove(1);

				if (item.getAmount() <= 0) {
					return true;
				}

			}

		}

		return false;
	}
	
	/**
	 * Checks if the player has an item with the specified amount
	 * 
	 * @param item
	 *            The item to check for
	 * @return The player has the item
	 */
	public boolean playerHasItem(Item item) {
		for (Item i : items) {
			if (i == null)
				continue;
			if (i.getId() == item.getId()) {
				if (i.getAmount() >= item.getAmount()) {
					return true;
				}
			}
		}
		return false;
	}

	public void clear() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				onRemove(items[i]);
			}
		}

		items = new Item[size];
		update();
	}

	public int getFreeSlots() {
		int am = 0;
		for (int i = 0; i < size; i++) {
			if (items[i] == null)
				am++;
		}
		return am;
	}

	public int getFreeSlots(int start, int end) {
		int am = 0;
		for (int i = start; i <= end; i++) {
			if (items[i] == null)
				am++;
		}
		return am;
	}

	public int getItemSlot(int id) {
		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				if (shift) {
					break;
				}

			} else if (items[i].getId() == id) {
				return i;
			}
		}

		return -1;
	}

	public int getSlotId(int slot) {
		if ((slot > size) || (slot < 0)) {
			return 0;
		}

		if (items[slot] == null) {
			return 0;
		}

		return items[slot].getId();
	}

	public int getSlotAmount(int slot) {
		if ((slot > size) || (slot < 0)) {
			return 0;
		}

		if (items[slot] == null) {
			return 0;
		}

		return items[slot].getAmount();
	}

	public void setId(int slot, int id) {
		if ((slot > size) || (slot < 0)) {
			return;
		}

		if (items[slot] == null) {
			return;
		}

		onRemove(items[slot]);

		items[slot].setId(id);

		onAdd(items[slot]);
		update();
	}

	public void clear(int slot) {
		if ((slot > size) || (slot < 0)) {
			return;
		}

		if (items[slot] != null) {
			onRemove(items[slot]);
		}

		items[slot] = null;
		update();
	}

	public void swap(int to, int from) {
		if ((to > size) || (to < 0) || (from > size) || (from < 0)) {
			return;
		}

		if (((items[to] == null) || (items[from] == null)) && (shift)) {
			return;
		}

		Item item = items[to];
		items[to] = items[from];
		items[from] = item;

		update();
	}

	public void setSlotOnLogin(int slot, Item i) {
		if ((slot > size) || (slot < 0)) {
			return;
		}

		onAdd(items[slot]);

		items[slot] = i;
	}

	public boolean isStackable(int id) {
		return (type == ContainerTypes.ALWAYS_STACK)
				|| ((Item.getDefinition(id) != null) && (Item.getDefinition(id).isStackable()) && (type == ContainerTypes.STACK));
	}

	public boolean slotHasItem(int slot) {
		if ((slot > size) || (slot < 0)) {
			return false;
		}

		return items[slot] != null;
	}

	public boolean slotContainsItem(int slot, int id) {
		if ((slot > size) || (slot < 0)) {
			return false;
		}

		if (items[slot] == null) {
			return false;
		}

		if (items[slot].getId() != id) {
			return false;
		}

		return true;
	}

	public void setSlot(Item item, int slot) {
		if (item == null) {
			if (items[slot] != null)
				onRemove(items[slot]);
		} else {
			if (items[slot] != null) {
				onRemove(items[slot]);
			}

			onAdd(item);
		}

		items[slot] = item;
	}

	public int getSize() {
		return size;
	}

	public ContainerTypes getType() {
		return type;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}

	public boolean isShift() {
		return shift;
	}

	public Item[] getItems() {
		return items;
	}

	public static enum ContainerTypes {
		NEVER_STACK,

		STACK,

		ALWAYS_STACK;
	}
}
