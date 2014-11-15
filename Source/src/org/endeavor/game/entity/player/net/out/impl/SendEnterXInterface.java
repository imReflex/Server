package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.StreamBuffer.OutBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendEnterXInterface extends OutgoingPacket {

	private final int interfaceId;
	private final int itemId;

	public SendEnterXInterface(int interfaceId, int itemId) {
		this.interfaceId = interfaceId;
		this.itemId = itemId;
	}

	public SendEnterXInterface() {
		interfaceId = 0;
		itemId = 0;
	}

	@Override
	public void execute(Client client) {
		if (itemId > 0 || interfaceId > 0) {
			client.getPlayer().setEnterXInterfaceId(interfaceId);
			client.getPlayer().setEnterXItemId(itemId);
		}
		OutBuffer outBuffer = StreamBuffer.newOutBuffer(5);
		outBuffer.writeHeader(client.getEncryptor(), getOpcode());
		client.send(outBuffer.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 27;
	}

}
