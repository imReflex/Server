package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

public class SendTurnCamera extends OutgoingPacket {

	private final int x;
	private final int y;
	private final int z;
	private final int constantSpeed;
	private final int variableSpeed;

	/**
	 * Gradually turns the camera to look at a specified location;
	 * 
	 * @param location
	 *            The new camera location.
	 * @param zPos
	 *            The new cameraHeight, in positions.
	 * @param constantSpeed
	 *            The constant linear camera movement speed as angular units per
	 *            cycle. In the XY angle there are 2048 units in 360 degrees. In
	 *            the Z angle is restricted between 128 and 383, which are the
	 *            min and max ZAngle.
	 * @param variableSpeed
	 *            The variable linear camera movement speed as promile of what's
	 *            left. Max 99, 100 is instant.
	 * @return The action sender instance, for chaining.
	 */

	public SendTurnCamera(int x, int y, int z, int constantSpeed, int variableSpeed) {
		super();
		this.x = x / 64;
		this.y = y / 64;
		this.z = z;
		this.constantSpeed = constantSpeed;
		this.variableSpeed = variableSpeed;
	}

	@Override
	public void execute(Client client) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(client.getEncryptor(), 177);
		out.writeByte(x);
		out.writeByte(y);
		out.writeShort(z);
		out.writeByte(constantSpeed);
		out.writeByte(variableSpeed);
		client.send(out.getBuffer());
	}

	@Override
	public int getOpcode() {
		return 177;
	}

}
