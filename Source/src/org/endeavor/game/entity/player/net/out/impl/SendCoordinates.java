package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendCoordinates extends OutgoingPacket {

	private final Location p;
	private final Location base;

	public SendCoordinates(Location p, Player player) {
		super();
		this.p = p;
		base = player.getCurrentRegion();
	}

	public SendCoordinates(Location p, Location base) {
		super();
		this.p = p;
		this.base = base;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(client.getEncryptor(), 85);
		int y = p.getY() - 8 * base.getRegionY();
		int x = p.getX() - 8 * base.getRegionX();
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 85;
	}

}
