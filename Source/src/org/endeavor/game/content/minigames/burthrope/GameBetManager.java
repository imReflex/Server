package org.endeavor.game.content.minigames.burthrope;

import java.io.Serializable;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.minigames.burthrope.dice.RollingDice;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendEnterXInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class GameBetManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8647802092881593446L;
	public static final int BET_INTERFACE = 15500;
	public static final int MAIN_LINE_TEXT_ID = 15511;
	public static final int PLAYER_1_AMOUNT = 15516;
	public static final int PLAYER_2_AMOUNT = 15517;
	public static final int PLAYER_1_NAME_AND_STATUS_TEXT = 15509;
	public static final int PLAYER_2_NAME_AND_STATUS_TEXT = 15510;
	public static final int ACCEPT_BUTTON = 60147;
	public static final int DECLINE_BUTTON = 60148;
	public static final int INCREASE_BUTTON = 60161;
	public static final int DECREASE_BUTTON = 60160;
	private final Player player;
	private Player request = null;
	private GameTypes requestType = GameTypes.DICING;

	private GameBetManager opponent = null;

	private BetStatuses status = BetStatuses.NONE;
	private GameTypes type = GameTypes.DICING;

	private int offered = 0;
	private boolean increase = true;

	public GameBetManager(Player player) {
		this.player = player;
	}

	public void request(Player r, GameTypes type) {
	}

	public void start(GameTypes type, Player other) {
		status = BetStatuses.BETTING;
		this.type = type;
		opponent = other.getMinigames().getBetManager();
		update();
		player.getClient().queueOutgoingPacket(new SendString(getGameTypeToString(type), 15511));
		player.getClient().queueOutgoingPacket(new SendInterface(15500));
	}

	public void update() {
		player.getClient().queueOutgoingPacket(new SendString(getStatusBar(this), 15509));
		player.getClient().queueOutgoingPacket(new SendString(getStatusBar(opponent), 15510));

		player.getClient().queueOutgoingPacket(new SendString(Misc.formatCoins(offered), 15516));
		player.getClient().queueOutgoingPacket(new SendString(Misc.formatCoins(opponent.getOffered()), 15517));
	}

	public void changeOffer(int change) {
		offer(this.offered += change);
	}

	public void offer(int amount) {
		if (opponent == null) {
			return;
		}

		if (amount < 0) {
			amount = 0;
		}

		offered = amount;
		opponent.setStatus(BetStatuses.BETTING);
		setStatus(BetStatuses.BETTING);
		opponent.update();
		update();
	}

	public void end(boolean success) {
		if (success) {
			player.getClient().queueOutgoingPacket(new SendMessage("Accepted."));
			opponent.getPlayer().getClient().queueOutgoingPacket(new SendMessage("Accepted."));
			switch (type) {
			case DICING:
				RollingDice.play(player, opponent.getPlayer(), offered, opponent.getOffered());
				break;
			}

			opponent.reset();
			reset();
		} else {
			player.getClient().queueOutgoingPacket(new SendMessage("You declined the bet."));
			if (opponent != null) {
				opponent.getPlayer().getClient()
						.queueOutgoingPacket(new SendMessage("The other player declined the bet."));
			}

			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

			if (opponent != null) {
				opponent.getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
				opponent.reset();
			}

			reset();
		}
	}

	public void reset() {
		opponent = null;
		offered = 0;
		request = null;
		status = BetStatuses.NONE;
		requestType = GameTypes.DICING;
		type = GameTypes.DICING;
	}

	public boolean clickButton(int id) {
		switch (id) {
		case 60147:
			if (!player.getInventory().hasItemAmount(new Item(995, offered))) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough coins to bet that much."));
				return true;
			}

			if (opponent == null) {
				return true;
			}

			if (!player.getInventory().hasSpaceFor(
					new Item[] { new Item(995, offered), new Item(995, opponent.getOffered()) })) {
				player.getClient().queueOutgoingPacket(new SendMessage("You need more inventory space to bet that."));
				return true;
			}

			if (player.getMinigames().getBetManager().getOffered() != opponent.getOffered()) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You must offer the same amount as the other player."));
				return true;
			}

			status = BetStatuses.ACCEPTED;
			update();
			opponent.update();

			if (opponent.getStatus() == status) {
				end(true);
			}
			return true;
		case 60148:
			end(false);
			return true;
		case 60161:
			increase = true;
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(15500, -1));
			return true;
		case 60160:
			increase = false;
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(15500, -1));
			return true;
		}
		return false;
	}

	public boolean betting() {
		return opponent != null;
	}

	public boolean requested(Player r, GameTypes type) {
		if (request == null) {
			return false;
		}
		return (request.equals(r)) && (requestType.equals(type));
	}

	public String getStatusBar(GameBetManager p) {
		return p.getPlayer().getUsername() + (p.getStatus() == BetStatuses.ACCEPTED ? " (Accepted)" : "");
	}

	public BetStatuses getStatus() {
		return status;
	}

	public void setStatus(BetStatuses status) {
		this.status = status;
	}

	public Player getPlayer() {
		return player;
	}

	public int getOffered() {
		return offered;
	}

	public boolean increase() {
		return increase;
	}

	public static String getGameTypeToString(GameTypes type) {
		switch (type) {
		case DICING:
			return "Dicing";
		}
		return "null";
	}

	public static String getGameTypeAction(GameTypes type) {
		switch (type) {
		case DICING:
			return "dice";
		}
		return "null";
	}

	public static enum BetStatuses {
		NONE, BETTING, ACCEPTED;
	}

	public static enum GameTypes {
		DICING;
	}
}
