package org.endeavor.game.content.combat.special.specials;

import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class StaffOfLightSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getAttacking().getUpdateFlags().sendGraphic(Graphic.highGraphic(1958, 0));
		player.getCombat().getAttacking().getUpdateFlags().sendAnimation(new Animation(10516, 0));
	}

	@Override
	public int getSpecialAmountRequired() {
		return 100;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}
}
