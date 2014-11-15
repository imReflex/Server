package org.endeavor.game.content.minigames.dungeoneering;

import org.endeavor.game.entity.Location;

public abstract interface DungMap {
	public abstract Location getPlayerSpawn();

	public abstract Location[] getMobSpawnsForWave();

	public abstract void init(DungGame paramDungGame);
}
