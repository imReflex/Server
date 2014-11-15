package org.endeavor.game.content.quest;

import org.endeavor.game.entity.player.Player;

public abstract interface Quest {
	public abstract void init(Player paramPlayer);

	public abstract void doAction(Player paramPlayer, byte paramByte);

	public abstract void reward(Player paramPlayer);

	public abstract String getName();

	public abstract byte getFinalStage();

	public abstract short getId();

	public abstract boolean useItemOnObject(Player paramPlayer, int paramInt1, int paramInt2);

	public abstract boolean clickObject(Player paramPlayer, int paramInt1, int paramInt2);

	public abstract boolean clickButton(Player paramPlayer, int paramInt);

	public abstract byte getPoints();

	public abstract String[] getLinesForStage(byte paramByte);
}
