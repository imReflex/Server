package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendInterface extends OutgoingPacket {

	private final int id;

	public SendInterface(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(client.getEncryptor(), 97);
		out.writeShort(id);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().setActive(id, -1);
	}

	@Override
	public int getOpcode() {
		return 97;
	}

}
