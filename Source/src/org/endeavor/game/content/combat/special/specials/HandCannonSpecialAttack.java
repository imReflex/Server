package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class HandCannonSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getRanged().setAnimation(new Animation(12175));
		player.getCombat().getRanged().setStart(new Graphic(2138, 0, false));
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
