package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendDetails extends OutgoingPacket {

	private final int slot;

	public SendDetails(int slot) {
		super();
		this.slot = slot;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(client.getEncryptor(), 249);
		out.writeByte(1, StreamBuffer.ValueType.A);
		out.writeShort(slot, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 249;
	}

}
