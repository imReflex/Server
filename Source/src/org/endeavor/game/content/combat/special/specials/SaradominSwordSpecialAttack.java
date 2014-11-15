package org.endeavor.game.content.combat.special.specials;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.impl.Attack;
import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class SaradominSwordSpecialAttack implements Special {

	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMagic().setNextHit(Misc.randomNumber(20));
		
		player.getCombat().getMagic().setAttack(new Attack(1, player.getCombat().getAttackCooldown()), null, new Graphic(1224, 0, true), new Graphic(1207, 0, true), null);
		player.getCombat().getMagic().execute(player.getCombat().getAttacking());
		
		player.getCombat().getMelee().setAnimation(new Animation(7072, 0));
		player.getCombat().getMelee().setDamageBoost(1.1);
	}

	@Override
	public int getSpecialAmountRequired() {
		return 100;
	}

	@Override
	public boolean checkRequirements(Player paramPlayer) {
		return true;
	}

}
