package org.endeavor.game.content.minigames.dungeoneering;

import java.util.ArrayList;
import java.util.List;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;

public class DungChest extends GameObject {
	private List<Integer> used = new ArrayList<Integer>();
	private final boolean trap;

	public DungChest(int id, int x, int y, int z, int type, int face) {
		super(id, x, y, z, type, face);
		trap = (Misc.randomNumber(5) == 0);
	}

	public boolean isTrap() {
		return trap;
	}

	public void setSearchedBy(Player p) {
		used.add(Integer.valueOf(p.getIndex()));
	}

	public boolean canSearch(Player p) {
		return !used.contains(Integer.valueOf(p.getIndex()));
	}

	public void setCheckedBy(Player p) {
		used.add(Integer.valueOf(p.getIndex() + 32768));
	}

	public boolean canCheck(Player p) {
		return !used.contains(Integer.valueOf(p.getIndex() + 32768));
	}
}
