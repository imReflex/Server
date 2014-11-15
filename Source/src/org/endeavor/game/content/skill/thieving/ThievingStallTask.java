package org.endeavor.game.content.skill.thieving;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ThievingStallTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4253469894865988438L;
	public static final int[][] THEIVING_ITEMS = { { 1277, 1 }, { 1281, 1 }, { 1211, 1 }, { 1285, 1 }, { 1331, 1 },
			{ 1289, 1 } };
	private Player player;
	private ThievingStallData data;

	public static void declare() {
		for (int i = 0; i < THEIVING_ITEMS.length; i++) {
			GameDefinitionLoader.setValue(THEIVING_ITEMS[i][0], (i + 1) * 300, (i + 1) * 150);
			GameDefinitionLoader.setAlchValue(THEIVING_ITEMS[i][0], (i + 1) * 250, (i + 1) * 150);
		}
	}

	public ThievingStallTask(int delay, Player player, ThievingStallData data) {
		super(player, delay, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.data = data;
	}

	public static void attemptStealFromStall(Player player, int id, Location location) {
		ThievingStallData data = ThievingStallData.getObjectById(id);
		
		if (data == null) {
			return;
		}
		
		if (player.getSkill().locked()) {
			return;
		}
		if (!meetsRequirements(player, data)) {
			return;
		}
		player.getSkill().lock(3);
		player.getUpdateFlags().sendAnimation(new Animation(832));

		TaskQueue.queue(new ThievingStallTask(1, player, data));
	}

	private void successfulAttempt(Player player, ThievingStallData data) {
		int randomItem = Misc.randomNumber(data.getRewards().length);
		player.getInventory().add(new Item(data.getRewards()[randomItem][0], data.getRewards()[randomItem][1]));

		if (data.getRewards()[randomItem][0] == 995) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You steal " + data.getRewards()[randomItem][1] + "gp from the stall."));
		}

		player.getSkill().addExperience(17, data.getExperience());
	}

	private static boolean meetsRequirements(Player player, ThievingStallData data) {
		if (player.getInventory().getFreeSlots() == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your inventory is full."));
			return false;
		}
		if (player.getSkill().getLevels()[17] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a thieving level of " + data.getLevelRequired()
							+ " to steal from this stall."));
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, data)) {
			stop();
			return;
		}
		successfulAttempt(player, data);
		stop();
	}

	@Override
	public void onStop() {
	}
}
