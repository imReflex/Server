package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendString extends OutgoingPacket {

	private final String message;

	private final int id;

	public SendString(String message, int id) {
		super();
		this.message = message;
		this.id = id;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 6);
		out.writeVariableShortPacketHeader(client.getEncryptor(), 126);
		out.writeString(message);
		out.writeShort(id, StreamBuffer.ValueType.A);
		out.finishVariableShortPacketHeader();
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 126;
	}

}
