package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class DragonHalberdSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMelee().setAnimation(new Animation(1203, 0));
		player.getUpdateFlags().sendGraphic(Graphic.lowGraphic(282, 0));
		player.getCombat().getAttacking().getUpdateFlags().sendGraphic(Graphic.lowGraphic(282, 0));
	}

	@Override
	public int getSpecialAmountRequired() {
		return 0;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return false;
	}
}
