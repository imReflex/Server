package org.endeavor.game.content.skill.summoning;

import org.endeavor.game.entity.player.Player;

public abstract interface FamiliarSpecial {
	public abstract boolean execute(Player paramPlayer, FamiliarMob paramFamiliarMob);

	public abstract int getAmount();

	public abstract SpecialType getSpecialType();

	public abstract double getExperience();

	public static enum SpecialType {
		COMBAT, NONE;
	}
}
