package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendFlashSidebarIcon extends OutgoingPacket {

	private final int id;

	public SendFlashSidebarIcon(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 24);
		out.writeByte(-id, StreamBuffer.ValueType.A);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 24;
	}

}
