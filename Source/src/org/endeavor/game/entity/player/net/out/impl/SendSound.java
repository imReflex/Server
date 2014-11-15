package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.Sound;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendSound extends OutgoingPacket {

	private final int id;

	private final int type;

	private final int delay;

	public SendSound(int id, int type, int delay) {
		super();
		this.id = id;
		this.delay = delay;
		if (type == 0) {
			this.type = 10;
		} else {
			this.type = type;
		}
	}

	public SendSound(Sound sound) {
		super();
		this.id = sound.id;
		this.type = sound.type;
		this.delay = sound.delay;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(18);
		out.writeHeader(client.getEncryptor(), 174);
		out.writeShort(id);
		out.writeByte(type);
		out.writeShort(delay);
		// out.writeShort(7);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 174;
	}

}
