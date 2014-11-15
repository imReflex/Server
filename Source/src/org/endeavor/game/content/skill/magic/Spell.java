package org.endeavor.game.content.skill.magic;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public abstract class Spell {
	public abstract String getName();

	public abstract int getLevel();

	public abstract Item[] getRunes();

	public abstract boolean execute(Player paramPlayer);

	public abstract double getExperience();
}
