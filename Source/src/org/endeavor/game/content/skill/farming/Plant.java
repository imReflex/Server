package org.endeavor.game.content.skill.farming;

import java.io.Serializable;
import java.util.Calendar;

import org.endeavor.GameSettings;
import org.endeavor.engine.task.RunOnceTask;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Plant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1254888063291564125L;
	private final int patch;
	private final int plant;
	private int minute;
	private int hour;
	private int day;
	private int year;
	private byte stage = 0;
	private byte disease = -1;
	private byte watered = 0;

	private boolean dead = false;

	private byte harvested = 0;

	public Plant(int patchId, int plantId) {
		patch = patchId;
		plant = plantId;
	}

	public boolean doDisease() {
		return false;
	}

	public boolean doWater() {
		return false;
	}

	public void water(Player player, int item) {
		if (item == 5332) {
			return;
		}

		if (isWatered()) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your plants have already been watered."));
			return;
		}

		if (item == 5331) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your watering can is empty."));
			return;
		}

		player.getClient().queueOutgoingPacket(new SendMessage("You water the plant."));
		player.getFarming().nextWateringCan(item);
		player.getUpdateFlags().sendAnimation(2293, 0);

		watered = -1;
		doConfig(player);
	}

	public void setTime() {
		minute = Calendar.getInstance().get(12);
		hour = Calendar.getInstance().get(11);
		day = Calendar.getInstance().get(6);
		year = Calendar.getInstance().get(1);
	}

	public void click(Player player, int option) {
		if (option == 1) {
			if (dead)
				player.getClient().queueOutgoingPacket(new SendMessage("Oh dear, your plants have died!"));
			else if (isDiseased())
				player.getClient().queueOutgoingPacket(new SendMessage("Your plants are diseased!"));
			else if (stage == Plants.values()[plant].stages)
				harvest(player);
			else
				player.getClient().queueOutgoingPacket(new SendMessage("Your plants are healthy."));
		} else if ((option == 2) && (stage == Plants.values()[plant].stages))
			player.getClient().queueOutgoingPacket(new SendMessage("Your plants are healthy and ready to harvest."));
	}

	public void harvest(final Player player) {
		if (player.getInventory().hasItemId(FarmingPatches.values()[patch].harvestItem)) {
			final Plant instance = this;

			TaskQueue.queue(new Task(player, 2, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -3526551239803691368L;

				@Override
				public void execute() {
					player.getUpdateFlags().sendAnimation(FarmingPatches.values()[patch].harvestAnimation, 0);

					Item add = null;
					int id = Plants.values()[plant].harvest;

					if (player.isSuperMember()) {
						add = Item.getDefinition(id).isNote() ? new Item(id, 1) : new Item(Item.getDefinition(id)
								.getNoteId() == -1 ? id : Item.getDefinition(id).getNoteId(), 1);
					} else {
						add = Item.getDefinition(id).isNote() ? new Item(Item.getDefinition(id).getNoteId(), 1)
								: new Item(id, 1);
					}

					player.getAchievements().incr(player, "Harvest 500 crops");

					player.getInventory().addOrCreateGroundItem(add.getId(), add.getAmount(), true);

					String name = Item.getDefinition(Plants.values()[plant].harvest).getName();
					player.getClient().queueOutgoingPacket(
							new SendMessage("You harvest " + Misc.getAOrAn(name) + " " + name + "."));

					player.getSkill().addExperience(19, Plants.values()[plant].harvestExperience);

					if (harvested == 3 && player.getInventory().hasItemId(18336) && Misc.randomNumber(4) == 0) {
						player.getClient().queueOutgoingPacket(new SendMessage("You receive a seed back from your Scroll of life."));
						player.getInventory().addOrCreateGroundItem(Plants.values()[plant].seed, 1, true);
					}
					
					if (harvested++ > 5 && Misc.randomNumber(3) == 0) {
						player.getFarming().remove(instance);
						stop();
						return;
					}
				}

				@Override
				public void onStop() {
					player.getUpdateFlags().sendAnimation(65535, 0);
				}
			});
		} else {
			String name = Item.getDefinition(FarmingPatches.values()[patch].harvestItem).getName();
			player.getClient().queueOutgoingPacket(new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to harvest these plants."));
		}
	}

	public boolean useItemOnPlant(final Player player, int item) {
		if (item == 952) {
			player.getUpdateFlags().sendAnimation(830, 0);
			player.getFarming().remove(this);

			TaskQueue.queue(new RunOnceTask(player, 2) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -6002649043496218223L;

				@Override
				public void onStop() {
					player.getUpdateFlags().sendAnimation(65535, 0);
					player.getClient().queueOutgoingPacket(new SendMessage("You remove your plants from the plot."));
				}
			});
			return true;
		}
		if (item == 6036) {
			if (dead) {
				player.getClient().queueOutgoingPacket(new SendMessage("Your plant is dead!"));
			} else if (isDiseased()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cure the plant."));
				player.getUpdateFlags().sendAnimation(2288, 0);
				player.getInventory().remove(6036);
				disease = -1;
				doConfig(player);
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Your plant does not need this."));
			}

			return true;
		}
		if ((item >= 5331) && (item <= 5340)) {
			water(player, item);
			return true;
		}

		return false;
	}

	public void process(Player player) {
		if (dead || stage >= Plants.values()[plant].stages) {
			return;
		}

		int elapsed = GameSettings.DEV_MODE ? 3 : Misc.getMinutesElapsed(minute, hour, day, year);
		int grow = GameSettings.DEV_MODE ? 1 : Plants.values()[plant].minutes;

		if (elapsed >= grow) {
			for (int i = 0; i < elapsed / grow; i++) {
				if (isDiseased()) {
					if (!doDisease());
				} else if (!isWatered()) {
					if (!doWater());
				} else {
					stage++;
					if (stage >= Plants.values()[plant].stages) {
						return;
					}
				}

			}

			setTime();
		}
	}

	public void doConfig(Player player) {
		player.getFarming().doConfig();
	}

	public int getConfig() {
		if ((Plants.values()[plant].type == SeedType.ALLOTMENT) && (stage == 0) && (isWatered())) {
			return (Plants.values()[plant].healthy + stage + 64) * FarmingPatches.values()[patch].mod;
		}
		return (Plants.values()[plant].healthy + stage) * FarmingPatches.values()[patch].mod;
	}

	public FarmingPatches getPatch() {
		return FarmingPatches.values()[patch];
	}

	public boolean isDiseased() {
		return disease > -1;
	}

	public boolean isWatered() {
		return watered == -1;
	}
}
