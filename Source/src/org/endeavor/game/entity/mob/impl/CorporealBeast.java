package org.endeavor.game.entity.mob.impl;

import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;

public class CorporealBeast extends Mob {
	private Mob[] darkEnergyCores = null;

	public void spawn() {
		new CorporealBeast();
	}

	public CorporealBeast() {
		super(8133, true, new Location(2946, 4386));
	}

	@Override
	public void doPostHitProcessing(Hit hit) {
		if ((getCombatants().size() != 0) && (getLevels()[3] != 0) && (getLevels()[3] <= 150) && (areCoresDead()))
			darkEnergyCores = DarkEnergyCore.spawn();
	}

	@Override
	public void onDeath() {
		darkEnergyCores = null;
	}

	public boolean areCoresDead() {
		if (darkEnergyCores == null) {
			return true;
		}

		for (Mob mob : darkEnergyCores) {
			if (!mob.isDead()) {
				return false;
			}
		}

		return true;
	}
}
