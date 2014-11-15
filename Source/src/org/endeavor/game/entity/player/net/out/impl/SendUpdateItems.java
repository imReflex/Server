package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendUpdateItems extends OutgoingPacket {

	private final int id;

	private final Item[] items;

	public SendUpdateItems(int id, Item[] items) {
		super();
		this.id = id;
		this.items = items;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4096);
		out.writeVariableShortPacketHeader(client.getEncryptor(), 53);
		out.writeShort(id);
		if (items == null) {
			out.writeShort(0);
			out.writeByte(0);
			out.writeShort(0/*, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE*/);
			out.finishVariableShortPacketHeader();
			client.send(out.getBuffer());
			return;
		}
		out.writeShort(items.length);
		for (Item item : items) {
			if (item != null) {
				if (item.getAmount() > 254) {
					out.writeByte(255);
					out.writeInt(item.getAmount(), StreamBuffer.ByteOrder.INVERSE_MIDDLE);
				} else {
					out.writeByte(item.getAmount());
				}
				out.writeShort(item.getId() + 1/*, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE*/);
			} else {
				out.writeByte(0);
				out.writeShort(0/*, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE*/);
			}
		}
		out.finishVariableShortPacketHeader();
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 53;
	}

}
