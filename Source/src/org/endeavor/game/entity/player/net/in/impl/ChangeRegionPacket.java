package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendDetails;

public class ChangeRegionPacket extends IncomingPacket {
	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		player.getClient().queueOutgoingPacket(new SendDetails(player.getIndex()));
		player.getGroundItems().onRegionChange();
		player.getObjects().onRegionChange();

		if (player.getDueling().isStaking()) {
			player.getDueling().decline();
		}

		if (player.getTrade().trading()) {
			player.getTrade().end(false);
		}

		if (player.getMinigames().getBetManager().betting()) {
			player.getMinigames().getBetManager().end(false);
		}

		player.resetAggression();

		if (player.getDungGame() != null) {
			player.getDungGame().spawnObjects(player);
		}
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}
}
