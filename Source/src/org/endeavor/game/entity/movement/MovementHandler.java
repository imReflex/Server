package org.endeavor.game.entity.movement;

import java.io.Serializable;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;

public abstract class MovementHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3740724184075000609L;
	protected int primaryDirection = -1;
	protected int secondaryDirection = -1;
	protected Location lastLocation = new Location(0, 0);
	protected Deque<Point> waypoints = new ConcurrentLinkedDeque<Point>();
	protected final Entity entity;
	protected boolean flag = false;

	protected boolean forceMove = false;

	private boolean forced = false;

	public MovementHandler(Entity entity) {
		this.entity = entity;
	}

	public abstract void process();

	public abstract boolean canMoveTo(int paramInt);

	public abstract boolean canMoveTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

	public void reset() {
		waypoints.clear();

		Location p = entity.getLocation();
		waypoints.add(new Point(p.getX(), p.getY(), -1));
	}

	public void resetMoveDirections() {
		primaryDirection = -1;
		secondaryDirection = -1;
	}

	public boolean moving() {
		return (waypoints.size() > 0) && (!entity.isFrozen());
	}

	public boolean hasDirection() {
		return primaryDirection != -1;
	}

	public void finish() {
		waypoints.removeFirst();
	}

	public void addToPath(Location location) {
		if (waypoints.size() == 0) {
			reset();
		}
		Point last = waypoints.peekLast();
		int deltaX = location.getX() - last.getX();
		int deltaY = location.getY() - last.getY();
		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0)
				deltaX++;
			else if (deltaX > 0) {
				deltaX--;
			}
			if (deltaY < 0)
				deltaY++;
			else if (deltaY > 0) {
				deltaY--;
			}
			addStep(location.getX() - deltaX, location.getY() - deltaY);
		}
	}

	private void addStep(int x, int y) {
		if (waypoints.size() >= 100) {
			return;
		}
		Point last = waypoints.peekLast();
		int deltaX = x - last.getX();
		int deltaY = y - last.getY();
		int direction = Misc.direction(deltaX, deltaY);
		if (direction > -1)
			waypoints.add(new Point(x, y, direction));
	}

	public void walkTo(int x, int y) {
		Location location = entity.getLocation();
		int newX = location.getX() + x;
		int newY = location.getY() + y;
		reset();
		addToPath(new Location(newX, newY));
		finish();
	}

	public void setPath(Deque<Point> path) {
		waypoints = path;
	}

	public int getPrimaryDirection() {
		return primaryDirection;
	}

	public void setPrimaryDirection(int primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	public int getSecondaryDirection() {
		return secondaryDirection;
	}

	public void setSecondaryDirection(int secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public void flag() {
		flag = true;
		forced = false;
	}

	public boolean isForced() {
		return forced;
	}

	public void setForced(boolean forced) {
		this.forced = forced;
	}

	public boolean isFlagged() {
		return flag;
	}

	public boolean isForceMove() {
		return forceMove;
	}

	public void setForceMove(boolean forceMove) {
		this.forceMove = forceMove;
	}
}
