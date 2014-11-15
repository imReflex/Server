package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.player.Player;

public class DragonSpearSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
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
