package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendLoginResponse extends OutgoingPacket {

	private final int response;
	private final int rights;
	private final int crownId;

	public SendLoginResponse(int response, int rights, int crownId) {
		super();
		this.response = response;
		this.rights = rights;
		this.crownId = crownId;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(4);
		//System.out.println("Response: " + response + " Rights: " + rights + " Crown : " + crownId);
		resp.writeByte(response);
		resp.writeByte(rights);
		resp.writeByte(crownId);
		resp.writeByte(0);
		client.send(resp.getBuffer());
		//System.out.println("Send Map Region...");
		new SendMapRegion(client.getPlayer()).execute(client);
		new SendDetails(client.getPlayer().getIndex()).execute(client);
	}

	@Override
	public int getOpcode() {
		return -1;
	}

}
