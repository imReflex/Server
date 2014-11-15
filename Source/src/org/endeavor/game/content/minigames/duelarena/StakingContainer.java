package org.endeavor.game.content.minigames.duelarena;

import org.endeavor.GameSettings;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.ItemContainer;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

public class StakingContainer extends ItemContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4202163776150283343L;
	private final Player player;

	public StakingContainer(Player p) {
		super(28, ItemContainer.ContainerTypes.STACK, true, true);
		player = p;
	}

	public void offer(int id, int amount, int slot) {
		if ((PlayerConstants.isOwner(player)) && (!GameSettings.DEV_MODE)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot stake."));
			return;
		}

		if (!player.getDueling().canAppendStake()) {
			return;
		}

		if (!Item.getDefinition(id).isTradable()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot stake that item."));
			return;
		}

		int invAmount = player.getInventory().getItemAmount(id);

		if (invAmount == 0)
			return;
		if (invAmount < amount) {
			amount = invAmount;
		}

		int removed = player.getInventory().remove(new Item(id, amount));

		if (removed > 0) {
			add(id, removed);

			int gpAm = getItemAmount(995);

			if (gpAm > 50000000) {
				player.getClient().queueOutgoingPacket(new SendMessage("You can only stake up to 50 million coins."));
				withdraw(getItemSlot(995), -(50000000 - gpAm));
			}
		} else {
			return;
		}

		player.getDueling().onStake();
		player.getDueling().getInteracting().getDueling().onStake();

		update();
	}

	public void withdraw(int slot, int amount) {
		if ((get(slot) == null) || (!player.getDueling().canAppendStake())) {
			return;
		}

		int id = get(slot).getId();

		int removed = remove(id, amount);

		if (removed > 0)
			player.getInventory().add(new Item(id, removed));
		else {
			return;
		}

		player.getDueling().onStake();
		player.getDueling().getInteracting().getDueling().onStake();

		update();
	}

	@Override
	public void update() {
		player.getClient().queueOutgoingPacket(new SendUpdateItems(6669, getItems()));
		player.getClient().queueOutgoingPacket(new SendUpdateItems(3322, player.getInventory().getItems()));
		if (player.getDueling().getInteracting() != null)
			player.getDueling().getInteracting().getClient().queueOutgoingPacket(new SendUpdateItems(6670, getItems()));
	}

	@Override
	public void onFillContainer() {
	}

	@Override
	public void onMaxStack() {
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
}
