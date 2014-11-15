package org.endeavor.game.entity.movement;

import org.endeavor.game.entity.Location;

public class Point extends Location {
	private int direction;

	public Point(int x, int y, int direction) {
		super(x, y);
		setDirection(direction);
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}
}
