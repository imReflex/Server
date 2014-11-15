package org.endeavor.game.content.skill.herblore;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class HerbloreGrindingTask extends Task {
	private final Player player;
	private final GrindingData data;

	public HerbloreGrindingTask(Player player, GrindingData data) {
		super(player, 1, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.data = data;
	}

	public static void handleGrindingIngredients(Player player, Item used, Item usedWith) {
		int itemId = used.getId() != 233 ? used.getId() : usedWith.getId();
		GrindingData data = GrindingData.forId(itemId);
		if (data == null)
			return;
		player.getUpdateFlags().sendAnimation(new Animation(364));
		TaskQueue.queue(new HerbloreGrindingTask(player, data));
	}

	private void createGroundItem() {
		player.getInventory().remove(data.getItemId(), 1);
		player.getInventory().add(new Item(data.getGroundId(), 1));
	}

	@Override
	public void execute() {
		createGroundItem();
		stop();
	}

	@Override
	public void onStop() {
	}
}
