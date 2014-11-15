package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendWeight extends OutgoingPacket {

	private final int weight;

	public SendWeight(int weight) {
		super();
		this.weight = weight;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 240);
		out.writeShort(weight);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 240;
	}

}
