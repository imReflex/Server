package org.endeavor.game.content.combat;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Entity;

public interface CombatInterface {
	
	public abstract void hit(Hit paramHit);

	public abstract void onHit(Entity paramEntity, Hit paramHit);

	public abstract boolean canAttack();
	
	public abstract void updateCombatType();

	public abstract void onCombatProcess(Entity paramEntity);

	public abstract void afterCombatProcess(Entity paramEntity);

	public abstract void onAttack(Entity paramEntity, int paramInt, CombatTypes paramCombatTypes, boolean paramBoolean);

	public abstract int getAccuracy(Entity paramEntity, CombatTypes paramCombatTypes);

	public abstract int getMaxHit(CombatTypes paramCombatTypes);

	public abstract int getCorrectedDamage(int paramInt);

	public abstract boolean isIgnoreHitSuccess();

	public abstract void checkForDeath();

}
