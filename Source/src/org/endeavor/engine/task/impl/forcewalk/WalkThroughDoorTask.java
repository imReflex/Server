package org.endeavor.engine.task.impl.forcewalk;

import org.endeavor.engine.cache.map.Door;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public class WalkThroughDoorTask extends Task {

	private final Player p;
	private final Door door;
	private final int xMod;
	private final int yMod;
	private byte stage = 0;
	private final Controller start;

	public WalkThroughDoorTask(Player p, int x, int y, int z, Location dest) {
		super(p, 1, true);
		this.p = p;
		this.door = Region.getDoor(x, y, z);
		start = p.getController();
		p.setController(ControllerManager.FORCE_MOVEMENT_CONTROLLER);

		int xDiff = p.getLocation().getX() - dest.getX();
		int yDiff = p.getLocation().getY() - dest.getY();

		if (xDiff != 0)
			xMod = (xDiff < 0 ? 1 : -1);
		else
			xMod = 0;
		if (yDiff != 0)
			yMod = (yDiff < 0 ? 1 : -1);
		else
			yMod = 0;

		if (xDiff != 0 && yDiff != 0 || door == null) {
			p.setController(start);
			stop();
		}
	}

	@Override
	public void execute() {
		if (stage == 0) {
			ObjectManager.send(new GameObject(ObjectManager.BLANK_OBJECT_ID, door.getX(), door.getY(), door.getZ(),
					door.getType(), door.getCurrentFace()));
			door.append();
			ObjectManager.send(new GameObject(door.getCurrentId(), door.getX(), door.getY(), door.getZ(), door
					.getType(), door.getCurrentFace()));
		} else if (stage == 1) {
			p.getMovementHandler().walkTo(xMod, yMod);
		} else if (stage == 2) {
			ObjectManager.send(new GameObject(ObjectManager.BLANK_OBJECT_ID, door.getX(), door.getY(), door.getZ(),
					door.getType(), door.getCurrentFace()));
			door.append();
			ObjectManager.send(new GameObject(door.getCurrentId(), door.getX(), door.getY(), door.getZ(), door
					.getType(), door.getCurrentFace()));
			p.setController(start);
			p.getUpdateFlags().sendFaceToDirection(p.getLocation().getX() - xMod, p.getLocation().getY() - yMod);
			stop();
		}

		stage++;
	}

	@Override
	public void onStop() {
	}

}
