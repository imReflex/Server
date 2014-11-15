package org.endeavor.game.entity.mob.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class Kreearra extends Mob {
	public static final int KREEARRA = 6222;
	public static final int SPECIAL_CHANCE = 20;
	public static final int TORNADO_A = 1196;
	public static final int TORNADO_B = 1197;
	public static final int TORNADO_X = 2833;
	public static final int TORNADO_Y = 5302;
	private boolean tornados = false;

	public Kreearra() {
		super(6222, true, new Location(2831, 5303, 2));
	}

	@Override
	public void doAliveMobProcessing() {
		if ((getCombat().getAttacking() != null) && (!tornados) && (Misc.randomNumber(20) == 0)) {
			tornados = true;
			initTornados();
		}
	}

	public void checkForDamage(Location a) {
		for (Player player : getCombatants()) {
			Location b = player.getLocation();
			if ((Math.abs(a.getX() - b.getX()) <= 1) && (Math.abs(a.getY() - b.getY()) <= 1))
				player.hit(new Hit(Misc.randomNumber(20)));
		}
	}

	public void initTornados() {
		final Location a = new Location(2833, 5302, 2);
		final Location b = new Location(2833, 5302, 2);
		final Location c = new Location(2833, 5302, 2);
		final Location d = new Location(2833, 5302, 2);

		getUpdateFlags().sendForceMessage("Feel Armadyl's power!");

		TaskQueue.queue(new Task(1) {
			byte stage = 0;

			@Override
			public void execute() {
				if (isDead()) {
					stop();
					return;
				}

				a.move(1, 1);
				World.sendStillGraphic(1196, 0, a);
				checkForDamage(a);

				b.move(-1, 1);
				World.sendStillGraphic(1197, 0, b);
				checkForDamage(b);

				c.move(-1, -1);
				World.sendStillGraphic(1196, 0, c);
				checkForDamage(c);

				d.move(1, -1);
				World.sendStillGraphic(1197, 0, d);
				checkForDamage(d);

				if ((this.stage = (byte) (stage + 1)) == 5) {
					tornados = false;
					stop();
				}
			}

			@Override
			public void onStop() {
			}
		});
	}
}
