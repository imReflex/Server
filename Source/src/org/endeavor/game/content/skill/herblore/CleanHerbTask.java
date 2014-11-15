package org.endeavor.game.content.skill.herblore;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class CleanHerbTask extends Task {
	private final Player player;
	private int slot;
	private GrimyHerbData data;

	public CleanHerbTask(Player player, int slot, GrimyHerbData data) {
		super(player, 0, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.slot = slot;
		this.data = data;
	}

	private static boolean meetsRequirements(Player player, GrimyHerbData data) {
		if (player.getSkill().getLevels()[15] < data.getLevelReq()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need an herblore level of " + data.getLevelReq() + " to clean this herb."));
			return false;
		}
		return true;
	}

	public static void attemptHerbCleaning(Player player, int slot) {
		if (player.getInventory().get(slot) == null) {
			return;
		}
		
		GrimyHerbData data = GrimyHerbData.forId(player.getInventory().get(slot).getId());

		if (data == null) {
			return;
		}
		if (!meetsRequirements(player, data)) {
			return;
		}

		TaskQueue.queue(new CleanHerbTask(player, slot, data));
	}

	private void cleanHerb() {
		player.getInventory().getItems()[slot] = null;
		player.getInventory().getItems()[slot] = new Item(data.getCleanHerb(), 1);
		player.getInventory().update();
		player.getSkill().addExperience(15, data.getExp());
		player.getClient().queueOutgoingPacket(new SendMessage("You clean the dirt off the herb."));
	}

	@Override
	public void execute() {
		cleanHerb();
		stop();
	}

	@Override
	public void onStop() {
	}
}
