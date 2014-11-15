package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.StreamBuffer.ByteOrder;
import org.endeavor.engine.network.StreamBuffer.ValueType;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendColor extends OutgoingPacket {

	private final int id;

	private final int color;

	public SendColor(int id, int color) {
		super();
		this.id = id;
		this.color = color;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8);
		out.writeHeader(client.getEncryptor(), 122);
		out.writeShort(id, ByteOrder.BIG);
		out.writeShort(color, ByteOrder.BIG);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 122;
	}

}
