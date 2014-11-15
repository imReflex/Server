package org.endeavor.engine.task.impl.forcewalk;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.pathfinding.SimplePathWalker;
import org.endeavor.game.entity.player.Player;

public class MobWalkTask extends Task {
	private final Mob mob;
	private final Location l;
	private byte wait = 0;
	private final boolean shouldWait;

	public MobWalkTask(Mob mob, Location l, boolean shouldWait) {
		super(mob, 1, true, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, 0);
		this.mob = mob;
		mob.setForceWalking(true);
		this.l = l;
		this.shouldWait = shouldWait;
	}

	@Override
	public void execute() {
		if (mob.isDead()) {
			stop();
			return;
		}

		if (Misc.getManhattanDistance(l, mob.getLocation()) <= 0) {
			stop();
		} else {
			SimplePathWalker.walkToNextTile(mob, l);

			if (mob.getMovementHandler().getPrimaryDirection() == -1) {
				if (shouldWait) {
					if (wait != 0) {
						wait = ((byte) (wait - 1));
						return;
					}

					for (Player p : World.getPlayers()) {
						if ((p != null) && (mob.getLocation().isViewableFrom(p.getLocation()))) {
							wait = 15;
							return;
						}
					}

					mob.teleport(l);
				}

				stop();
			} else {
				mob.getCombat().reset();
			}
		}
	}

	@Override
	public void onStop() {
		mob.setForceWalking(false);
	}
}
