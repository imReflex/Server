package org.endeavor.game.entity.player;

import org.endeavor.engine.definitions.ItemBonusDefinition;
import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class EquipmentConstants {
	
	public static final int SIZE = 14;
	public static final int HELM_SLOT = 0;
	public static final int CAPE_SLOT = 1;
	public static final int NECKLACE_SLOT = 2;
	public static final int WEAPON_SLOT = 3;
	public static final int TORSO_SLOT = 4;
	public static final int SHIELD_SLOT = 5;
	public static final int LEGS_SLOT = 7;
	public static final int GLOVES_SLOT = 9;
	public static final int BOOTS_SLOT = 10;
	public static final int RING_SLOT = 12;
	public static final int AMMO_SLOT = 13;
	public static final int AURA_SLOT = 14;
	
	public static final String[] SLOT_NAMES = { "helm", "cape", "amulet", "weapon", "torso", "shield", "none", "legs",
			"none", "gloves", "boots", "none", "ring", "ammo" };

	public static final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush",
			"Magic", "Range", "Strength", "Prayer" };
	public static final int ATTACK_STAB = 0;//0
	public static final int ATTACK_SLASH = 1;//1
	public static final int ATTACK_CRUSH = 2;//2
	public static final int ATTACK_MAGIC = 3;//3
	public static final int ATTACK_RANGED = 4;//4
	public static final int DEFENCE_STAB = 5;//5
	public static final int DEFENCE_SLASH = 6;//6
	public static final int DEFENCE_CRUSH = 7;//7
	public static final int DEFENCE_MAGIC = 8;//8
	public static final int DEFENCE_RANGED = 9;//9
	public static final int DEFENCE_SUMMONING = 13;//10
	public static final int STRENGTH = 10;//11
	public static final int PRAYER = 11;//12
	public static final int RANGED_STRENGTH = 12;//13
	public static final int MAGIC_STRENGTH = 14;//14
	public static final int ABSORB_MELEE = 15;//15
	public static final int ABSORB_MAGIC = 16;//16
	public static final int ABSORB_RANGED = 17;//17
	private static final byte[] FULL_BODY = new byte[PlayerConstants.MAX_ITEM_COUNT];
	private static final byte[] FULL_HELM = new byte[PlayerConstants.MAX_ITEM_COUNT];
	private static final byte[] FULL_MASK = new byte[PlayerConstants.MAX_ITEM_COUNT];
	private static final byte[] IS_METAL = new byte[PlayerConstants.MAX_ITEM_COUNT];

	private static final int[] FULL_BODY_IDS = { 13624, 13614, 577, 9674, 6107, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 2583, 2584, 2591, 2599,
			2607, 2615, 2623, 2653, 2661, 2669, 3481, 4720, 4728, 4728, 4749, 4892, 4894, 4895, 4916, 4917, 4918, 4919,
			4964, 4965, 4966, 4967, 6617, 10348, 14479, 10551, 4736, 4940, 4941, 4942, 4943, 11724, 544, 4091, 4101,
			4111, 4991, 4990, 4989, 4988, 4758, 4712, 4868, 4869, 4870, 4871, 4757, 8839, 3058, 6654, 6180, 6184,
			17259, 5575, 3140, 10748, 14595, 11020, 10338, 10400, 10404, 10408, 10412, 10416, 10420, 13619, 6916,
			13887, 13884, 13858, 13870, 13889, 13886, 13860, 13872, 14497, 14601, 15600, 15606, 15612, 15618, 17193,9944,
			14114};

	private static final int[] FULL_HELM_IDS = { 1153, 1155, 1157, 1159, 1161, 1163, 1165, 2587, 2595, 2605, 2613,
			2619, 2627, 2657, 2665, 2673, 3486, 6623, 10350, 11335, 13896, 13898, 4724, 4904, 4905, 4906, 4907, 4716,
			4880, 4881, 4882, 4883, 4708, 4856, 4857, 4858, 4859, 4745, 4952, 4953, 4954, 4955, 4732, 4928, 4929, 4930,
			4931, 7534, 11718, 6109, 3748, 3749, 3751, 3753, 3755, 4089, 4099, 4109, 11663, 11664, 11665, 4753, 4976,
			4977, 4978, 4979, 4709, 4856, 4857, 4858, 4859, 10828, 3057, 7594, 6188, 1149, 10548, 10547, 16711, 5574,
			4502, 11021, 10334, 10392, 10398, 13876, 
			15492, 10589, 1053, 1055, 1057, 17061, 13263, 14116, 1167,
			12673, 12675, 12677, 12679, 12681};

	private static final int[] FULL_MASK_IDS = { 1153, 1155, 1157, 1159, 1161, 1163, 1165, 2587, 2595, 2605, 2613,
			2619, 2627, 2657, 2665, 2673, 3486, 6623, 10350, 11335, 4979, 4978, 4977, 4976, 4753, 3057, 7594, 6188,
			5574, 15492, 1053, 1055, 1057, 13263,  };
	public static final int DEFAULT_SIDEBAR = 5855;
	public static final int DEFAULT_SIDEBAR_TEXT = 5857;
	public static final int ATTACK_STYLE_CONFIG_ID = 43;
	public static final int ANTI_DRAGON_SHIELD_ID = 1540;
	public static final int DRAGONFIRE_SHIELD_ID = 11283;

	public static final void declare() {
		System.out.println("Equipment constants set.");

		for (int i = 0; i < FULL_BODY_IDS.length; i++) {
			FULL_BODY[FULL_BODY_IDS[i]] = 1;
		}

		for (int i = 0; i < FULL_HELM_IDS.length; i++) {
			FULL_HELM[FULL_HELM_IDS[i]] = 1;
		}

		for (int i = 0; i < FULL_MASK_IDS.length; i++) {
			FULL_MASK[FULL_MASK_IDS[i]] = 1;
		}

		FULL_HELM[1050] = 2;
		FULL_HELM[14499] = 2;
		FULL_HELM[6858] = 2;

		for (int i = 0; i < 20145; i++) {
			ItemBonusDefinition def1 = GameDefinitionLoader.getItemBonusDefinition(i);
			ItemDefinition def2 = GameDefinitionLoader.getItemDef(i);

			if ((def1 != null) && (def2 != null)) {
				if ((def2.getName().contains("beret")) || (def2.getName().contains("cavalier"))
						|| (def2.getName().contains("headband"))) {
					FULL_HELM[i] = 2;
				}

				if ((def2.getName() != null) && (def2.getName().contains("hood"))
						&& (!def2.getName().contains("Robin")) || def2.getName().contains("mask")) {
					FULL_HELM[i] = 1;
				}

				for (int k = 0; k < def1.getBonuses().length; k++)
					if ((def1.getBonuses()[k] > 0) && (!def2.getName().contains("bow"))
							&& (!def2.getName().contains("dart")) && (!def2.getName().contains("knife"))
							&& (!def2.getName().contains("thrown axe")) && (!def2.getName().contains("throwing axe"))
							&& (!def2.getName().contains("d'hide")) && (!def2.getName().contains("leather"))) {
						IS_METAL[i] = 1;
						break;
					}
			}
		}
	}

	public static boolean isForceNewHair(int i) {
		return FULL_HELM[i] == 2;
	}

	public static boolean isWearingMetal(Player player) {
		Item[] equip = player.getEquipment().getItems();

		if ((equip[4] != null) && (IS_METAL[equip[4].getId()] == 1)) {
			return true;
		}

		if ((equip[7] != null) && (IS_METAL[equip[7].getId()] == 1)) {
			return true;
		}

		if ((equip[5] != null) && (IS_METAL[equip[5].getId()] == 1)) {
			return true;
		}

		return false;
	}

	public static void sendSoundForEquipSlot(Player player, int slot, int item) {
		int sound = 326;

		if (slot == 10) {
			sound = 1343;
		}

		if (IS_METAL[item] == 1)
			switch (slot) {
			case 0:
				sound = 1342;
				break;
			case 4:
				sound = 1346;
				break;
			case 7:
				sound = 1346;
				break;
			case 3:
				sound = 1344;
			case 1:
			case 2:
			case 5:
			case 6:
			}
		if (sound > 0)
			player.getClient().queueOutgoingPacket(new SendSound(sound, 10, 0));
	}

	public static boolean isFullBody(int id) {
		return FULL_BODY[id] == 1/* || GameDefinitionLoader.getEquipmentDefinition(id).getType() == 6*/;
	}

	public static boolean isFullHelm(int id) {
		return FULL_HELM[id] == 1/* || GameDefinitionLoader.getEquipmentDefinition(id).getType() == 8*/;
	}

	public static boolean isFullMask(int id) {
		return FULL_MASK[id] == 1;
	}

	public static int getShieldBlockAnimation(int id) {
		switch (id) {
		case 3842:
		case 3844:
		case 3840:
			return 424;
		
		case 2997:
		case 8844:
		case 8845:
		case 8846:
		case 8847:
		case 8848:
		case 8849:
		case 8850:
		case 18340:
		case 18653:
		case 17273:
			return 4177;
		}

		return 1156;
	}

	public static int getAttackStyleConfigId(int id, Equipment.AttackStyles attackStyle) {
		if (id == 0) {
			switch (attackStyle) {
			case ACCURATE:
				return 1;
			case AGGRESSIVE:
				return 2;
			case CONTROLLED:
				return -1;
			case DEFENSIVE:
				return 0;
			}
		}

		switch (getWeaponAttackStyle(id)) {
		case THREE_CONTROLLED:
			switch (attackStyle) {
			case ACCURATE:
				return 0;
			case AGGRESSIVE:
				break;
			case CONTROLLED:
				return 1;
			case DEFENSIVE:
				return 2;
			}

			break;
		case THREE_STRENGTH:
			switch (attackStyle) {
			case ACCURATE:
				return 0;
			case AGGRESSIVE:
				return 1;
			case CONTROLLED:
				break;
			case DEFENSIVE:
				return 2;
			}

			break;
		case DEFAULT:
			switch (attackStyle) {
			case ACCURATE:
				return 0;
			case AGGRESSIVE:
				return 1;
			case CONTROLLED:
				return 2;
			case DEFENSIVE:
				return 3;
			}

			break;
		}

		return 0;
	}

	public static WeaponAttackStyles getWeaponAttackStyle(int id) {
		int interfaceId = Item.getWeaponDefinition(id) != null ? Item.getWeaponDefinition(id).getSidebarId() : 5855;

		switch (interfaceId) {
		case 12290:
			return WeaponAttackStyles.THREE_CONTROLLED;
		case 328:
		case 425:
		case 1764:
		case 4446:
		case 5855:
			return WeaponAttackStyles.THREE_STRENGTH;
		}
		return WeaponAttackStyles.DEFAULT;
	}

	public static CombatTypes getCombatTypeForWeapon(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		if ((weapon != null) && (weapon.getWeaponDefinition() != null)) {
			return weapon.getWeaponDefinition().getType();
		}

		return CombatTypes.MELEE;
	}

	public static boolean clickAttackStyleButtons(Player player, int buttonId) {
		switch (buttonId) {
		case 1080:
			if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
				return true;
			}
			
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.ACCURATE);
			player.updateCombatType();
			player.getMagic().getSpellCasting().disableAutocast();
			return true;
		case 1079:
			if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
				return true;
			}
			
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.AGGRESSIVE);
			player.updateCombatType();
			player.getMagic().getSpellCasting().disableAutocast();
			return true;
		case 1078:
			if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
				return true;
			}
			
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.DEFENSIVE);
			player.updateCombatType();
			player.getMagic().getSpellCasting().disableAutocast();
			return true;
		case 1177:
		case 6168:
		case 6236:
		case 8234:
		case 14218:
		case 17102:
		case 18077:
		case 30088:
		case 48010:
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.ACCURATE);
			player.updateCombatType();
			return true;
		case 1176:
		case 6171:
		case 6235:
		case 8237:
		case 14221:
		case 17101:
		case 18080:
		case 22230:
		case 30091:
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.AGGRESSIVE);
			player.updateCombatType();
			return true;
		case 6170:
		case 8236:
		case 14220:
		case 18079:
		case 30090:
		case 48009:
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.CONTROLLED);
			player.updateCombatType();
			return true;
		case 1175:
		case 6169:
		case 6234:
		case 8235:
		case 14219:
		case 17100:
		case 18078:
		case 22229:
		case 30089:
		case 48008:
		case 22228:
			player.getEquipment().setAttackStyle(Equipment.AttackStyles.DEFENSIVE);
			player.updateCombatType();
			return true;
		}

		return false;
	}

	public static final int getTextIdForInterface(int interfaceId) {
		switch (interfaceId) {
		case 7762:
			return 7765;
		case 12290:
			return 12293;
		case 1698:
			return 1701;
		case 2276:
			return 2279;
		case 1764:
			return 1767;
		case 328:
			return 355;
		case 4446:
			return 4449;
		case 4679:
			return 4682;
		case 425:
			return 428;
		case 3796:
			return 3799;
		case 8460:
			return 8463;
		case 5570:
			return 5573;
		}
		return 5857;
	}

	public static enum WeaponAttackStyles {
		DEFAULT, THREE_CONTROLLED, THREE_STRENGTH;
	}
}
