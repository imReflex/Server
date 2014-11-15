package org.endeavor.game.content.minigames.duelarena;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.WeaponDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class DuelingConstants {
	public static final int DUEL_SCREEN_1 = 6575;
	public static final int DUEL_SCREEN_2 = 6412;
	public static final int DUELING_WITH_ID = 6671;
	public static final int ACCEPTED_SCREEN_1_ID = 6684;
	public static final int ACCEPTED_SCREEN_2_ID = 6571;
	public static final int DUEL_VICTORY_INTERFACE = 6733;
	public static final int DUEL_VICTORY_COMBAT_LEVEL_ID = 6839;
	public static final int DUEL_VICTORY_DEFEATED_NAME_ID = 6840;
	public static final int RULES_CONFIG_ID = 286;
	public static final int LOCAL_DUELING_CONTAINER_ID = 6669;
	public static final int OTHER_DUELING_CONTAINER_ID = 6670;
	public static final int FORFEIT_TRAP_DOOR = 3203;
	public static final Location[] RESPAWN_LOCATIONS = { new Location(3363, 3276),
			new Location(3369, 3275), new Location(3373, 3275), new Location(3377, 3275), new Location(3377, 3275),
			new Location(3370, 3268), new Location(3363, 3268) };
	public static final int WHIP_AND_DDS_ONLY = 0;
	public static final int NO_MOVEMENT = 1;
	public static final int NO_RANGED = 2;
	public static final int NO_MELEE = 3;
	public static final int NO_MAGIC = 4;
	public static final int NO_DRINKS = 5;
	public static final int NO_FOOD = 6;
	public static final int NO_PRAYER = 7;
	public static final int OBSTACLES = 8;
	public static final int NO_ARMOUR = 9;
	public static final int NO_SP_ATTACK = 10;
	public static final int NO_HELMET = 11;
	public static final int NO_CAPE = 12;
	public static final int NO_AMULET = 13;
	public static final int NO_WEAPON = 14;
	public static final int NO_TORSO = 15;
	public static final int NO_SHIELD = 16;
	public static final int NO_LEGS = 17;
	public static final int NO_GLOVES = 18;
	public static final int NO_BOOTS = 19;
	public static final int NO_RING = 20;
	public static final int NO_AMMO = 21;
	public static final int[] DUEL_RULE_IDS = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536,
			131072, 262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728 };
	public static final int LOCAL_STAKED_ITEMS_STRING = 6516;
	public static final int OTHER_STAKED_ITEMS_STRING = 6517;
	public static final int[] BEFORE_THE_DUEL_STRING_IDS = { 8250, 8238, 8239, 8240, 8241 };

	public static final int[] DURING_THE_DUEL_STRING_IDS = { 8242, 8243, 8244, 8245, 8246, 8247, 8248, 8249, 8251,
			8252, 8253 };

	private static final Map<Integer, Boolean> funWeapons = new HashMap<Integer, Boolean>();

	public static final String[] FUN_WEAPON_NAMES = { "flowers", "rubber chicken", "easter carrot", "mouse toy",
			"scythe" };

	public static final void declare() {
		for (int i = 0; i < 20145; i++) {
			WeaponDefinition wep = GameDefinitionLoader.getWeaponDefinition(i);
			if (wep != null) {
				String n = GameDefinitionLoader.getItemDef(i).getName().toLowerCase();

				for (int k = 0; k < FUN_WEAPON_NAMES.length; k++)
					if (n.equals(FUN_WEAPON_NAMES[k]))
						funWeapons.put(Integer.valueOf(i), Boolean.valueOf(true));
			}
		}
	}

	public static final boolean isFunWeapon(Item item) {
		if (item == null) {
			return false;
		}

		if (funWeapons.get(Integer.valueOf(item.getId())) == null) {
			return false;
		}

		return funWeapons.get(Integer.valueOf(item.getId())).booleanValue();
	}

	public static final boolean hasFunWeapon(Player p) {
		Item weapon = p.getEquipment().getItems()[3];

		if (isFunWeapon(weapon)) {
			return true;
		}

		Item[] inv = p.getInventory().getItems();

		for (int i = 0; i < inv.length; i++) {
			if ((inv[i] != null) && (funWeapons.get(Integer.valueOf(inv[i].getId())) != null)) {
				if (funWeapons.get(Integer.valueOf(inv[i].getId())).booleanValue()) {
					return true;
				}
			}
		}
		return false;
	}

	public static final String[] getBeforeDuelStringUpdates(boolean[] rules) {
		String[] strings = new String[5];

		strings[0] = "Boosted stats will be restored.";
		strings[1] = "Existing prayers will be stopped.";

		for (int i = 11; i < 21; i++) {
			if (rules[i]) {
				strings[2] = "Some worn items will be taken off.";
				break;
			}
		}

		return strings;
	}

	public static final String[] getDuringDuelStringUpdates(boolean[] rules) {
		String[] strings = new String[11];

		int index = 0;

		if (rules[0]) {
			strings[index] = "You can only use a Whip or DDS.";
			index++;
		}

		if (rules[1]) {
			strings[index] = "You cannot move.";
			index++;
		}

		if (rules[2]) {
			strings[index] = "You cannot use Ranged attacks.";
			index++;
		}

		if (rules[3]) {
			strings[index] = "You cannot use Melee attacks.";
			index++;
		}

		if (rules[4]) {
			strings[index] = "You cannot use Magic attacks.";
			index++;
		}

		if (rules[5]) {
			strings[index] = "You cannot use drinks.";
			index++;
		}

		if (rules[6]) {
			strings[index] = "You cannot use food.";
			index++;
		}

		if (rules[7]) {
			strings[index] = "You cannot use prayer.";
			index++;
		}

		if (rules[8]) {
			strings[index] = "There will be obstacles.";
			index++;
		}

		if (rules[9]) {
			strings[index] = "You cannot use armour or 2h weapons.";
			index++;
		}

		if (rules[10]) {
			strings[index] = "You cannot use special attacks.";
			index++;
		}

		if (rules[16]) {
			strings[index] = "You can't use 2H weapons such as bows.";
			index++;
		}

		return strings;
	}

	public static final String getStakedItemsToString(Item[] staked, int count) {
		if (count == 0) {
			return "Absolutely nothing!";
		}

		String s = "";

		boolean altParse = false;

		if (count > 14) {
			altParse = true;
		}

		for (int i = 0; i < staked.length; i++) {
			if (staked[i] == null) {
				break;
			}
			s = s + staked[i].getDefinition().getName() + " x " + formatAmount(staked[i].getAmount())
					+ ((!altParse) || ((altParse) && ((i + 1) % 2 == 0)) ? "\\n" : " ");
		}

		return s;
	}

	public static final String formatAmount(int amount) {
		if (amount >= 10000000)
			return "@gre@" + amount / 1000000 + " million @whi@(" + amount + ")";
		if (amount >= 100000) {
			return "@cya@" + amount / 1000 + "K @whi@(" + amount + ")";
		}
		return "" + amount;
	}

	public static void updateToRemove(Dueling d, int rule, boolean toggle) {
		switch (rule) {
		case 11:
			d.appendToRemove(0, toggle);
			break;
		case 12:
			d.appendToRemove(1, toggle);
			break;
		case 13:
			d.appendToRemove(2, toggle);
			break;
		case 14:
			d.appendToRemove(3, toggle);
			break;
		case 15:
			d.appendToRemove(4, toggle);
			break;
		case 16:
			d.appendToRemove(5, toggle);
			break;
		case 17:
			d.appendToRemove(7, toggle);
			break;
		case 18:
			d.appendToRemove(9, toggle);
			break;
		case 19:
			d.appendToRemove(10, toggle);
			break;
		case 20:
			d.appendToRemove(12, toggle);
			break;
		case 21:
			d.appendToRemove(13, toggle);
		}
	}

	public static final boolean clickDuelButton(Player p, int id) {
		switch (id) {
		case 25120:
		case 26018:
			p.getDueling().accept();
			return true;
		case 26069:
			p.getDueling().toggleRule(2);
			return true;
		case 26070:
			p.getDueling().toggleRule(3);
			return true;
		case 26071:
			p.getDueling().toggleRule(4);
			return true;
		case 30136:
			p.getDueling().toggleRule(10);
			return true;
		case 2158:
			p.getDueling().toggleRule(9);
			return true;
		case 26065:
			p.getDueling().toggleRule(0);
			return true;
		case 26072:
			p.getDueling().toggleRule(5);
			return true;
		case 26073:
			p.getDueling().toggleRule(6);
			return true;
		case 26074:
			p.getDueling().toggleRule(7);
			return true;
		case 26066:
			p.getDueling().toggleRule(1);
			return true;
		case 26076:
			p.getDueling().toggleRule(8);
			return true;
		}
		return false;
	}
}
