package org.endeavor.game.content.trading;

import org.endeavor.GameSettings;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.ItemContainer;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

public class TradeContainer extends ItemContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2282139171827162438L;
	private final Trade trade;

	public TradeContainer(Trade trade) {
		super(28, ItemContainer.ContainerTypes.STACK, true, true);
		this.trade = trade;
	}

	public void offer(int id, int amount, int slot) {
		if ((!trade.getPlayer().getController().equals(DungConstants.DUNG_CONTROLLER))
				&& (ItemCheck.isDungeoneeringItem(id))) {
			trade.getPlayer().getInventory().remove(id);
			return;
		}
		
		if (!GameSettings.DEV_MODE && PlayerConstants.isOwner(trade.player)) {
			trade.player.getClient().queueOutgoingPacket(new SendMessage("You cannot trade items."));
			return;
		}

		if (!Item.getDefinition(id).isTradable()) {
			trade.player.getClient().queueOutgoingPacket(new SendMessage("You cannot trade this item."));
			return;
		}

		if (!trade.canAppendTrade()) {
			return;
		}

		if (!trade.player.getInventory().slotContainsItem(slot, id)) {
			return;
		}

		if ((trade.getStage() == Trade.TradeStages.STAGE_2) || (trade.getStage() == Trade.TradeStages.STAGE_2_ACCEPTED)) {
			return;
		}

		int invAmount = trade.player.getInventory().getItemAmount(id);

		if (invAmount < amount) {
			amount = invAmount;
		}

		int added = add(id, amount, true);

		if (added > 0) {
			trade.getPlayer().getInventory().remove(id, added);
		}

		trade.setStage(Trade.TradeStages.STAGE_1);
		trade.tradingWith.setStage(Trade.TradeStages.STAGE_1);

		trade.player.getClient().queueOutgoingPacket(new SendString("", 3431));
		trade.tradingWith.player.getClient().queueOutgoingPacket(new SendString("", 3431));
	}

	public void withdraw(int slot, int amount) {
		if (!trade.canAppendTrade()) {
			return;
		}

		if ((trade.getStage() == Trade.TradeStages.STAGE_2) || (trade.getStage() == Trade.TradeStages.STAGE_2_ACCEPTED)) {
			return;
		}

		if (!slotHasItem(slot)) {
			return;
		}

		int id = getSlotId(slot);
		int tradeAmount = getItemAmount(id);

		if (tradeAmount < amount) {
			amount = tradeAmount;
		}

		int removed = remove(id, amount);
		
		if (removed > 0) {
			trade.player.getInventory().add(id, removed);
		}

		trade.setStage(Trade.TradeStages.STAGE_1);
		trade.tradingWith.setStage(Trade.TradeStages.STAGE_1);

		trade.player.getClient().queueOutgoingPacket(new SendString("", 3431));
		trade.tradingWith.player.getClient().queueOutgoingPacket(new SendString("", 3431));
	}

	@Override
	public void update() {
		if ((trade.stage == Trade.TradeStages.STAGE_2) || (trade.stage == Trade.TradeStages.STAGE_2_ACCEPTED)) {
			trade.player.getClient().queueOutgoingPacket(new SendUpdateItems(3322, null));
			trade.tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendUpdateItems(3322, null));
		} else {
			trade.player.getClient().queueOutgoingPacket(
					new SendUpdateItems(3322, trade.player.getInventory().getItems()));
			trade.tradingWith
					.getPlayer()
					.getClient()
					.queueOutgoingPacket(
							new SendUpdateItems(3322, trade.tradingWith.getPlayer().getInventory().getItems()));
		}

		trade.player.getClient().queueOutgoingPacket(new SendUpdateItems(3415, trade.getTradedItems()));
		trade.player.getClient().queueOutgoingPacket(new SendUpdateItems(3416, trade.tradingWith.getTradedItems()));

		trade.tradingWith.getPlayer().getClient()
				.queueOutgoingPacket(new SendUpdateItems(3415, trade.tradingWith.getTradedItems()));
		trade.tradingWith.getPlayer().getClient()
				.queueOutgoingPacket(new SendUpdateItems(3416, trade.getTradedItems()));
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
