package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendSong extends OutgoingPacket {

	private final int id;

	public SendSong(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		if (id != -1) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
			out.writeHeader(client.getEncryptor(), 74);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			client.send(out.getBuffer());
		}
	}

	@Override
	public int getOpcode() {
		return 74;
	}

}
