package org.endeavor.game.entity.mob.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Revenant extends Mob {
	public static final RevenantSpawn[] SPAWN_LOCS = { new RevenantSpawn(2958, 3543, 0, "Green dragons"),
			new RevenantSpawn(3049, 3612, 0, "Green dragons"), new RevenantSpawn(3052, 3653, 0, "Green dragons"),
			new RevenantSpawn(2967, 3668, 0, "Green dragons"), new RevenantSpawn(3356, 3648, 0, "Hill giants"),
			new RevenantSpawn(3304, 3683, 0, "Hill giants"), new RevenantSpawn(3351, 3684, 0, "Hill giants"),
			new RevenantSpawn(3345, 3706, 0, "Hill giants"), new RevenantSpawn(3107, 3696, 0, "Bounty Hunter Crater"),
			new RevenantSpawn(3115, 3713, 0, "Bounty Hunter Crater"),
			new RevenantSpawn(3154, 3708, 0, "Bounty Hunter Crater"),
			new RevenantSpawn(3053, 3661, 0, "Bounty Hunter Crater"),
			new RevenantSpawn(3168, 3654, 0, "Bounty Hunter Crater"), new RevenantSpawn(3174, 3932, 0, "Mage bank"),
			new RevenantSpawn(3136, 3910, 0, "Mage bank"), new RevenantSpawn(3043, 3939, 0, "Mage bank"),
			new RevenantSpawn(3078, 3908, 0, "Mage bank") };

	public static final int[] REV_IDS = { 6647, 6648, 6649, 6998 };

	private static final Revenant[] REVENANTS = new Revenant[5];

	private boolean healed = false;

	static {
		TaskQueue.queue(new Task(300) {
			@Override
			public void execute() {
				Revenant[] r = Revenant.get3RandomAliveRevenant();
				if (r != null) {
					for (Player p : World.getPlayers()) {
						if (p != null && p.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) {
							for (int i = 0; i < r.length && i < 3; i++) {
								if (r[i] != null) {
									p.getClient().queueOutgoingPacket(new SendMessage("<col=255>There is a Revenant alive at level " + r[i].getWildernessLevel() + ".</col>"));
								}
							}
						}
					}
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public static boolean withinRevenant(Entity e) {
		for (Revenant i : REVENANTS) { 
			if ((i != null) && (!i.isDead())) {
				int xd = Math.abs(i.getLocation().getX() - e.getLocation().getX());
				int yd = Math.abs(i.getLocation().getY() - e.getLocation().getY());
				if ((xd < 30) && (yd < 30)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isRevenant(Mob mob) {
		for (int i : REV_IDS) {
			if (mob.getId() == i) {
				return true;
			}
		}
		return false;
	}

	public static Revenant[] get3RandomAliveRevenant() {
		Revenant[] alive = new Revenant[REVENANTS.length];
		byte c = 0;

		for (Revenant i : REVENANTS) {
			if ((!i.isDead()) && (i.isVisible())) {
				alive[c] = i;
				c++;
			}
		}

		if (c == 0) {
			return null;
		}
		
		Revenant[] ret = new Revenant[c];
		
		for (int i = 0; i < c; i++) {
			ret[i] = alive[i];
		}
		
		return ret;
	}

	public static void spawn() {
		REVENANTS[0] = new Revenant(new Location(2992, 3648));
		REVENANTS[1] = new Revenant(new Location(3067, 3660));
		REVENANTS[2] = new Revenant(new Location(3248, 3706));
		REVENANTS[3] = new Revenant(new Location(3130, 3954));
		REVENANTS[4] = new Revenant(new Location(3134, 3574));
	}

	public Revenant(Location l) {
		super(REV_IDS[Misc.randomNumber(REV_IDS.length)], true, l);
	}

	@Override
	public void doAliveMobProcessing() {
		if ((!healed) && (getLevels()[3] <= 50)) {
			getLevels()[3] = ((short) (getLevels()[3] + 100));
			if (getLevels()[3] > getMaxLevels()[3]) {
				getLevels()[3] = getMaxLevels()[3];
			}

			healed = true;
		}
	}

	public String getArea() {
		int d = 99999;
		RevenantSpawn low = null;
		for (RevenantSpawn i : SPAWN_LOCS) {
			int dc = Misc.getManhattanDistance(i, getLocation());
			if (dc < d) {
				low = i;
				d = dc;
			}
		}

		return low.name;
	}

	@Override
	public void onDeath() {
		healed = false;
	}

	@Override
	public Location getNextSpawnLocation() {
		transform(REV_IDS[Misc.randomNumber(REV_IDS.length)]);
		return getSpawnLocation();
	}

	@Override
	public int getRespawnTime() {
		return 150;
	}

	public static class RevenantSpawn extends Location {
		public final String name;

		public RevenantSpawn(int x, int y, int z, String name) {
			super(x, y, z);
			this.name = name;
		}
	}
}
