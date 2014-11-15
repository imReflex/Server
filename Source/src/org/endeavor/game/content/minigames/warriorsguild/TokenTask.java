package org.endeavor.game.content.minigames.warriorsguild;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class TokenTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2204457532219124385L;
	private Player player;

	public TokenTask(Player player, byte delay) {
		super(delay);
		this.player = player;
	}

	@Override
	public void execute() {
		player.getInventory().remove(8851, 20);

		if (player.getInventory().getItemAmount(8851) < 20) {
			player.teleport(WarriorsGuildConstants.NO_TOKENS);
			player.getClient().queueOutgoingPacket(new SendMessage("Oh dear, You have run out of tokens!"));
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			stop();
		}
	}

	@Override
	public void onStop() {
	}
}
