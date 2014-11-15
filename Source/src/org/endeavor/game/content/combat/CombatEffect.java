package org.endeavor.game.content.combat;

import org.endeavor.game.entity.Entity;

public abstract interface CombatEffect {
	public abstract void execute(Entity paramEntity1, Entity paramEntity2);
}
