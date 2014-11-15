package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.player.Player;

public class VestaLongswordSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getMelee().setAnimation(new Animation(10502, 0));
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
