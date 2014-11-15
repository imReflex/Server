package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendUpdateExperience extends OutgoingPacket {

	private final int id;
	private final double exp;

	public SendUpdateExperience(int id, double exp) {
		super();
		this.id = id;
		this.exp = exp;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(9);
		out.writeHeader(client.getEncryptor(), 124);
		out.writeShort(id);
		out.writeInt((int) exp);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 124;
	}
}
