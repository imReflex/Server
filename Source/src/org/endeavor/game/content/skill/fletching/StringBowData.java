package org.endeavor.game.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

public enum StringBowData {
	NORMAL_SHORTBOW(50, 841, 1, 5.0D), NORMAL_LONGBOW(48, 839, 10, 10.0D), OAK_SHORTBOW(54, 843, 20, 16.5D), OAK_LONGBOW(
			56, 845, 25, 25.0D), WILLOW_SHORTBOW(60, 849, 35, 33.25D), WILLOW_LONGBOW(58, 847, 40, 41.5D), MAPLE_SHORTBOW(
			64, 853, 50, 50.0D), MAPLE_LONGBOW(62, 851, 55, 58.200000000000003D), YEW_SHORTBOW(68, 857, 65, 67.5D), YEW_LONGBOW(
			66, 855, 70, 75.0D), MAGIC_SHORTBOW(72, 861, 80, 83.200000000000003D), MAGIC_LONGBOW(70, 859, 85, 91.5D);

	private final int item;
	private final int product;
	private final int levelRequired;
	private final double experience;
	private static final Map<Integer, StringBowData> bow = new HashMap<Integer, StringBowData>();

	public static void declare() {
		for (StringBowData b : values()) {
			bow.put(Integer.valueOf(b.getItem()), b);
		}
	}

	public static StringBowData forId(int item) {
		return bow.get(Integer.valueOf(item));
	}

	private StringBowData(int item, int product, int levelRequired, double experience) {
		this.item = item;
		this.product = product;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}

	public double getExperience() {
		return experience;
	}

	public int getItem() {
		return item;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getProduct() {
		return product;
	}
}
