package org.endeavor.game.entity.movement.impl;

import org.endeavor.GameSettings;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.minigames.duelarena.DuelingConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.movement.MovementHandler;
import org.endeavor.game.entity.movement.Point;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendMultiInterface;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

public class PlayerMovementHandler extends MovementHandler {
	private final Player player;

	public PlayerMovementHandler(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public void process() {
		if ((player.isDead())
				|| (player.isFrozen())
				|| (player.getMagic().isTeleporting())
				|| ((player.getDueling().isDueling()) && player.getDueling().getRuleToggle()[DuelingConstants.NO_MOVEMENT])) {
			reset();
			return;
		}

		boolean isInHome = player.inHomeBank();

		Point walkPoint = waypoints.poll();

		if ((walkPoint != null) && (walkPoint.getDirection() != -1)) {
			if (player.getRunEnergy().isResting()) {
				player.getRunEnergy().toggleResting();
			}
			
			if ((!forceMove)
					&& (!GameSettings.DEV_MODE)
					&& (!Region.getRegion(player.getLocation()).canMove(player.getLocation(), walkPoint.getDirection()))
					|| entity.isOverSafePKBoundary(player.getY() + GameConstants.DIR[walkPoint.getDirection()][1])) {
				
				if (entity.isOverSafePKBoundary(player.getY() + GameConstants.DIR[walkPoint.getDirection()][1])) {
					player.getClient().queueOutgoingPacket(new SendMessage("A mysterious force blocks you from going any further.."));
				}
				
				reset();
				return;
			}

			player.getMovementHandler().getLastLocation().setAs(player.getLocation());
			player.getLocation().move(org.endeavor.game.GameConstants.DIR[walkPoint.getDirection()][0],
					org.endeavor.game.GameConstants.DIR[walkPoint.getDirection()][1]);
			primaryDirection = walkPoint.getDirection();
			flag = true;
		} else {
			if (flag) {
				flag = false;
			}
			return;
		}

		if (player.getRunEnergy().isRunning()) {
			if (player.getRunEnergy().canRun()) {
				Point runPoint = waypoints.poll();

				if ((runPoint != null) && (runPoint.getDirection() != -1)) {
					if ((!forceMove)
							&& (!GameSettings.DEV_MODE)
							&& (!Region.getRegion(player.getLocation()).canMove(player.getLocation(), runPoint.getDirection()))
							|| entity.isOverSafePKBoundary(player.getY() + GameConstants.DIR[walkPoint.getDirection()][1])) {
						
						if (entity.isOverSafePKBoundary(player.getY() + GameConstants.DIR[walkPoint.getDirection()][1])) {
							player.getClient().queueOutgoingPacket(new SendMessage("A mysterious force blocks you from going any further.."));
						}
						
						reset();
						return;
					}

					player.getMovementHandler().getLastLocation().setAs(player.getLocation());
					player.getLocation().move(org.endeavor.game.GameConstants.DIR[runPoint.getDirection()][0],
							org.endeavor.game.GameConstants.DIR[runPoint.getDirection()][1]);
					secondaryDirection = runPoint.getDirection();

					player.getRunEnergy().onRun();
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You don't have enough run energy to do that."));
				player.getRunEnergy().reset();
			}

		}

		/*if ((player.inHomeBank()) && (!isInHome))
			player.getClient().queueOutgoingPacket(new SendPlayerOption("View shop", 4));
		else if ((!player.inHomeBank()) && (isInHome)) {
			player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		}*/
		/*if(!player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
			player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		}*/

		Clan clan = Clans.getClanForOwner(player);
		if(clan != null) {
			if(clan.getWarSetup() != null) {
				clan.getWarSetup().cancel();
			}
		}
		
		ControllerManager.setControllerOnWalk(player);

		if (player.inMultiArea())
			player.getClient().queueOutgoingPacket(new SendMultiInterface(true));
		else {
			player.getClient().queueOutgoingPacket(new SendMultiInterface(false));
		}

		player.checkForRegionChange();

		if ((forceMove) && (waypoints.size() == 0))
			forceMove = false;
	}

	@Override
	public boolean canMoveTo(int direction) {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		int z = player.getLocation().getZ();

		return Region.getRegion(x, y).canMove(x, y, z, direction);
	}

	@Override
	public boolean canMoveTo(int x, int y, int size, int direction) {
		int z = player.getLocation().getZ();

		return Region.getRegion(x, y).canMove(x, y, z, direction);
	}
}
