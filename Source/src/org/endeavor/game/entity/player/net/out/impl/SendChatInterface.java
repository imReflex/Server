package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendChatInterface extends OutgoingPacket {

	private final int id;

	public SendChatInterface(int id) {
		super();
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 164);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		client.send(out.getBuffer());
		client.getPlayer().getInterfaceManager().setChat(id);
	}

	@Override
	public int getOpcode() {
		return 164;
	}

}
