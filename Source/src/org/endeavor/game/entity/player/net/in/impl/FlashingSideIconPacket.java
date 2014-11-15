package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;

public class FlashingSideIconPacket extends IncomingPacket {
	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}
}
