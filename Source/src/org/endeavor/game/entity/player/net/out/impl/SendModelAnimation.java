package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendModelAnimation extends OutgoingPacket {

	private final int model;

	private final int anim;

	public SendModelAnimation(int model, int anim) {
		super();
		this.model = model;
		this.anim = anim;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), 200);
		out.writeShort(model);
		out.writeShort(anim);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 200;
	}

}
