package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class AnchorSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMelee().setAnimation(new Animation(5870, 0));
		player.getUpdateFlags().sendGraphic(Graphic.lowGraphic(1027, 0));
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
