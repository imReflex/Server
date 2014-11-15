package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendSmithingItem extends OutgoingPacket {

	private final int id;
	private final int slot;
	private final int column;

	public SendSmithingItem(int id, int slot, int column) {
		this.id = id;
		this.slot = slot;
		this.column = column;
	}

	/*
	 * c.outStream.createFrameVarSizeWord(34); // init item to smith screen
	 * c.outStream.writeWord(column); // Column Across Smith Screen
	 * c.outStream.writeByte(4); // Total Rows? c.outStream.writeDWord(slot); //
	 * Row Down The Smith Screen c.outStream.writeWord(id + 1); // item
	 * c.outStream.writeByte(amount); // how many there are?
	 * c.outStream.endFrameVarSizeWord();
	 */

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(client.getEncryptor(), 34);
		out.writeShort(column);
		out.writeByte(4);
		out.writeShort(slot);
		out.writeShort(id + 1);
		out.writeByte(1);
		out.finishVariableShortPacketHeader();
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 34;
	}

}
