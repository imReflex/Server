package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendGlobalSound extends OutgoingPacket {

	private final int id;

	private final int type;

	private final int delay;

	public SendGlobalSound(int id, int type, int delay) {
		super();
		this.id = id;
		this.type = type;
		this.delay = delay;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(18);
		out.writeHeader(client.getEncryptor(), getOpcode());
		out.writeShort(id);
		out.writeByte(type);
		out.writeShort(delay);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				if (Misc.getExactDistance(client.getPlayer().getLocation(), player.getLocation()) < 10) {
					player.getClient().send(out.getBuffer());
				}
			}
		}
	}

	@Override
	public int getOpcode() {
		return 174;
	}

}
