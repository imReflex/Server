package org.endeavor.game.content.trading;

import java.io.Serializable;

import org.endeavor.GameSettings;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendInventoryInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class Trade implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3689769887013322643L;
	public static final int TRADE_CONTAINER_SIZE = 28;
	protected final Player player;
	protected TradeStages stage = TradeStages.NONE;
	protected Trade tradingWith = null;
	protected TradeContainer container = new TradeContainer(this);
	protected String lastRequest = null;

	public Trade(Player player) {
		this.player = player;
	}

	public void request(Player requested) {
		if ((requested.isBusy()) || (player.isBusy())) {
			player.getClient().queueOutgoingPacket(new SendMessage("The other player is busy at the moment."));
			return;
		}

		if (!player.getController().canTrade()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't trade right now."));
			return;
		}

		if ((player.getClient().getHostId() == requested.getClient().getHostId()) && (!GameSettings.DEV_MODE)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't trade this player."));
			return;
		}

		if ((requested.getTrade().trading())
				|| (player.getController().equals(ControllerManager.ROLLING_DICE_CONTROLLER))) {
			player.getClient().queueOutgoingPacket(new SendMessage("The other player is busy at the moment."));
			return;
		}

		player.getClient().queueOutgoingPacket(new SendMessage("Sending " + getRequestString() + " offer.."));
		lastRequest = requested.getUsername();

		if (requested.getTrade().requested(player))
			begin(requested.getTrade());
		else if (!requested.getPrivateMessaging().ignored(player.getUsername()))
			requested.getClient().queueOutgoingPacket(
					new SendMessage(NameUtil.uppercaseFirstLetter(player.getUsername()) + ":" + getRequestString()
							+ "req:"));
	}

	public void begin(Trade tradingWith) {
		player.getClient().queueOutgoingPacket(
				new SendString(getStatus() + " with: "
						+ NameUtil.uppercaseFirstLetter(tradingWith.getPlayer().getUsername()), 3417));
		tradingWith
				.getPlayer()
				.getClient()
				.queueOutgoingPacket(
						new SendString(getStatus() + " with: " + NameUtil.uppercaseFirstLetter(player.getUsername()),
								3417));

		player.getClient().queueOutgoingPacket(new SendString("", 3431));
		tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendString("", 3431));

		player.getClient().queueOutgoingPacket(
				new SendString("Are you sure you want to make this " + getAction() + "?", 3535));
		tradingWith.getPlayer().getClient()
				.queueOutgoingPacket(new SendString("Are you sure you want to make this " + getAction() + "?", 3535));

		player.getClient().queueOutgoingPacket(new SendInventoryInterface(3323, 3321));
		tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendInventoryInterface(3323, 3321));

		reset();
		tradingWith.reset();

		this.tradingWith = tradingWith;
		tradingWith.setTradingWith(this);

		stage = TradeStages.STAGE_1;
		tradingWith.setStage(TradeStages.STAGE_1);

		container.update();
		tradingWith.container.update();
	}

	public void accept() {
		if (stage == TradeStages.STAGE_1) {
			if (!player.getInventory().hasSpaceFor(tradingWith.getTradedItems())) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough inventory space to make this " + getAction() + "."));
				return;
			}
			player.getClient().queueOutgoingPacket(new SendString("Waiting for other player...", 3431));
			tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendString("Other player has accepted.", 3431));
			stage = TradeStages.STAGE_1_ACCEPTED;
		} else if (stage == TradeStages.STAGE_2) {
			stage = TradeStages.STAGE_2_ACCEPTED;
			player.getClient().queueOutgoingPacket(new SendString("Waiting for other player...", 3535));
			tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendString("Other player has accepted.", 3535));
		}

		if ((tradingWith != null) && (tradingWith.accepted()))
			switch (stage) {
			case STAGE_1_ACCEPTED:
				stage = TradeStages.STAGE_2;
				tradingWith.setStage(TradeStages.STAGE_2);

				sendItemText(player);
				sendItemText(tradingWith.getPlayer());

				container.update();
				player.getClient().queueOutgoingPacket(new SendInventoryInterface(3443, 3213));
				tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendInventoryInterface(3443, 3213));
				break;
			case STAGE_2_ACCEPTED:
				end(true);

				stage = TradeStages.NONE;
				tradingWith.setStage(TradeStages.NONE);

				tradingWith.reset();
				reset();
				break;
			}
	}

	public void end(boolean success) {
		Item[] traded = getTradedItems();
		Item[] recieving = tradingWith.getTradedItems();

		for (int i = 0; i < 28; i++) {
			if (success) {
				if (traded[i] != null) {
					tradingWith.getPlayer().getInventory().insert(traded[i]);
				}

				if (recieving[i] != null)
					player.getInventory().insert(recieving[i]);
			} else {
				if (traded[i] != null) {
					player.getInventory().insert(traded[i]);
				}

				if (recieving[i] != null) {
					tradingWith.getPlayer().getInventory().insert(recieving[i]);
				}
			}
		}

		if (success) {
			player.getClient().queueOutgoingPacket(new SendMessage("Accepted."));
			tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendMessage("Accepted."));
		} else {
			player.getClient().queueOutgoingPacket(new SendMessage("You decline the " + getAction() + "."));
			tradingWith.getPlayer().getClient()
					.queueOutgoingPacket(new SendMessage("The other player declined the " + getAction() + "."));
		}

		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		tradingWith.getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());

		player.getInventory().update();
		tradingWith.getPlayer().getInventory().update();

		stage = TradeStages.NONE;
		tradingWith.setStage(TradeStages.NONE);
	}

	public boolean clickTradeButton(int buttonId) {
		switch (buttonId) {
		case 13218:
			accept();
			return true;
		case 13092:
			accept();
			return true;
		}

		return false;
	}

	public void reset() {
		container = new TradeContainer(this);
		stage = TradeStages.NONE;
		tradingWith = null;
		lastRequest = null;
	}

	public static void sendItemText(Player player) {
		Item[] traded = player.getTrade().getTradedItems();
		Item[] recieving = player.getTrade().getTradingWith().getTradedItems();

		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			Item item = traded[i];
			String prefix = "";
			if (item != null) {
				empty = false;
				if ((item.getAmount() >= 1000) && (item.getAmount() < 1000000))
					prefix = "@cya@" + item.getAmount() / 1000 + "K @whi@(" + item.getAmount() + ")";
				else if (item.getAmount() >= 1000000)
					prefix = "@gre@" + item.getAmount() / 1000000 + " million @whi@(" + item.getAmount() + ")";
				else {
					prefix = "" + item.getAmount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		player.getClient().queueOutgoingPacket(new SendString(trade.toString(), 3557));
		trade = new StringBuilder();
		empty = true;

		for (int i = 0; i < player.getInventory().getSize(); i++) {
			Item item = recieving[i];
			String prefix = "";
			if (item != null) {
				empty = false;
				if ((item.getAmount() >= 1000) && (item.getAmount() < 1000000))
					prefix = "@cya@" + item.getAmount() / 1000 + "K @whi@(" + item.getAmount() + ")";
				else if (item.getAmount() >= 1000000)
					prefix = "@gre@" + item.getAmount() / 1000000 + " million @whi@(" + item.getAmount() + ")";
				else {
					prefix = "" + item.getAmount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		player.getClient().queueOutgoingPacket(new SendString(trade.toString(), 3558));
	}

	public static int getTradedWealth(Player Player) {
		int value = 0;
		for (Item item : Player.getTrade().getTradedItems()) {
			if (item != null)
				value += GameDefinitionLoader.getHighAlchemyValue(item.getId()) * item.getAmount();
		}
		return value;
	}

	public static String getTotalAmount(int amount) {
		if ((amount >= 10000) && (amount < 10000000))
			return amount / 1000 + "K";
		if ((amount >= 10000000) && (amount <= 2147483647)) {
			return amount / 1000000 + "M";
		}
		return amount + " gp";
	}

	public boolean canAppendTrade() {
		return (stage == TradeStages.STAGE_1) || (stage == TradeStages.STAGE_1_ACCEPTED);
	}

	public String getRequestString() {
		return "trade";
	}

	public String getStatus() {
		return "Trading";
	}

	public String getAction() {
		return "trade";
	}

	public boolean trading() {
		return stage != TradeStages.NONE;
	}

	public boolean accepted() {
		return (stage == TradeStages.STAGE_1_ACCEPTED) || (stage == TradeStages.STAGE_2_ACCEPTED);
	}

	public boolean requested(Player other) {
		if (lastRequest == null) {
			return false;
		}

		return lastRequest.equalsIgnoreCase(other.getUsername());
	}

	public void setTradingWith(Trade tradingWith) {
		this.tradingWith = tradingWith;
	}

	public TradeStages getStage() {
		return stage;
	}

	public void setStage(TradeStages stage) {
		this.stage = stage;
	}

	public Player getPlayer() {
		return player;
	}

	public Item[] getTradedItems() {
		return container.getItems();
	}

	public TradeContainer getContainer() {
		return container;
	}

	public Trade getTradingWith() {
		return tradingWith;
	}

	public static enum TradeStages {
		NONE, STAGE_1, STAGE_1_ACCEPTED, STAGE_2, STAGE_2_ACCEPTED;
	}
}
