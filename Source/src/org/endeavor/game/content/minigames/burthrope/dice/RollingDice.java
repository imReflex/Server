package org.endeavor.game.content.minigames.burthrope.dice;

import java.security.SecureRandom;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendModelAnimation;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class RollingDice {
	private static final SecureRandom random = new SecureRandom();
	public static final int ROLL_DICE_INTERFACE = 6675;
	public static final int CASH_STACK_1_CONFIG_ID = 261;
	public static final int CASH_STACK_1_TEXT_ID = 8424;
	public static final int CASH_STACK_2_CONFIG_ID = 262;
	public static final int CASH_STACK_2_TEXT_ID = 8425;
	public static final int MAIN_LINE_TEXT_ID = 8426;
	public static final int PLAYER_1_TEXT_ID = 7815;
	public static final int PLAYER_2_TEXT_ID = 8399;
	public static final int PLAYER_1_DIE_1_MODEL_ID = 6719;
	public static final int PLAYER_1_DIE_2_MODEL_ID = 6695;
	public static final int PLAYER_2_DIE_1_MODEL_ID = 7814;
	public static final int PLAYER_2_DIE_2_MODEL_ID = 7813;
	public static final int ROLLING_ANIMATION = 1051;
	public static final int STOP_ROLL_ANIMATION = 65535;
	public static final int DEFAULT_ROLL_TIMER = 3;

	public static void sendRollingInterface(Player p1, Player p2, int bet1, int bet2) {
		p1.getClient().queueOutgoingPacket(new SendConfig(261, getCashStackConfig(bet1)));
		p1.getClient().queueOutgoingPacket(new SendConfig(262, getCashStackConfig(bet2)));

		p1.getClient().queueOutgoingPacket(new SendString(Misc.format(bet1), 8424));
		p1.getClient().queueOutgoingPacket(new SendString(Misc.format(bet2), 8425));

		p1.getClient().queueOutgoingPacket(new SendString(p1.getUsername(), 7815));
		p1.getClient().queueOutgoingPacket(new SendString(p2.getUsername(), 8399));

		p1.getClient().queueOutgoingPacket(new SendInterface(6675));
	}

	public static void rollDice(Player roll, Player observe) {
		roll.getClient().queueOutgoingPacket(new SendModelAnimation(6719, 1051));
		roll.getClient().queueOutgoingPacket(new SendModelAnimation(6695, 1051));
		observe.getClient().queueOutgoingPacket(new SendModelAnimation(7814, 1051));
		observe.getClient().queueOutgoingPacket(new SendModelAnimation(7813, 1051));
	}

	public static int finishRoll(Player roll, Player observe) {
		int outcome = 2 + random.nextInt(11);

		roll.getUpdateFlags().sendForceMessage(
				roll.getUsername() + " has rolled " + outcome + " versus " + observe.getUsername() + ".");

		roll.getClient().queueOutgoingPacket(new SendModelAnimation(6719, 65535));
		roll.getClient().queueOutgoingPacket(new SendModelAnimation(6695, 65535));

		observe.getClient().queueOutgoingPacket(new SendModelAnimation(7814, 65535));
		observe.getClient().queueOutgoingPacket(new SendModelAnimation(7813, 65535));

		roll.getClient().queueOutgoingPacket(new SendString(roll.getUsername() + " rolled " + outcome, 8426));
		observe.getClient().queueOutgoingPacket(new SendString(roll.getUsername() + " rolled " + outcome, 8426));

		return outcome;
	}

	public static void onForceLogout(Player p) {
		p.getInventory().add(995, p.getMinigames().getBetManager().getOffered());
		p.getMinigames().getBetManager().reset();
	}

	public static void play(final Player p1, final Player p2, final int bet1, final int bet2) {
		if (!p1.setController(ControllerManager.ROLLING_DICE_CONTROLLER)) {
			p1.getController().throwException(p1, "TRANSITION CONTROLLER");
			sendFailure(p1);
			sendFailure(p2);
			return;
		}
		if (!p2.setController(ControllerManager.ROLLING_DICE_CONTROLLER)) {
			p2.getController().throwException(p2, "TRANSITION CONTROLLER");
			sendFailure(p1);
			sendFailure(p2);
			return;
		}

		p1.getInventory().remove(new Item(995, bet1));
		p2.getInventory().remove(new Item(995, bet2));
		p1.getInventory().update();
		p2.getInventory().update();

		sendRollingInterface(p1, p2, bet1, bet2);
		sendRollingInterface(p2, p1, bet2, bet1);

		p1.getClient().queueOutgoingPacket(new SendString("", 8426));
		p2.getClient().queueOutgoingPacket(new SendString("", 8426));

		Task roll1 = getRollTask(p1, p2, null);
		final Task roll2 = getRollTask(p2, p1, roll1);

		TaskQueue.queue(roll1);
		TaskQueue.queue(roll2);
		TaskQueue.queue(new Task(1) {
			@Override
			public void execute() {
				if (roll2.stopped()) {
					RollingDice.finishGame(p1, p2, bet1, bet2);
					RollingDice.finishGame(p2, p1, bet2, bet1);
					stop();
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public static void finishGame(Player p, Player p2, int bet1, int bet2) {
		p.onControllerFinish();
		p2.onControllerFinish();
		int roll1 = p.getMinigames().getLastDiceRoll();
		int roll2 = p2.getMinigames().getLastDiceRoll();

		if (roll1 > roll2) {
			p.getClient()
					.queueOutgoingPacket(
							new SendMessage("You have won " + Misc.formatCoins(bet2) + " coins from "
									+ p2.getUsername() + "."));
			int goldAmount = p.getInventory().getItemAmount(995);
			int winnings = bet1 + bet2;
			if (goldAmount + winnings > 2147483647) {
				winnings = 2147483647 - goldAmount;
			}
			p.getInventory().add(new Item(995, winnings));
		} else if (roll1 < roll2) {
			p.getClient().queueOutgoingPacket(
					new SendMessage("You have lost " + Misc.formatCoins(bet1) + " coins to " + p2.getUsername() + "."));
		} else if (roll1 == roll2) {
			p.getClient().queueOutgoingPacket(new SendMessage("You have tied with " + p2.getUsername() + "."));
			p.getInventory().add(new Item(995, bet1));
		}

		p.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
	}

	public static final Task getRollTask(final Player roll, final Player observe, final Task wait) {
		return new Task(2) {
			int time = 3;

			@Override
			public void execute() {
				if ((wait != null) && (!wait.stopped())) {
					return;
				}

				if (time > 0) {
					roll.getClient().queueOutgoingPacket(
							new SendString(roll.getUsername() + " - rolling in.. " + time, 8426));
					observe.getClient().queueOutgoingPacket(
							new SendString(roll.getUsername() + " - rolling in.. " + time, 8426));
				} else if (time == 0) {
					roll.getClient().queueOutgoingPacket(new SendString(roll.getUsername() + " - rolling..", 8426));
					observe.getClient().queueOutgoingPacket(new SendString(roll.getUsername() + " - rolling..", 8426));
					RollingDice.rollDice(roll, observe);
				} else if (time == -2) {
					int outcome = RollingDice.finishRoll(roll, observe);
					roll.getMinigames().setLastDiceRoll(outcome);

					roll.getClient().queueOutgoingPacket(new SendMessage("You rolled " + outcome + "."));
					observe.getClient().queueOutgoingPacket(
							new SendMessage(roll.getUsername() + " rolled " + outcome + "."));

					roll.getClient().queueOutgoingPacket(new SendString("You rolled " + outcome, 8426));
					observe.getClient().queueOutgoingPacket(
							new SendString(roll.getUsername() + " rolled " + outcome, 8426));
				} else if (time == -4) {
					stop();
				}

				time -= 1;
			}

			@Override
			public void onStop() {
			}
		};
	}

	public static void sendFailure(Player p) {
		p.getClient().queueOutgoingPacket(
				new SendMessage("Unable to start Rolling Dice minigame. Contact your administrator."));
		p.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
	}

	public static int getCashStackConfig(int amount) {
		if (amount > 1500) {
			return 10;
		}

		switch (amount) {
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 4;
		case 5:
			return 5;
		}

		if ((amount > 5) && (amount <= 10))
			return 6;
		if ((amount > 10) && (amount <= 15))
			return 7;
		if ((amount > 15) && (amount <= 30))
			return 8;
		if ((amount > 30) && (amount <= 100))
			return 8;
		if ((amount > 100) && (amount <= 1500)) {
			return 9;
		}

		return 1;
	}
}
