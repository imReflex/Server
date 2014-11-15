package org.endeavor.game.content.skill.mining;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.entity.Animation;

public enum MiningPickAxeData {
	BRONZE_PICKAXE(1265, 1, 1, new Animation(625)), IRON_PICKAXE(1267, 1, 2, new Animation(626)), STEEL_PICKAXE(1269,
			6, 3, new Animation(627)), MITHRIL_PICKAXE(1273, 21, 4, new Animation(629)), ADAMANT_PICKAXE(1271, 31, 5,
			new Animation(628)), RUNE_PICKAXE(1275, 41, 6, new Animation(624)), INFERNO_ADZE(13661, 41, 7,
			new Animation(10222)), DRAGON_PICKAXE(15259, 61, 8, new Animation(12188));

	int pickId;
	int levelRequired;
	int bonus;
	Animation animation;
	private static Map<Integer, MiningPickAxeData> picks = new HashMap<Integer, MiningPickAxeData>();

	private MiningPickAxeData(int axeId, int level, int bonus, Animation animation) {
		pickId = axeId;
		levelRequired = level;
		this.bonus = bonus;
		this.animation = animation;
	}

	public static final void declare() {
		for (MiningPickAxeData data : values())
			picks.put(Integer.valueOf(data.getPickId()), data);
	}

	public int getPickId() {
		return pickId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getBonus() {
		return bonus;
	}

	public Animation getAnimation() {
		return animation;
	}

	public static MiningPickAxeData forId(int id) {
		return picks.get(Integer.valueOf(id));
	}
}
