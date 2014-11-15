package org.endeavor.game.content.combat.special;

import org.endeavor.game.entity.player.Player;

public abstract interface Special {
	public abstract void handleAttack(Player player);

	public abstract int getSpecialAmountRequired();

	public abstract boolean checkRequirements(Player player);
}
