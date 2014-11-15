package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.impl.Attack;
import org.endeavor.game.content.combat.impl.Melee;
import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class DragonClawsSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		Melee m = player.getCombat().getMelee();
		Entity a = player.getCombat().getAttacking();

		m.setAnimation(new Animation(10961));
		player.getUpdateFlags().sendGraphic(new Graphic(1950, 0, false));

		m.execute(a);
		int d1 = player.getLastDamageDealt();

		if (d1 == 0) {
			m.execute(a);
			int d2 = player.getLastDamageDealt();

			m.setAttack(new Attack(2, m.getAttack().getAttackDelay()), new Animation(10961));
			if (d2 == 0) {
				m.execute(a);
			} else {
				m.setNextDamage(d2 / 2);
				m.execute(a);
			}
		} else {
			m.setNextDamage(d1 / 2);
			m.execute(a);

			m.setAttack(new Attack(2, m.getAttack().getAttackDelay()), new Animation(10961));

			int n = player.getLastDamageDealt();
			m.setNextDamage(n / 2);
			m.execute(a);
			m.setNextDamage(n - player.getLastDamageDealt());
		}
	}

	@Override
	public int getSpecialAmountRequired() {
		return 50;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}
}
