package org.endeavor.game.content.quest;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public abstract class QuestMob extends Mob {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4187739545720703355L;

	public QuestMob(int npcId, Location location) {
		super(npcId, true, location);
	}

	public abstract void onClick(Player paramPlayer, int paramInt);
}
