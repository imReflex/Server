package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendObject extends OutgoingPacket {

	private final GameObject o;
	private final Location base;

	public SendObject(Player p, GameObject o) {
		super();
		this.o = o;
		this.base = new Location(p.getCurrentRegion());
	}

	@Override
	public void execute(Client client) {
		//System.out.println("Object Spawn: " + o.getId() + " - " + o.getLocation().getX() + " ");
		new SendCoordinates(o.getLocation(), base).execute(client);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(client.getEncryptor(), getOpcode());
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeShort(o.getId(), StreamBuffer.ByteOrder.LITTLE);
		out.writeByte(((o.getType() << 2) + (o.getFace() & 3)), StreamBuffer.ValueType.S);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 151;
	}

}
