package org.endeavor.game.content.skill.woodcutting;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.entity.Animation;

public enum WoodcuttingAxeData {
	BRONZE_AXE(1351, 1, 1, new Animation(879)), IRON_AXE(1349, 1, 2, new Animation(877)), STEEL_AXE(1353, 6, 3,
			new Animation(875)), BLACK_AXE(1361, 6, 4, new Animation(873)), MITHRIL_AXE(1355, 21, 5, new Animation(871)), ADAMANT_AXE(
			1357, 31, 6, new Animation(869)), RUNE_AXE(1359, 41, 7, new Animation(867)), DRAGON_AXE(6739, 61, 8,
			new Animation(2846)), INFERNAL_ADZE(13661, 41, 8, new Animation(10251));

	int itemId;
	int levelRequired;
	int bonus;
	Animation animation;
	private static Map<Integer, WoodcuttingAxeData> axes = new HashMap<Integer, WoodcuttingAxeData>();

	private WoodcuttingAxeData(int id, int level, int bonus, Animation animation) {
		itemId = id;
		levelRequired = level;
		this.bonus = bonus;
		this.animation = animation;
	}

	public static final void declare() {
		for (WoodcuttingAxeData data : values())
			axes.put(Integer.valueOf(data.getId()), data);
	}

	public int getId() {
		return itemId;
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

	public static WoodcuttingAxeData forId(int id) {
		return axes.get(Integer.valueOf(id));
	}
}
