package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendUpdateSpecialBar extends OutgoingPacket {

	private final int amount;

	private final int id;

	public SendUpdateSpecialBar(int amount, int id) {
		super();
		this.amount = amount;
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 70);
		out.writeShort(amount);
		out.writeShort(0, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 70;
	}

}
