package org.endeavor.game.content.skill.fishing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.fishing.Fishing.FishingSpots;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class Fishing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5750234078402432406L;
	private final Player player;
	private FishableData.Fishable[] fishing = null;
	private ToolData.Tools tool = null;

	public Fishing(Player player) {
		this.player = player;
	}

	public void start(final Mob mob, FishableData.Fishable[] fishing) {
		if ((fishing == null) || (fishing[0] == null) || (fishing[0].getToolId() == -1)) {
			return;
		}

		this.fishing = fishing;

		tool = ToolData.Tools.forId(fishing[0].getToolId());

		if (!hasFishingItems(player, fishing[0], true)) {
			return;
		}

		player.getClient().queueOutgoingPacket(new SendSound(289, 0, 0));

		player.getUpdateFlags().sendAnimation(tool.getAnimationId(), 0);

		Task skill = new Task(player, 4, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0) {
			@Override
			public void execute() {
				player.face(mob);
				player.getUpdateFlags().sendAnimation(tool.getAnimationId(), 0);

				if (!fish()) {
					stop();
					reset();
					return;
				}
			}

			@Override
			public void onStop() {
			}
		};
		TaskQueue.queue(skill);
	}

	public boolean fish() {
		if (fishing == null) {
			return false;
		}

		FishableData.Fishable[] fish = new FishableData.Fishable[5];

		byte c = 0;

		for (int i = 0; i < fishing.length; i++) {
			if (canFish(player, fishing[i], i == 0)) {
				fish[c] = fishing[i];
				c = (byte) (c + 1);
			}
		}
		if (c == 0) {
			return false;
		}

		FishableData.Fishable f = fish[Misc.randomNumber(c)];

		if (player.getInventory().getFreeSlots() == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("You have run out of inventory space."));
			return false;
		}

		if (success(f)) {
			if (f.getBaitRequired() != -1) {
				int r = player.getInventory().remove(new Item(f.getBaitRequired(), 1));

				if (r == 0) {
					player.getClient().queueOutgoingPacket(new SendMessage("You have run out of bait."));
					return false;
				}
			}

			player.getClient().queueOutgoingPacket(new SendSound(378, 0, 0));

			int id = f.getRawFishId();
			String name = Item.getDefinition(id).getName();
			player.getInventory().add(new Item(id, 1));
			player.getSkill().addExperience(10, f.getExperience());
			player.getClient().queueOutgoingPacket(
					new SendMessage("You manage to catch " + getFishStringMod(name) + name + "."));

			player.getAchievements().incr(player, "Fish 1,000 items");
		}

		player.getSkill().lock(4);

		return true;
	}

	public boolean success(FishableData.Fishable fish) {
		return SkillConstants.isSuccess(player, 10, fish.getRequiredLevel());
	}

	public void reset() {
		fishing = null;
		tool = null;
	}

	public static boolean canFish(Player p, FishableData.Fishable fish, boolean message) {
		if (p.getMaxLevels()[10] < fish.getRequiredLevel()) {
			if (message) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You need a fishing level of " + fish.getRequiredLevel() + " to fish here."));
			}
			return false;
		}

		if (!hasFishingItems(p, fish, message)) {
			return false;
		}

		return true;
	}

	public static boolean hasFishingItems(Player player, FishableData.Fishable fish, boolean message) {
		int tool = fish.getToolId();
		int bait = fish.getBaitRequired();

		if (tool == 311) {
			if (!player.getInventory().hasItemAmount(new Item(tool, 1))) {
				Item weapon = player.getEquipment().getItems()[3];

				if ((weapon != null) && (weapon.getId() == 10129)) {
					return true;
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You need a Harpoon to fish here."));
				return false;
			}
		} else if ((!player.getInventory().hasItemAmount(new Item(tool, 1))) && (message)) {
			String name = Item.getDefinition(tool).getName();
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to fish here."));
			return false;
		}

		if ((bait > -1) && (!player.getInventory().hasItemAmount(new Item(bait, 1)))) {
			String name = Item.getDefinition(bait).getName();
			if (message) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to fish here."));
			}
			return false;
		}

		return true;
	}

	public boolean clickNpc(Mob mob, int id, int option) {
		if (FishingSpots.forId(id) == null) {
			return false;
		}

		FishingSpots spot = FishingSpots.forId(id);

		FishableData.Fishable[] f = new FishableData.Fishable[3];
		int amount = 0;
		FishableData.Fishable[] fish;
		switch (option) {
		case 1:
			fish = spot.option_1;
			for (int i = 0; i < fish.length; i++) {
				if (canFish(player, fish[i], i == 0)) {
					f[i] = fish[i];
					amount++;
				}
			}
			break;
		case 2:
			fish = spot.option_2;
			for (int i = 0; i < fish.length; i++) {
				if (canFish(player, fish[i], i == 0)) {
					f[i] = fish[i];
					amount++;
				}
			}

		}

		if (amount == 0) {
			return true;
		}

		FishableData.Fishable[] fishing = new FishableData.Fishable[amount];

		for (int i = 0; i < amount; i++) {
			fishing[i] = f[i];
		}

		start(mob, fishing);

		return true;
	}

	public String getFishStringMod(String name) {
		return name.substring(name.length() - 2, name.length() - 1).equals("s") ? "some " : "a ";
	}

	public static enum FishingSpots {
		SMALL_NET_OR_BAIT(316, new FishableData.Fishable[] { FishableData.Fishable.SHRIMP,
				FishableData.Fishable.ANCHOVIES }, new FishableData.Fishable[] { FishableData.Fishable.SARDINE,
				FishableData.Fishable.HERRING, FishableData.Fishable.PIKE }),

		LURE_OR_BAIT(317, new FishableData.Fishable[] { FishableData.Fishable.TROUT, FishableData.Fishable.SALMON },
				new FishableData.Fishable[] { FishableData.Fishable.SARDINE, FishableData.Fishable.HERRING,
					FishableData.Fishable.PIKE }),

		LURE_OR_BAIT2(328, new FishableData.Fishable[] { FishableData.Fishable.TROUT, FishableData.Fishable.SALMON },
				new FishableData.Fishable[] { FishableData.Fishable.SARDINE, FishableData.Fishable.HERRING,
					FishableData.Fishable.PIKE }),
					
		LURE_OR_BAIT3(310, new FishableData.Fishable[] { FishableData.Fishable.TROUT, FishableData.Fishable.SALMON },
				new FishableData.Fishable[] { FishableData.Fishable.SARDINE, FishableData.Fishable.HERRING,
					FishableData.Fishable.PIKE }),

		LURE_OR_BAIT4(311, new FishableData.Fishable[] { FishableData.Fishable.TROUT, FishableData.Fishable.SALMON },
				new FishableData.Fishable[] { FishableData.Fishable.SARDINE, FishableData.Fishable.HERRING,
					FishableData.Fishable.PIKE }),	
					
		CAGE_OR_HARPOON(324, new FishableData.Fishable[] { FishableData.Fishable.LOBSTER },
				new FishableData.Fishable[] { FishableData.Fishable.TUNA, FishableData.Fishable.SWORD_FISH }),
		
		CAGE_OR_HARPOON2(312, new FishableData.Fishable[] { FishableData.Fishable.LOBSTER },
				new FishableData.Fishable[] { FishableData.Fishable.TUNA, FishableData.Fishable.SWORD_FISH }),

		LARGE_NET_OR_HARPOON(334, new FishableData.Fishable[] { FishableData.Fishable.MACKEREL,
				FishableData.Fishable.COD, FishableData.Fishable.BASS }, new FishableData.Fishable[] {
				FishableData.Fishable.SHARK}),
				
		LARGE_NET_OR_HARPOON2(313, new FishableData.Fishable[] { FishableData.Fishable.MACKEREL,
				FishableData.Fishable.COD, FishableData.Fishable.BASS }, new FishableData.Fishable[] {
				FishableData.Fishable.SHARK}),

		HARPOON_OR_SMALL_NET(322, new FishableData.Fishable[] { FishableData.Fishable.MONK_FISH },
				new FishableData.Fishable[] { FishableData.Fishable.TUNA, FishableData.Fishable.SWORD_FISH }),

		ROCKTAIL_SPOT(7046, new FishableData.Fishable[] { FishableData.Fishable.LOBSTER },
				new FishableData.Fishable[] { FishableData.Fishable.ROCKTAIL }),	
				
		ROCKTAIL_SPOT2(3019, new FishableData.Fishable[] {  FishableData.Fishable.ROCKTAIL },
				new FishableData.Fishable[] { FishableData.Fishable.MANTA_RAY }),	
		;

		private int id;
		private FishableData.Fishable[] option_1;
		private FishableData.Fishable[] option_2;
		private static Map<Integer, FishingSpots> fishingSpots = new HashMap<Integer, FishingSpots>();

		public static final void declare() {
			for (FishingSpots spots : values())
				fishingSpots.put(Integer.valueOf(spots.getId()), spots);
		}

		public static FishingSpots forId(int id) {
			return fishingSpots.get(Integer.valueOf(id));
		}

		private FishingSpots(int id, FishableData.Fishable[] option_1, FishableData.Fishable[] option_2) {
			this.id = id;
			this.option_1 = option_1;
			this.option_2 = option_2;
		}

		public int getId() {
			return id;
		}

		public FishableData.Fishable[] getOption_1() {
			return option_1;
		}

		public FishableData.Fishable[] getOption_2() {
			return option_2;
		}
	}
}
