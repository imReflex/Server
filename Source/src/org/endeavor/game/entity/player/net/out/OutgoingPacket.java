package org.endeavor.game.entity.player.net.out;

import org.endeavor.game.entity.player.net.Client;

public abstract class OutgoingPacket {
	public abstract void execute(Client paramClient);

	public abstract int getOpcode();

	@Override
	public boolean equals(Object o) {
		if ((o instanceof OutgoingPacket)) {
			return ((OutgoingPacket) o).getOpcode() == getOpcode();
		}

		return false;
	}
}
