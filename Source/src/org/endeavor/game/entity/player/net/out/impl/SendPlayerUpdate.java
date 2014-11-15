package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.game.entity.player.PlayerUpdateFlags;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.PlayerUpdating;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendPlayerUpdate extends OutgoingPacket {

	private final PlayerUpdateFlags[] pFlags;

	public SendPlayerUpdate(PlayerUpdateFlags[] pFlags) {
		super();
		this.pFlags = pFlags;
	}

	@Override
	public void execute(Client client) {
		PlayerUpdating.update(client.getPlayer(), pFlags);
	}

	@Override
	public int getOpcode() {
		return 81;
	}

}
