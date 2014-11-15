package org.endeavor.game.entity.player;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.skill.cooking.CookingData;
import org.endeavor.game.content.skill.herblore.FinishedPotionData;
import org.endeavor.game.entity.item.Item;

public class ItemCheck {
	public static boolean hasAnyItem(Player p) {
		for (Item i : p.getInventory().getItems()) {
			if (i != null) {
				return true;
			}
		}

		for (Item i : p.getEquipment().getItems()) {
			if (i != null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets if the player is using a balmung
	 * @param p
	 * @return
	 */
	public static boolean isUsingBalmung(Player p) {
		return (p.getEquipment().getItems()[3] != null) && (p.getEquipment().getItems()[3].getId() == 15403);
	}

	/**
	 * Gets if the player is using a crossbow
	 * 
	 * @param p
	 *            The player using a crossbow
	 * @return
	 */
	public static boolean isUsingCrossbow(Player p) {
		Item weapon = p.getEquipment().getItems()[3];

		if (weapon != null) {
			int i = weapon.getId();
			return (i == 18357) || (i == 837) || (i == 4734) || (i == 9174) || (i == 9178) || (i == 9180)
					|| (i == 9182) || (i == 9184) || (i == 9185);
		}

		return false;
	}

	/**
	 * Gets if the item is a dyed whip
	 * 
	 * @param i
	 *            The item being checked
	 * @return
	 */
	public static final boolean isItemDyedWhip(Item i) {
		if (i != null) {
			return (i.getId() == 15442) || (i.getId() == 15443) || (i.getId() == 15441) || (i.getId() == 15444);
		}

		return false;
	}

	/**
	 * Gets if the player is wearing an anti-dragonfire shield
	 * 
	 * @param p
	 *            The player wearing the shield
	 * @return
	 */
	public static final boolean isWearingAntiDFireShield(Player p) {
		Item s = p.getEquipment().getItems()[5];

		return ((s != null) && (s.getId() == 11283)) || ((s != null) && (s.getId() == 1540));
	}

	public static boolean hasConsumables(Player p) {
		for (Item i : p.getInventory().getItems()) {
			if (i != null) {
				int id = i.getId();
				if ((GameDefinitionLoader.getFoodDefinition(id) != null)
						|| (GameDefinitionLoader.getPotionDefinition(id) != null)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean hasHerbloreIngredients(Player p) {
		for (Item i : p.getInventory().getItems()) {
			if (i != null) {
				for (FinishedPotionData k : FinishedPotionData.values()) {
					if (i.getId() == k.getItemNeeded()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean hasRawFood(Player p) {
		for (Item i : p.getInventory().getItems()) {
			if ((i != null) && (CookingData.forId(i.getId()) != null)) {
				return true;
			}

		}

		return false;
	}

	public static final boolean hasEquipmentOn(Player p) {
		for (Item i : p.getEquipment().getItems()) {
			if (i != null) {
				return true;
			}
		}

		return false;
	}

	public static boolean isDungeoneeringItem(int i) {
		for (int k : org.endeavor.game.content.minigames.dungeoneering.DungConstants.CHEST_LOOT_TABLE[1]) {
			if (i == k) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasDFireShield(Player p) {
		return (p.getEquipment().getItems()[5] != null) && (p.getEquipment().getItems()[5].getId() == 11283);
	}

	public static boolean hasBNeckAndObbyMaulCombo(Player p) {
		Item w = p.getEquipment().getItems()[3];
		Item n = p.getEquipment().getItems()[2];

		return (w != null) && (w.getId() == 6528) && (n != null) && (n.getId() == 11128);
	}

	public static boolean wearingFullVoidMelee(Player player) {
		int[] slots = { 0, 4, 7, 9 };
		int[] ids = { 11665, 8839, 8840, 8842 };
		Item[] equip = player.getEquipment().getItems();

		for (int i = 0; i < slots.length; i++) {
			if (equip[slots[i]] == null) {
				return false;
			}
			if (equip[slots[i]].getId() != ids[i]) {
				return false;
			}

		}

		return true;
	}

	public static boolean wearingFullVoidRanged(Player player) {
		int[] slots = { 0, 4, 7, 9 };
		int[] ids = { 11664, 8839, 8840, 8842 };
		Item[] equip = player.getEquipment().getItems();

		for (int i = 0; i < slots.length; i++) {
			if (equip[slots[i]] == null) {
				return false;
			}
			if (equip[slots[i]].getId() != ids[i]) {
				return false;
			}

		}

		return true;
	}

	public static boolean wearingFullVoidMagic(Player player) {
		int[] slots = { 0, 4, 7, 9 };
		int[] ids = { 11663, 8839, 8840, 8842 };
		Item[] equip = player.getEquipment().getItems();

		for (int i = 0; i < slots.length; i++) {
			if (equip[slots[i]] == null) {
				return false;
			}
			if (equip[slots[i]].getId() != ids[i]) {
				return false;
			}

		}

		return true;
	}

	public static boolean wearingFullBarrows(Player player, String check) {
		int[] slots = { 0, 4, 7, 3 };
		Item[] equip = player.getEquipment().getItems();

		for (int i = 0; i < slots.length; i++) {
			if (equip[slots[i]] == null) {
				return false;
			}
			if (!equip[slots[i]].getDefinition().getName().contains(check)) {
				return false;
			}

		}

		return true;
	}
}
