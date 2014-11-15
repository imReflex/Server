package org.endeavor.game.content;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.ItemContainer;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendInventory;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Inventory extends ItemContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5026197399009384314L;
	private transient Player player;

	public Inventory(Player player) {
		super(28, ItemContainer.ContainerTypes.STACK, false, true);
		this.player = player;
	}

	public void addOnLogin(Item item, int slot) {
		if (item == null) {
			return;
		}

		getItems()[slot] = item;
		onAdd(item);
	}

	public void addOrCreateGroundItem(int id, int amount, boolean update) {
		if (player.getInventory().hasSpaceFor(new Item(id, amount))) {
			player.getInventory().insert(id, amount);
		} else if ((amount > 1) && (!Item.getDefinition(id).isStackable())) {
			for (int i = 0; i < amount; i++)
				GroundItemHandler.add(new Item(id, 1), player.getLocation(), player);
		} else {
			GroundItemHandler.add(new Item(id, amount), player.getLocation(), player);
		}

		if (update)
			update();
	}

	@Override
	public void update() {
		for (int i = 0; i < getItems().length; i++) {
			if ((getItems()[i] != null) && (getItems()[i].getAmount() >= 1000000000)
					&& (!PlayerConstants.isOwner(player))) {
				player.getClient().setLogPlayer(true);
				break;
			}
		}

		player.getSummoning().onUpdateInventory();

		player.getClient().queueOutgoingPacket(new SendInventory(getItems()));
	}

	@Override
	public void onFillContainer() {
		player.getClient()
				.queueOutgoingPacket(new SendMessage("You do not have enough inventory space to carry that."));
	}

	@Override
	public void onMaxStack() {
		player.getClient().queueOutgoingPacket(new SendMessage("You won't be able to carry all that!"));
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

	@Override
	public void setItems(Item[] items) {
		super.setItems(items);
		update();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
