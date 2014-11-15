package org.endeavor.game.content.skill.woodcutting;

import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;

public class StumpTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3892867912817418067L;
	private GameObject object;
	private final int treeId;

	public StumpTask(GameObject object, int treeId, int delay) {
		super(delay, false, Task.StackType.STACK, Task.BreakType.NEVER, 5);
		this.treeId = treeId;
		this.object = object;
	}

	@Override
	public void execute() {
		ObjectManager.removeFromList(object);

		RSObject rsObject = new RSObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation()
				.getZ(), treeId, 10, 0);
		Region.getRegion(object.getLocation().getX(), object.getLocation().getY()).addObject(rsObject);

		ObjectManager.send(new GameObject(treeId, object.getLocation().getX(), object.getLocation().getY(), object
				.getLocation().getZ(), 10, 0));
		stop();
	}

	@Override
	public void onStop() {
	}
}
