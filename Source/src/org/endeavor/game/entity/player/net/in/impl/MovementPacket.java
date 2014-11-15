package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.GameSettings;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.pathfinding.RS317PathFinder;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class MovementPacket extends IncomingPacket {
	@Override
	public int getMaxDuplicates() {
		return 2;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (player.getMagic().isTeleporting()) || (!player.getController().canMove(player))
				|| (PlayerConstants.isSettingAppearance(player))) {
			player.getCombat().reset();
			return;
		}

		if (System.currentTimeMillis() - player.getCurrentStunDelay() < player.getSetStunDelay()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are stunned!"));
			player.getCombat().reset();
			return;
		}

		if (player.isFrozen()) {
			player.getClient().queueOutgoingPacket(new SendMessage("A magical force stops you from moving."));
			player.getCombat().reset();
			return;
		}

		if (opcode == 248) {
			length -= 14;
		}

		if (opcode != 98) {
			player.getMovementHandler().setForced(false);

			player.getMagic().getSpellCasting().disableClickCast();
			player.getFollowing().reset();
			player.getCombat().reset();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

			if (player.getTrade().trading()) {
				player.getTrade().end(false);
			}

			if (player.getDueling().isStaking()) {
				player.getDueling().decline();
			}

			if (player.getMinigames().getBetManager().betting()) {
				player.getMinigames().getBetManager().end(false);
			}

			player.start(null);
			player.getShopping().reset();
			player.getInterfaceManager().reset();
			TaskQueue.onMovement(player);
			player.setEnterXInterfaceId(0);
		} else {
			player.getMovementHandler().setForced(true);
		}

		int steps = (length - 5) / 2;
		int[][] path = new int[steps][2];

		int firstStepX = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);

		for (int i = 0; i < steps; i++) {
			path[i][0] = in.readByte();
			path[i][1] = in.readByte();
		}

		int firstStepY = in.readShort(StreamBuffer.ByteOrder.LITTLE);
		in.readByte(StreamBuffer.ValueType.C);

		player.getMovementHandler().reset();

		boolean dev = (GameSettings.DEV_MODE) && (PlayerConstants.isOwner(player));

		if (dev) {
			player.getMovementHandler().addToPath(new Location(firstStepX, firstStepY));
		}

		for (int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
			if (dev) {
				player.getMovementHandler().addToPath(new Location(path[i][0], path[i][1]));
			}
		}

		if (steps > 0) {
			if ((Math.abs(path[(steps - 1)][0] - player.getLocation().getX()) > 21)
					|| (Math.abs(path[(steps - 1)][1] - player.getLocation().getY()) > 21)) {
				player.getMovementHandler().reset();
			}

		} else if ((Math.abs(firstStepX - player.getLocation().getX()) > 21)
				|| (Math.abs(firstStepY - player.getLocation().getY()) > 21)) {
			player.getMovementHandler().reset();
			return;
		}

		if (!dev) {
			if (steps > 0)
				RS317PathFinder.findRoute(player, path[(steps - 1)][0], path[(steps - 1)][1], true, 16, 16);
			else {
				RS317PathFinder.findRoute(player, firstStepX, firstStepY, true, 16, 16);
			}
		}

		if (dev)
			player.getMovementHandler().finish();
	}
}
