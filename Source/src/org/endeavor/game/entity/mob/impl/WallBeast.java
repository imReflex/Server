package org.endeavor.game.entity.mob.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class WallBeast extends Mob {
	private byte delay = 0;

	public WallBeast(Location location) {
		super(7823, true, location);
	}

	@Override
	public void process() {
		if (delay > 0) {
			delay = ((byte) (delay - 1));
			return;
		}

		for (Player p : World.getPlayers())
			if ((p != null) && (p.getLocation().getX() - getLocation().getX() == 0)
					&& (p.getLocation().getY() - getLocation().getY() == -1)) {
				getUpdateFlags().sendAnimation(new Animation(1802, 0));
				p.hit(new Hit(Misc.randomNumber(5) + 1));
				delay = 5;
				return;
			}
	}
}
