package org.endeavor.game.content.minigames.fightcave;

import java.util.ArrayList;
import java.util.List;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public final class TzharrDetails {
	private int stage = 0;
	private boolean kiln = false;
	private List<Mob> mobs = new ArrayList<Mob>();
	private int z;

	public void reset() {
		stage = 0;
		kiln = false;
	}

	public void setZ(Player p) {
		z = (p.getIndex() * 4);
	}

	public int getZ() {
		return z;
	}

	public void increaseStage() {
		stage += 1;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getStage() {
		return stage;
	}

	public int getKillAmount() {
		return mobs.size();
	}

	public List<Mob> getMobs() {
		return mobs;
	}

	public void addNpc(Mob mob) {
		mobs.add(mob);
	}

	public boolean removeNpc(Mob mob) {
		int index = mobs.indexOf(mob);

		if (index == -1) {
			return false;
		}

		mobs.remove(mob);
		return true;
	}

	public boolean isKiln() {
		return kiln;
	}

	public void setKiln(boolean kiln) {
		this.kiln = kiln;
	}
	
}
