package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.impl.Ranged;
import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.player.Player;

public class MagicShortbowSpecialAttack implements Special {
	public static final int MAGIC_SHORTBOW_PROJECTILE_ID = 256;
	public static final int DOUBLE_SHOOT_ANIMATION_ID = 1074;

	@Override
	public void handleAttack(Player player) {
		Ranged r = player.getCombat().getRanged();

		r.setStart(new Graphic(256, 5, true));
		r.setAnimation(new Animation(1074, 0));
		r.setProjectile(new Projectile(249));
		r.setStartGfxOffset((byte) 1);

		r.getProjectile().setDelay(35);

		r.execute(player.getCombat().getAttacking());

		r.setStartGfxOffset((byte) 0);
		r.setProjectileOffset(0);

		r.setProjectile(new Projectile(249));
	}

	@Override
	public int getSpecialAmountRequired() {
		return 55;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}
}
