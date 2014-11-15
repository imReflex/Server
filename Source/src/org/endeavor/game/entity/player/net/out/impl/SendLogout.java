package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendLogout extends OutgoingPacket {

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(client.getEncryptor(), 109);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 109;
	}

}
