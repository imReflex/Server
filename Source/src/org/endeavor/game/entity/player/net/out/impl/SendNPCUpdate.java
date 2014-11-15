package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.game.entity.mob.MobUpdateFlags;
import org.endeavor.game.entity.player.PlayerUpdateFlags;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.NpcUpdating;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendNPCUpdate extends OutgoingPacket {

	private final MobUpdateFlags[] nFlags;
	private final PlayerUpdateFlags pFlags;

	public SendNPCUpdate(MobUpdateFlags[] nFlags, PlayerUpdateFlags pFlags) {
		super();
		this.nFlags = nFlags;
		this.pFlags = pFlags;
	}

	@Override
	public void execute(Client client) {
		NpcUpdating.update(client, pFlags, nFlags);
	}

	@Override
	public int getOpcode() {
		return 65;
	}

}
