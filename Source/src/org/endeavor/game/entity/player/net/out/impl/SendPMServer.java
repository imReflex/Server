package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendPMServer extends OutgoingPacket {

	private final int state;

	public SendPMServer(int state) {
		super();
		this.state = state;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(client.getEncryptor(), 221);
		out.writeByte(state);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 221;
	}

}
