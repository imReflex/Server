package org.endeavor.game.content.skill.mining;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ProspectingTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2153615407837498970L;
	private final Player player;
	private final MiningRockData data;

	public static boolean canProspectRock(Player player, GameObject object) {
		MiningRockData data = MiningRockData.forId(object.getId());
		return data == null;
	}

	public ProspectingTask(Player entity, GameObject object) {
		super(entity, 3, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		player = entity;
		data = MiningRockData.forId(object.getId());
		player.getClient().queueOutgoingPacket(new SendMessage("You search the rock for ore."));
	}

	@Override
	public void execute() {
		player.getClient().queueOutgoingPacket(
				new SendMessage("This ore contains " + GameDefinitionLoader.getItemDef(data.getReward()).getName()
						+ "."));
		stop();
	}

	@Override
	public void onStop() {
	}
}
