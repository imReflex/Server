package org.endeavor.game.content.skill.runecrafting;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class RunecraftingTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3843289595252443899L;
	private final Player player;
	private final RunecraftingData data;
	private final int essenceId;
	public static final int PURE_ESSENCE = 7936;

	public RunecraftingTask(Player player, RunecraftingData data, int essenceId) {
		super(player, 1, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.data = data;
		this.essenceId = essenceId;
	}

	private static boolean meetsRequirements(Player player, RunecraftingData data, GameObject object) {
		if (player.getSkill().getLevels()[20] < data.getLevel()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a runecrafting level of " + data.getLevel() + " to craft this rune."));
			return false;
		}
		return true;
	}

	private static int getEssenceId(Player player) {
		if (player.getInventory().hasItemId(7936)) {
			return 7936;
		}
		return -1;
	}

	public static boolean attemptRunecrafting(Player player, GameObject object) {
		RunecraftingData data = RunecraftingData.forId(object.getId());

		if (data == null) {
			return true;
		}

		if (getEssenceId(player) == -1) {
			player.getClient().queueOutgoingPacket(new SendMessage("You don't have any essence to craft runes with."));
			return true;
		}

		if (!meetsRequirements(player, data, object)) {
			return true;
		}

		TaskQueue.queue(new RunecraftingTask(player, data, getEssenceId(player)));
		return true;
	}

	private int getMultiplier() {
		int multiplier = 1;
		for (int i = 1; i < data.getMultiplier().length; i++) {
			if (player.getMaxLevels()[20] >= data.getMultiplier()[i]) {
				multiplier = i;
			}
		}

		return multiplier;
	}

	@Override
	public void execute() {
		player.getClient().queueOutgoingPacket(new SendSound(481, 1, 0));
		player.getUpdateFlags().sendAnimation(new Animation(791));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(186, 0));

		int amount = player.getInventory().getItemAmount(essenceId);

		player.getSkill().addExperience(20, amount * data.getXp());

		player.getInventory().remove(new Item(essenceId, amount));
		player.getInventory().add(new Item(data.getRuneId(), amount * getMultiplier()));

		stop();
	}

	@Override
	public void onStop() {
	}
}
