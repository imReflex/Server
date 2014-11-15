package org.endeavor.game.content.skill.farming;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Farming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5362091678099891825L;
	private transient Player player;
	private Plant[] plants = new Plant[50];
	private GrassyPatch[] patches = new GrassyPatch[50];

	public Farming(Player player) {
		this.player = player;

		for (int i = 0; i < patches.length; i++)
			if (patches[i] == null)
				patches[i] = new GrassyPatch();
	}

	public void init() {
		TaskQueue.queue(new Task(player, /*50*/1, true) {
			@Override
			public void execute() {
				for (Plant i : plants) {
					if (i != null) {
						i.process(player);
					}
				}

				for (int i = 0; i < patches.length; i++) {
					if (i >= FarmingPatches.values().length) {
						break;
					}
					if ((patches[i] != null) && (!inhabited(FarmingPatches.values()[i].x, FarmingPatches.values()[i].y))) {
						patches[i].process(player, i);
					}
				}

				doConfig();
			}

			@Override
			public void onStop() {
			}
		});
		for (Plant i : plants) {
			if (i != null) {
				i.doConfig(player);
			}
		}

		for (int i = 0; i < patches.length; i++) {
			if (i >= FarmingPatches.values().length) {
				break;
			}
			if ((patches[i] != null) && (!inhabited(FarmingPatches.values()[i].x, FarmingPatches.values()[i].y)))
				patches[i].doConfig(player, i);
		}
	}

	public void doConfig() {
		for (int i = 0; i < FarmingPatches.values().length; i++)
			player.getClient().queueOutgoingPacket(
					new SendConfig(FarmingPatches.values()[i].config, getConfigFor(FarmingPatches.values()[i].config)));
	}

	public int getConfigFor(int configId) {
		int config = 0;

		for (FarmingPatches i : FarmingPatches.values()) {
			if (i.config == configId) {
				if (inhabited(i.x, i.y)) {
					for (Plant plant : plants)
						if ((plant != null) && (plant.getPatch().ordinal() == i.ordinal())) {
							config += plant.getConfig();
							break;
						}
				} else {
					config += patches[i.ordinal()].getConfig(i.ordinal());
				}
			}
		}

		return config;
	}

	public void clear() {
		for (int i = 0; i < plants.length; i++) {
			plants[i] = null;
		}

		for (int i = 0; i < patches.length; i++)
			patches[i] = new GrassyPatch();
	}

	public void nextWateringCan(int id) {
		player.getInventory().setId(player.getInventory().getItemSlot(id), id - (id == 5333 ? 2 : 1));
		player.getInventory().update();
	}

	public void insert(Plant patch) {
		for (int i = 0; i < plants.length; i++)
			if (plants[i] == null) {
				plants[i] = patch;
				break;
			}
	}

	public boolean inhabited(int x, int y) {
		for (int i = 0; i < plants.length; i++) {
			if (plants[i] != null) {
				FarmingPatches patch = plants[i].getPatch();
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean click(Player player, int x, int y, int option) {
		for (int i = 0; i < FarmingPatches.values().length; i++) {
			FarmingPatches patch = FarmingPatches.values()[i];

			if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
				if ((inhabited(x, y)) || (patches[i] == null))
					break;
				patches[i].click(player, option, i);
				return true;
			}

		}

		for (int i = 0; i < plants.length; i++) {
			if (plants[i] != null) {
				FarmingPatches patch = plants[i].getPatch();
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					plants[i].click(player, option);
					return true;
				}
			}
		}

		return false;
	}

	public void remove(Plant plant) {
		for (int i = 0; i < plants.length; i++)
			if ((plants[i] != null) && (plants[i].equals(plant))) {
				patches[plants[i].getPatch().ordinal()].setTime();
				plants[i] = null;
				doConfig();
				return;
			}
	}

	public boolean useItemOnPlant(int item, int x, int y) {
		if (item == 5341) {
			for (int i = 0; i < FarmingPatches.values().length; i++) {
				FarmingPatches patch = FarmingPatches.values()[i];

				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					patches[i].rake(player, i);
					break;
				}
			}

			return true;
		}

		for (int i = 0; i < plants.length; i++) {
			if (plants[i] != null) {
				FarmingPatches patch = plants[i].getPatch();
				if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
					plants[i].useItemOnPlant(player, item);
					return true;
				}
			}
		}

		return false;
	}

	public boolean plant(int seed, int object, int x, int y) {
		if (!Plants.isSeed(seed)) {
			return false;
		}

		for (FarmingPatches patch : FarmingPatches.values()) {
			if ((x >= patch.x) && (y >= patch.y) && (x <= patch.x2) && (y <= patch.y2)) {
				if (!patches[patch.ordinal()].isRaked()) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You must remove the weeds before planting!"));
					return true;
				}

				for (Plants plant : Plants.values()) {
					if (plant.seed == seed) {
						if (player.getMaxLevels()[19] >= plant.level) {
							if (inhabited(x, y)) {
								player.getClient().queueOutgoingPacket(
										new SendMessage("You can't plant on top of your other plants!"));
								return true;
							}

							if (patch.seedType != plant.type) {
								player.getClient().queueOutgoingPacket(
										new SendMessage("You can't plant this type of seed here!"));
								return true;
							}

							if (player.getInventory().hasItemId(patch.planter)) {
								player.getUpdateFlags().sendAnimation(2291, 0);
								player.getClient().queueOutgoingPacket(
										new SendMessage("You bury the seed in the dirt, don't forget to water it."));

								player.getInventory().remove(seed, 1, true);

								Plant planted = new Plant(patch.ordinal(), plant.ordinal());

								planted.setTime();
								insert(planted);

								doConfig();

								player.getSkill().addExperience(19, plant.plantExperience);
							} else {
								String name = Item.getDefinition(patch.planter).getName();
								player.getClient().queueOutgoingPacket(
										new SendMessage("You need " + Misc.getAOrAn(name) + " " + name
												+ " to plant seeds."));
							}

						} else {
							player.getClient().queueOutgoingPacket(
									new SendMessage("You need a Farming level of " + plant.level + " to plant this."));
						}

						return true;
					}
				}

				return false;
			}
		}

		return false;
	}

	public Plant[] getPlants() {
		return plants;
	}

	public void setPlants(Plant[] plants) {
		this.plants = plants;
	}

	public GrassyPatch[] getPatches() {
		return patches;
	}

	public void setPatches(GrassyPatch[] patches) {
		this.patches = patches;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
