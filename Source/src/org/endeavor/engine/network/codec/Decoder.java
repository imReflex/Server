package org.endeavor.engine.network.codec;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.endeavor.engine.network.ReceivedPacket;
import org.endeavor.engine.network.security.ISAACCipher;
import org.endeavor.engine.utility.Misc;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * 
 * @author Graham Edgecombe
 * @author Stuart Murphy
 * 
 */
public class Decoder extends FrameDecoder {

	private final ISAACCipher cipher;
	private int opcode = -1;
	private int size = -1;

	public Decoder(ISAACCipher cipher) {
		this.cipher = cipher;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (opcode == -1) {
			if (buffer.readableBytes() >= 1) {
				opcode = buffer.readByte() & 0xFF;
				opcode = (opcode - cipher.getNextValue()) & 0xFF;
				size = Misc.packetLengths[opcode];
			} else {
				return null;
			}
		}
		if (size == -1) {
			if (buffer.readableBytes() >= 1) {
				size = buffer.readByte() & 0xFF;
			} else {
				return null;
			}
		}
		if (buffer.readableBytes() >= size) {
			byte[] data = new byte[size];
			buffer.readBytes(data);

			ChannelBuffer payload = ChannelBuffers.buffer(size);
			payload.writeBytes(data);

			try {
				return new ReceivedPacket(opcode, size, payload);
			} finally {
				opcode = -1;
				size = -1;
			}
		}
		return null;
	}
}
