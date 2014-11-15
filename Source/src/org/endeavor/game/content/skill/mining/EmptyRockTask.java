package org.endeavor.game.content.skill.mining;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;

public class EmptyRockTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4106165203451897608L;
	private GameObject object;
	private GameObject emptyRock;

	public EmptyRockTask(int delay, GameObject emptyRock, GameObject object) {
		super(delay, false, Task.StackType.STACK, Task.BreakType.NEVER, 0);
		this.object = object;
		this.emptyRock = emptyRock;
	}

	@Override
	public void execute() {
		ObjectManager.removeFromList(emptyRock);
		ObjectManager.send(object);
		stop();
	}

	@Override
	public void onStop() {
	}
}
