package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.content.minigames.barrows.Barrows;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

/**
 * Digging with a spade
 */
public class DigTask extends Task {

	private final Player player;
	private int time = 0;

	public DigTask(Player player) {
		super(player, 1, true, StackType.NEVER_STACK, BreakType.ON_MOVE, TaskConstants.CURRENT_ACTION);
		this.player = player;
	}

	@Override
	public void execute() {
		if (++time == 1) {
			player.getUpdateFlags().sendAnimation(830, 0);
			player.getClient().queueOutgoingPacket(new SendMessage("You dig in the dirt.."));
			return;
		}

		if (++time != 3) {
			return;
		}

		player.getClient().queueOutgoingPacket(new SendSound(380, 10, 0));

		if (Barrows.dig(player)) {
			stop();
			return;
		}

		player.getClient().queueOutgoingPacket(new SendMessage("You find nothing of interest."));
		stop();
	}

	@Override
	public void onStop() {
		player.getUpdateFlags().sendAnimation(65535, 0);
	}

}
