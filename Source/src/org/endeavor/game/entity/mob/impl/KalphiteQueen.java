package org.endeavor.game.entity.mob.impl;

import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class KalphiteQueen extends Mob {
	private Entity lastKilledBy = null;
	
	public KalphiteQueen() {
		super(1158, true, new Location(3480, 9495));
	}

	public void onDeath() {
		lastKilledBy = getCombat().getLastAttackedBy();
	}
	
	public void transform() {
		transform(getId() == 1160 ? 1158 : 1160);
		
		if (lastKilledBy != null) {
			getCombat().setAttack(lastKilledBy);
		}
	}

	@Override
	public int getAffectedDamage(Hit hit) {
		switch (hit.getType()) {
		case MAGIC:
			if (getId() == 1158) {
				return 0;
			}
			break;
		case MELEE:
			if (getId() == 1160) {
				if ((hit.getAttacker() != null) && (!hit.getAttacker().isNpc())) {
					Player player = World.getPlayers()[hit.getAttacker().getIndex()];
					
					if ((player != null) && (player.getMelee().isVeracEffectActive())) {
						return hit.getDamage();
					}
				}

				return 0;
			}
			break;
		case RANGED:
			if (getId() == 1158) {
				return 0;
			}
			break;
		default:
			return hit.getDamage();
		}

		return hit.getDamage();
	}
}
