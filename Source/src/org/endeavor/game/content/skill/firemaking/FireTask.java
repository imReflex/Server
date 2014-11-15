package org.endeavor.game.content.skill.firemaking;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;

public class FireTask extends Task {
	private final GameObject object;
	private final Player p;

	public FireTask(Player p, int cycles, GameObject object) {
		super(cycles, false, Task.StackType.STACK, Task.BreakType.NEVER, 0);
		this.object = object;
		ObjectManager.register(object);
		this.p = p;
	}

	@Override
	public void execute() {
		ObjectManager.remove(object);
		GroundItemHandler.add(new Item(592, 1), object.getLocation(), p);
		stop();
	}

	@Override
	public void onStop() {
	}
}
