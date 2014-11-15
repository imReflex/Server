package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendMessage extends OutgoingPacket {

	private final String message;

	public SendMessage(String message) {
		super();
		this.message = message;
	}

	@Override
	public void execute(Client client) {
		String check = message.toLowerCase();
		if (check.contains("inventory") && check.contains("space")) {
			new SendSound(1252, 10, 0).execute(client);
		}

		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 3);
		out.writeVariablePacketHeader(client.getEncryptor(), 253);
		out.writeString(message);
		out.finishVariablePacketHeader();
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 253;
	}

}
