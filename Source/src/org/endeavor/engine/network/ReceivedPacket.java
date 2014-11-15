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
package org.endeavor.engine.network;

/**
 *
 * @author Stuart Murphy
 *
 */
import org.jboss.netty.buffer.ChannelBuffer;

public class ReceivedPacket {

	private final int opcode;
	private final int size;
	private final ChannelBuffer payload;

	public ReceivedPacket(int opcode, int size, ChannelBuffer payload) {
		this.opcode = opcode;
		this.size = size;
		this.payload = payload;
	}

	public int getOpcode() {
		return opcode;
	}

	public int getSize() {
		return size;
	}

	public ChannelBuffer getPayload() {
		return payload;
	}
}
