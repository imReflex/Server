package org.endeavor.engine.task;

import org.endeavor.game.entity.Entity;

public abstract class RunOnceTask extends Task {

	public RunOnceTask(Entity entity, int delay) {
		super(entity, delay);
	}

	@Override
	public void execute() {
		stop();
	}

	@Override
	public abstract void onStop();

}
