package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendAnimateObject extends OutgoingPacket {

	private final RSObject object;
	private final int animation;

	public SendAnimateObject(RSObject object, int animation) {
		this.object = object;
		this.animation = animation;
	}

	@Override
	public void execute(Client client) {
		if (object == null) {
			return;
		}

		new SendCoordinates(new Location(object.getX(), object.getY(), object.getZ()), client.getPlayer()).execute(client);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), 160);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeByte((object.getType() << 2) + (object.getFace() & 3), StreamBuffer.ValueType.S);
		out.writeShort(animation, StreamBuffer.ValueType.A);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 160;
	}

}
