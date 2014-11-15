package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class DragonMaceSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMelee().setAnimation(new Animation(1060, 0));
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(251, 2));
	}

	@Override
	public int getSpecialAmountRequired() {
		return 25;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}
}
