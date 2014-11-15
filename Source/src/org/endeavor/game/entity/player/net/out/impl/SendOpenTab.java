package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendOpenTab extends OutgoingPacket {

	private final int id;

	public SendOpenTab(int id) {
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(client.getEncryptor(), 106);
		out.writeByte(id, StreamBuffer.ValueType.C);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 106;
	}

}
