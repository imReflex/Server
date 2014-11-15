package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;

public class CloseInterfacePacket extends IncomingPacket {
	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		player.getInterfaceManager().reset();

		if (player.getController().equals(ControllerManager.ROLLING_DICE_CONTROLLER)) {
			player.getClient().queueOutgoingPacket(new SendInterface(6675));
			return;
		}
		if (player.getMinigames().getBetManager().betting()) {
			player.getMinigames().getBetManager().end(false);
			return;
		}

		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}

		if (player.getDueling().isStaking()) {
			player.getDueling().decline();
		}

		player.getShopping().reset();
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}
}
