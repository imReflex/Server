package org.endeavor.game.content.skill.slayer;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.player.Player;

public enum SlayerMob {
	CRAWLING_HAND("crawling hand", 1);

	public final int level;
	public final String name;
	private static final Map<Integer, Byte> mobs = new HashMap<Integer, Byte>();

	private SlayerMob(String name, int level) {
		this.level = level;
		this.name = name;
	}

	public static final void declare() {
		for (SlayerMob i : values()) {
			NpcDefinition def = null;
			for (int k = 0; k < 10000; k++)
				if (((def = GameDefinitionLoader.getNpcDefinition(k)) != null)
						&& (def.getName().equalsIgnoreCase(i.name)))
					mobs.put(Integer.valueOf(k), Byte.valueOf((byte) i.level));
		}
	}

	public static final boolean canAttack(Player p, int id) {
		byte get = mobs.get(Integer.valueOf(id)) != null ? mobs.get(Integer.valueOf(id)).byteValue() : 0;

		if (get == 0) {
			return true;
		}

		return p.getLevels()[18] >= get;
	}
}
