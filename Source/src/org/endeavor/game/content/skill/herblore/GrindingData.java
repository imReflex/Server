package org.endeavor.game.content.skill.herblore;

import java.util.HashMap;
import java.util.Map;

public enum GrindingData {
	UNICORN_HORN(237, 235), CHOCOLATE_BAR(1973, 1975), BIRDS_NEST(5075, 6693), KEBBIT_TEETH(10109, 10111), BLUE_DRAGON_SCALE(
			243, 241), DIAMOND_ROOT(14703, 14704), DESERT_GOAT_HORN(9735, 9736), RUNE_SHARDS(6466, 6467);

	int itemId;
	int groundId;
	private static Map<Integer, GrindingData> grindables = new HashMap<Integer, GrindingData>();

	private GrindingData(int itemId, int groundId) {
		this.itemId = itemId;
		this.groundId = groundId;
	}

	public static final void declare() {
		for (GrindingData data : values())
			grindables.put(Integer.valueOf(data.getItemId()), data);
	}

	public int getItemId() {
		return itemId;
	}

	public int getGroundId() {
		return groundId;
	}

	public static GrindingData forId(int id) {
		return grindables.get(Integer.valueOf(id));
	}
}
