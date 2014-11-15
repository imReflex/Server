package org.endeavor.game.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

public enum Craftable {
	LEATHERGLOVES(1741, 1059, 1, 13.800000000000001D, 1), LEATHERBOOTS(1741, 1061, 7, 16.25D, 1), LEATHERCOWL(1741,
			1167, 9, 18.5D, 1), LEATHERVAMBS(1741, 1063, 11, 22.0D, 1), LEATHERBODY(1741, 1129, 14, 25.0D, 1), LEATHERCHAPS(
			1741, 1095, 18, 27.0D, 1), COIF(1741, 1169, 38, 37.0D, 1), GREENVAMBS(1745, 1065, 57, 62.0D, 1), GREENCHAPS(
			1745, 1099, 60, 124.0D, 2), GREENBODY(1745, 1135, 63, 186.0D, 3), BLUEVAMBS(2505, 2487, 66, 70.0D, 1), BLUECHAPS(
			2505, 2493, 68, 140.0D, 2), BLUEBODY(2505, 2499, 71, 210.0D, 3), REDVAMBS(2507, 2489, 73, 78.0D, 1), REDCHAPS(
			2507, 2495, 75, 156.0D, 2), REDBODY(2507, 2501, 77, 234.0D, 3), BLACKVAMBS(2509, 2491, 79, 86.0D, 1), BLACKCHAPS(
			2509, 2497, 82, 172.0D, 2), BLACKBODY(2509, 2503, 84, 258.0D, 3);

	private int itemId;
	private int outcome;
	private int requiredLevel;
	private int requiredAmount;
	private double experience;
	private static Map<Integer, Craftable> craftables = new HashMap<Integer, Craftable>();

	private static Map<Integer, Craftable> craftableRewards = new HashMap<Integer, Craftable>();

	private Craftable(int itemId, int outcome, int requiredLevel, double experience, int requiredAmount) {
		this.itemId = itemId;
		this.outcome = outcome;
		this.requiredLevel = requiredLevel;
		this.experience = experience;
		this.requiredAmount = requiredAmount;
	}

	public int getItemId() {
		return itemId;
	}

	public int getOutcome() {
		return outcome;
	}

	public int getRequiredAmount() {
		return requiredAmount;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public double getExperience() {
		return experience;
	}

	public static final void declare() {
		for (Craftable craftable : values()) {
			craftables.put(Integer.valueOf(craftable.getItemId()), craftable);
		}
		for (Craftable craftable : values())
			craftableRewards.put(Integer.valueOf(craftable.getOutcome()), craftable);
	}

	public static Craftable forId(int id) {
		return craftables.get(Integer.valueOf(id));
	}

	public static Craftable forReward(int id) {
		return craftableRewards.get(Integer.valueOf(id));
	}
}
