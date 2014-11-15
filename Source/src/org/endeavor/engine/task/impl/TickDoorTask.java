package org.endeavor.engine.task.impl;

import org.endeavor.engine.cache.map.Door;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;

public class TickDoorTask extends Task {

	private final Door door;

	private byte stage = 10;

	public TickDoorTask(Door door) {
		super(null, 1);
		this.door = door;
		
		if (door.original()) {
			stop();
			return;
		}
	}

	@Override
	public void execute() {
		/*if (stage <= 0) {
			ObjectManager.removeFromList(new GameObject(door.getCurrentId(), door.getX(), door.getY(), door.getZ(), door.getType(), door.getCurrentFace()));
			ObjectManager.queueSend(new GameObject(ObjectManager.BLANK_OBJECT_ID, door.getX(), door.getY(), door.getZ(), door.getType(), door.getCurrentFace()));

			Region.getRegion(door.getX(), door.getY()).appendDoor(door.getCurrentId(), door.getX(), door.getY(), door.getZ());

			ObjectManager.removeFromList(new GameObject(ObjectManager.BLANK_OBJECT_ID, door.getX(), door.getY(), door.getZ(), door.getType(), door.getCurrentFace()));
			ObjectManager.queueSend(new GameObject(door.getCurrentId(), door.getX(), door.getY(), door.getZ(), door.getType(), door.getCurrentFace()));
			*/
			stop();
			/*return;
		}

		stage--;*/
	}

	@Override
	public void onStop() {
	}

}
