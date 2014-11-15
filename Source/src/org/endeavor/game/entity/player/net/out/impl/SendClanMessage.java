package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.StreamBuffer.OutBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendClanMessage extends OutgoingPacket {

	private final String username;

	private final String message;

	private final String clanOwner;

	private final int rights;

	public SendClanMessage(String username, String message, String owner, int rights) {
		this.username = username;
		this.message = message;
		this.clanOwner = owner;
		this.rights = rights;
	}

	@Override
	public void execute(Client client) {
		OutBuffer out = StreamBuffer.newOutBuffer(100);
		out.writeHeader(client.getEncryptor(), getOpcode());
		out.writeString(username);
		out.writeString(message);
		out.writeString(clanOwner);
		out.writeShort(rights & 0xFF);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 217;
	}

}
