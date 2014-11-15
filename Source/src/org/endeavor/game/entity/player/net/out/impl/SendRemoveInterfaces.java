package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendRemoveInterfaces extends OutgoingPacket {

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(client.getEncryptor(), 219);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().reset();
	}

	@Override
	public int getOpcode() {
		return 219;
	}

}
