package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendDuelEquipment extends OutgoingPacket {

	private final int id;

	private final int amount;

	private final int slot;

	public SendDuelEquipment(int id, int amount, int slot) {
		super();
		this.id = id;
		this.amount = amount;
		this.slot = slot;
	}

	public SendDuelEquipment(Item i, int slot) {
		this(i.getId(), i.getAmount(), slot);
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(client.getEncryptor(), 34);
		out.writeShort(13824);
		out.writeByte(slot);
		if (id == 0) {
			out.writeShort(0);
			out.writeByte(0);
		} else {
			out.writeShort(id + 1);
			if (amount > 254) {
				out.writeByte(255);
				out.writeInt(amount);
			} else {
				out.writeByte(amount);
			}
		}
		out.finishVariableShortPacketHeader();
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 34;
	}

}
