package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendMoveCamera extends OutgoingPacket {

	private final int x, y, z, constantSpeed, variableSpeed;

	/**
	 * Gradually moves the camera to a specified location;
	 * 
	 * @param location
	 *            The new camera location.
	 * @param zPos
	 *            The new cameraHeight, in positions.
	 * @param constantSpeed
	 *            The constant linear camera movement speed as positions per
	 *            cycle.
	 * @param variableSpeed
	 *            The variable linear camera movement speed as promile of what's
	 *            left. Max 99, 100 is instant.
	 * @return The action sender instance, for chaining.
	 */

	public SendMoveCamera(int x, int y, int z, int constantSpeed, int variableSpeed) {
		super();
		Location l = new Location(x, y);
		this.x = l.getLocalX();
		this.y = l.getLocalY();
		this.z = z;
		this.constantSpeed = constantSpeed;
		this.variableSpeed = variableSpeed;
	}

	public SendMoveCamera(int x, int y, int z) {
		super();
		this.x = x / 128 - 64;
		this.y = y / 128 - 64;
		this.z = z;
		this.constantSpeed = 0;
		this.variableSpeed = 100;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 166);
		out.writeByte(x);
		out.writeByte(y);
		out.writeShort(z);
		out.writeByte(constantSpeed);
		out.writeByte(variableSpeed);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 166;
	}

}
