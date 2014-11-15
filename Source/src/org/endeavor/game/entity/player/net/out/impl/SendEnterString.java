package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.StreamBuffer.OutBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendEnterString extends OutgoingPacket {

	@Override
	public void execute(Client client) {
		OutBuffer outBuffer = StreamBuffer.newOutBuffer(5);
		outBuffer.writeHeader(client.getEncryptor(), getOpcode());
		client.send(outBuffer.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 187;
	}

}
