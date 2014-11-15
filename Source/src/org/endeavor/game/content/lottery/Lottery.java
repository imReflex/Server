package org.endeavor.game.content.lottery;

import java.util.ArrayList;
import java.util.List;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.io.PlayerSaveUtil;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Lottery {
	private static final List<Player> entries = new ArrayList<Player>();
	private static Mob mob;
	private static byte position = 0;
	private static final byte DRAW_POS = 60;
	private static boolean message = false;

	public static void enter(Player player) {
		entries.add(player);
	}

	public static boolean canEnter(Player player) {
		return !entries.contains(player);
	}
	
	public static int getPot() {
		return 10 + Lottery.entries.size() * (!GameConstants.IS_DOUBLE_EXP_WEEKEND ? 2 : 3);
	}

	public static void init() {
		mob = new Mob(3001, true, new Location(2604, 3092));

		TaskQueue.queue(new Task(mob, 25, false) {
			@Override
			public void execute() {
				if ((Lottery.position = (byte) (Lottery.position + 1)) == 60) {
					if (Lottery.entries.size() < 4) {
						Lottery.mob.getUpdateFlags().sendForceMessage(
								"There weren't enough entries to draw the lottery!");

						if (Lottery.entries.size() > 0) {
							for (Player player : Lottery.entries) {
								player = World.getPlayerByName(player.getUsername());

								if ((player != null) && (player.isActive())) {
									if (player.getBank().hasSpaceFor(new Item(995, 5000000)))
										player.getBank().add(995, 5000000, false);
									else if (player.getInventory().hasSpaceFor(new Item(995, 5000000))) {
										player.getInventory().add(995, 5000000, true);
									}

									player.getClient().queueOutgoingPacket(
											new SendMessage("Your money from the lottery has been refunded."));
								} else if ((player != null) && (player.getUsername() != null)) {
									PlayerSaveUtil.addToOfflineContainer(player.getUsername(), new Item(995, 5000000));
								}
							}
						}

						Lottery.entries.clear();
						Lottery.position = 0;
						Lottery.message = false;
					} else {
						Player winner = Lottery.entries.get(Misc.randomNumber(Lottery.entries.size()));
						winner = World.getPlayerByName(winner.getUsername());

						Lottery.mob.getUpdateFlags().sendForceMessage(
								winner.getUsername() + " has won the pot: " + (getPot()) + "M!");

						if ((winner != null) && (winner.isActive())) {
							if (winner.getBank()
									.hasSpaceFor(new Item(995, (getPot()) * 1000000)))
								winner.getBank().depositFromNoting(995, (getPot()) * 1000000,
										-1, false);
							else if (winner.getInventory().hasSpaceFor(
									new Item(995, (getPot()) * 1000000)))
								winner.getInventory().add(995, (getPot()) * 1000000, true);
						} else if ((winner != null) && (winner.getUsername() != null)) {
							PlayerSaveUtil.addToOfflineContainer(winner.getUsername(), new Item(995,
									(getPot()) * 1000000));
						}
					}

					Lottery.entries.clear();
					Lottery.position = 0;
					Lottery.message = false;
				} else {
					if ((Lottery.entries.size() > 3) && (!Lottery.message) && ((60 - Lottery.position) * 15 <= 60)) {
						World.sendGlobalMessage("Lottery is drawing in less than one minute!", true);
						Lottery.message = true;
					}

					Lottery.mob.getUpdateFlags().sendForceMessage(
							(60 - Lottery.position) * 15 + " seconds left! The pot is "
									+ (getPot()) + "M! Entries: " + Lottery.entries.size());
				}
			}

			@Override
			public void onStop() {
			}
		});
	}
}
