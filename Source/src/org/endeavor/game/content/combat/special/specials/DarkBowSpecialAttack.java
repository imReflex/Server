package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.impl.Ranged;
import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class DarkBowSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		Ranged r = player.getCombat().getRanged();
		Item ammo = player.getEquipment().getItems()[13];

		if (ammo != null) {
			if ((ammo.getId() == 11212) || (ammo.getId() == 11227) || (ammo.getId() == 0) || (ammo.getId() == 11228)) {
				r.setProjectile(new Projectile(1099));
				r.setEnd(new Graphic(1100, 0, true));
			} else {
				r.setProjectile(new Projectile(1101));
				r.setEnd(new Graphic(1103, 0, true));
			}

		}

		r.setProjectileOffset(1);
	}

	@Override
	public int getSpecialAmountRequired() {
		return 60;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}
}
