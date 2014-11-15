package org.endeavor.game.entity.pathfinding;

import org.endeavor.game.GameConstants;
import org.endeavor.game.content.minigames.pestcontrol.impl.Shifter;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.Walking;

public class SimplePathWalker {
	public static void walkToNextTile(Mob mob, Location waypoint) {
		if (mob.getLocation().equals(waypoint)) {
			return;
		}
		
		int direction = -1;

		final int x = mob.getLocation().getX();
		final int y = mob.getLocation().getY();
		final int xDifference = waypoint.getX() - mob.getLocation().getX();
		final int yDifference = waypoint.getY() - mob.getLocation().getY();

		int toX = 0;
		int toY = 0;

		if (xDifference > 0) {
			toX = 1;
		} else if (xDifference < 0) {
			toX = -1;
		}

		if (yDifference > 0) {
			toY = 1;
		} else if (yDifference < 0) {
			toY = -1;
		}

		int toDir = GameConstants.getDirection(x, y, x + toX, y + toY);
		
		if (walkable(mob, toDir)) {
			direction = toDir;
		} else {
			if (toDir == 0) {
				if (walkable(mob, 3)) {
					direction = 3;
				} else if (walkable(mob, 1)) {
					direction = 1;
				}
			} else if (toDir == 2) {
				if (walkable(mob, 1)) {
					direction = 1;
				} else if (walkable(mob, 4)) {
					direction = 4;
				}
			} else if (toDir == 5) {
				if (walkable(mob, 3)) {
					direction = 3;
				} else if (walkable(mob, 6)) {
					direction = 6;
				}
			} else if (toDir == 7) {
				if (walkable(mob, 4)) {
					direction = 4;
				} else if (walkable(mob, 6)) {
					direction = 6;
				}
			}
		}
		
		if (direction == -1) {
			return;
		}
		
		Walking.walk(mob, direction);
	}

	public static boolean walkable(Entity entity, int i) {
		return entity.getMovementHandler().canMoveTo(i);
	}
}
