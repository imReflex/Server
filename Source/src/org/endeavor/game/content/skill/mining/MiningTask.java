package org.endeavor.game.content.skill.mining;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

/**
 * Handles the mining skill
 * 
 * @author Arithium
 * 
 */
public class MiningTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5052025635482201694L;
	private Player player;
	private GameObject rock;
	private MiningRockData rockData;
	private MiningPickAxeData pickData;
	private int animationCycle;
	private int cycle = 0;

	private static final int[] PICKAXES = { 1275, 15259, 1271, 1273, 1269, 1267, 1265 };

	public MiningTask(int delay, Player player, GameObject object, MiningRockData rock, MiningPickAxeData pick) {
		super(player, delay, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.rock = object;
		rockData = rock;
		pickData = pick;
	}

	private boolean successfulAttemptChance() {
		return SkillConstants.isSuccess(player, 14, rockData.getLevel(), pickData.getLevelRequired());
	}

	private void handleAnimation() {
		if ((animationCycle <= 1) || (animationCycle == 4) || (animationCycle == 6) || (animationCycle == 11)) {
			player.getClient().queueOutgoingPacket(new SendSound(432, 10, animationCycle == 11 ? 10 : 0));
		}

		if (animationCycle < 15) {
			animationCycle += 1;
		} else {
			animationCycle = 0;
			player.getUpdateFlags().sendAnimation(pickData.getAnimation());
			player.getUpdateFlags().sendFaceToDirection(rock.getLocation().getX(), rock.getLocation().getY());
		}
	}

	private static boolean meetsRequirements(Player player, MiningRockData rock, GameObject object) {
		if (!Region.objectExists(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object
				.getLocation().getZ())) {
			return false;
		}
		if (player.getSkill().getLevels()[14] < rock.getLevel()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a mining level of " + rock.getLevel() + " to mine this rock."));
			return false;
		}
		if (!checkForPickaxe(player)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You don't have a pickaxe."));
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You don't have enough inventory space to mine this."));
			player.getUpdateFlags().sendAnimation(new Animation(65535));
			return false;
		}
		return true;
	}

	private static boolean meetsAxeRequirements(Player player, MiningPickAxeData data) {
		if (player.getSkill().getLevels()[14] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a mining level of " + data.getLevelRequired() + " to use this pickaxe."));
			return false;
		}
		return true;
	}

	public static void attemptMining(Player player, int objectId, int x, int y, int z) {
		MiningRockData data = MiningRockData.forId(objectId);
		GameObject object = new GameObject(objectId, x, y, z, 10, 0);
		if (data == null)
			return;
		if (!meetsRequirements(player, data, object)) {
			return;
		}
		MiningPickAxeData pick = null;
		if (player.getEquipment().getItems()[3] != null) {
			for (int i : PICKAXES) {
				if (player.getEquipment().getItems()[3].getId() == i) {
					pick = MiningPickAxeData.forId(i);
					break;
				}
			}
		}
		if (pick == null) {
			for (Item item : player.getInventory().getItems()) {
				for (int i : PICKAXES) {
					if ((item != null) && (item.getId() == i)) {
						pick = MiningPickAxeData.forId(i);
						break;
					}
				}
			}
		}

		if (!meetsAxeRequirements(player, pick)) {
			return;
		}
		player.getUpdateFlags().sendAnimation(pick.getAnimation());
		player.getClient().queueOutgoingPacket(new SendMessage("You swing your pick at the rock."));
		TaskQueue.queue(new MiningTask(1, player, object, data, pick));
	}

	private static boolean checkForPickaxe(Player player) {
		if (player.getEquipment().getItems()[3] != null) {
			for (int i : PICKAXES) {
				if (player.getEquipment().getItems()[3].getId() == i) {
					return true;
				}
			}
		}
		for (Item item : player.getInventory().getItems()) {
			for (int i : PICKAXES) {
				if ((item != null) && (item.getId() == i)) {
					return true;
				}
			}
		}

		return false;
	}

	private void successfulAttempt() {
		player.getSkill().addExperience(14, rockData.getExperience());
		player.getInventory().add(new Item(rockData.getReward(), 1));
		player.getClient().queueOutgoingPacket(new SendMessage("You successfully mined the rock."));

		player.getAchievements().incr(player, "Mine 500 ores");

		if (rockData.getReplacement() != -1) {
			player.getUpdateFlags().sendAnimation(new Animation(65535));

			GameObject emptyRock = new GameObject(rockData.getReplacement(), rock.getLocation().getX(), rock
					.getLocation().getY(), rock.getLocation().getZ(), rock.getType(), rock.getFace());

			ObjectManager.register(emptyRock);
			TaskQueue.queue(new EmptyRockTask(rockData.getRespawnTimer(), emptyRock, rock));
		}
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, rockData, rock)) {
			stop();
			return;
		}

		handleAnimation();

		if ((cycle == 5) || ((rockData.getReplacement() == -1) && (cycle == 0))) {
			if (successfulAttemptChance()) {
				successfulAttempt();
				if (rockData.getReplacement() != -1) {
					stop();
				}
			}
			cycle = 0;
		} else {
			cycle += 1;
		}
	}

	@Override
	public void onStop() {
	}
}
