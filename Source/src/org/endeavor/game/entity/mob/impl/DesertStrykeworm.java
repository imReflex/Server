package org.endeavor.game.entity.mob.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public abstract class DesertStrykeworm extends Mob {
	private boolean teleported = false;

	public static void spawn() {
		new DesertStrykeworm(2029, 5234) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2043, 5243);
			}
		};
		new DesertStrykeworm(2042, 5189) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2041, 5215);
			}
		};
		new DesertStrykeworm(2006, 5203) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2017, 5214);
			}
		};
		new DesertStrykeworm(1991, 5238) {
			@Override
			public Location getTeleportToLocation() {
				return new Location(2015, 5235);
			}
		};
	}

	public DesertStrykeworm(int x, int y) {
		super(9465, true, new Location(x, y));
	}

	public abstract Location getTeleportToLocation();

	@Override
	public boolean isWalkToHome() {
		return false;
	}

	@Override
	public void doAliveMobProcessing() {
		teleport();
	}

	@Override
	public void onDeath() {
		teleported = false;
	}

	@Override
	public boolean withinMobWalkDistance(Entity e) {
		return true;
	}

	public void teleport() {
		if (!teleported) {
			if (getLevels()[3] < getMaxLevels()[3] / 2) {
				teleported = true;

				if (getCombatants() != null) {
					for (Player p : getCombatants()) {
						p.getClient().queueOutgoingPacket(new SendMessage("The Desert strykeworm retreats!"));
					}
				}

				TaskQueue.queue(new Task(this, 1) {
					private byte b = 0;

					@Override
					public void onStop() {
						teleport(getTeleportToLocation());
					}

					@Override
					public void execute() {
						b = ((byte) (b + 1));

						getCombat().setAttackTimer(5);

						if (b == 8)
							getUpdateFlags().sendAnimation(new Animation(12796, 0));
						else if (b == 10)
							stop();
					}
				});
			}
		}
	}
}
