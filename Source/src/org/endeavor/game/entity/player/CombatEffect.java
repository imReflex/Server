package org.endeavor.game.entity.player;

import org.endeavor.game.entity.Entity;

public abstract interface CombatEffect {
	public abstract void execute(Player paramPlayer, Entity paramEntity);
}
