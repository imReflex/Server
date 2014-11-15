package org.endeavor.game.entity.player.net.in;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.Player;

public abstract class IncomingPacket {
	public abstract void handle(Player paramPlayer, StreamBuffer.InBuffer paramInBuffer, int paramInt1, int paramInt2);

	public abstract int getMaxDuplicates();
}
