package org.endeavor.game.content.combat.impl;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.content.skill.slayer.Slayer;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Equipment;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;

public class AccuracyFormulas {
	public static final double[][] MELEE_PRAYER_MODIFIERS = { { 2.0D, 0.05D }, { 7.0D, 0.1D }, { 15.0D, 0.15D },
			{ 24.0D, 0.15D }, { 25.0D, 0.2D } };

	public static final double[][] MELEE_CURSE_PRAYER_MODIFIERS = { { 19.0D, 0.15D } };

	public static final double[][] RANGED_PRAYER_MODIFIERS = { { 3.0D, 0.05D }, { 11.0D, 0.1D }, { 19.0D, 0.15D } };

	public static final double[][] MAGIC_PRAYER_MODIFIERS = { { 4.0D, 0.05D }, { 12.0D, 0.1D }, { 20.0D, 0.15D } };

	public static final double[][] DEFENCE_PRAYER_MODIFIERS = { { 0.0D, 0.05D }, { 5.0D, 0.1D }, { 13.0D, 0.15D },
			{ 24.0D, 0.2D }, { 25.0D, 0.25D } };

	public static final double[][] DEFENCE_CURSE_PRAYER_MODIFIERS = { { 19.0D, 0.15D } };

	public static double getDefenderPrayerModifier(Entity e1, Entity e2, CombatTypes type) {
		if ((e2 != null) && (!e2.isNpc())) {
			Player pDefender = org.endeavor.game.entity.World.getPlayers()[e2.getIndex()];

			if ((pDefender != null)
					&& (pDefender.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES)
					&& (((pDefender.getPrayer().active(1)) && ((type == CombatTypes.MELEE) || (type == null)))
							|| ((pDefender.getPrayer().active(2)) && ((type == CombatTypes.RANGED) || (type == null))) || ((pDefender
							.getPrayer().active(3)) && ((type == CombatTypes.MAGIC) || (type == null))))) {
				return e2.hasAttackedConsecutively(e1, 25) ? 0.8D : 0.9D;
			}

		}

		return 1.0D;
	}

	public static int getAccuracy(Entity attacking, Entity defending, CombatTypes combatType) {
		Player pDefender = null;
		Player pAttacker = null;

		if (!defending.isNpc()) {
			pDefender = org.endeavor.game.entity.World.getPlayers()[defending.getIndex()];
		}

		if (!attacking.isNpc()) {
			pAttacker = org.endeavor.game.entity.World.getPlayers()[attacking.getIndex()];

			if ((pAttacker != null) && (ItemCheck.hasDFireShield(pAttacker))
					&& (pAttacker.getMagic().isDFireShieldEffect())) {
				return 100;
			}

		}

		int aLevel = 0;

		double aPBonus = 1.0D;
		int aSBonus = 0;

		double dPBonus = 1.0D;
		int dSBonus = 0;

		double vBonus = 1.0D;

		int dDefensive = (int) (defending.getLevels()[1] * getDefenderPrayerModifier(defending, attacking, null));

		int aBonus = 0;
		int dBonus = 0;

		if (pDefender != null) {
			if (pDefender.getController().equals(ARConstants.AR_CONTROLLER)) {
				dDefensive = 99;
			}

			if (pDefender.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
				for (int i = 0; i < DEFENCE_PRAYER_MODIFIERS.length; i++)
					if (pDefender.getPrayer().active((int) DEFENCE_PRAYER_MODIFIERS[i][0])) {
						dPBonus += DEFENCE_PRAYER_MODIFIERS[i][1];
						break;
					}
			} else {
				for (int i = 0; i < DEFENCE_CURSE_PRAYER_MODIFIERS.length; i++) {
					if (pDefender.getPrayer().active((int) DEFENCE_CURSE_PRAYER_MODIFIERS[i][0])) {
						dPBonus += DEFENCE_CURSE_PRAYER_MODIFIERS[i][1];

						if (MELEE_CURSE_PRAYER_MODIFIERS[i][0] != 19.0D)
							break;
						if (defending.getMaxLevels()[1] > 99) {
							dDefensive = (int) (dDefensive + 14.85D);
							break;
						}
						dDefensive = (int) (dDefensive + defending.getMaxLevels()[1] * 0.15D);

						break;
					}
				}
			}

			switch (pDefender.getEquipment().getAttackStyle()) {
			case CONTROLLED:
				dSBonus = 1;
				break;
			case DEFENSIVE:
				dSBonus = 3;
			default:
				break;
			}

		}

		if ((!defending.isNpc()) && (!attacking.isNpc())) {
			dDefensive = (int) (dDefensive * 0.86D);
		}

		switch (combatType) {
		case MAGIC:
			aLevel = (int) (attacking.getLevels()[6] * getDefenderPrayerModifier(attacking, defending, combatType));
			aBonus = attacking.getBonuses()[3];
			dBonus = defending.getBonuses()[8];

			if (defending.getMagicWeaknessMod() != 0.0D) {
				dDefensive = (int) (dDefensive * defending.getMagicWeaknessMod());
			}

			if (pAttacker != null) {
				if (pAttacker.getController().equals(ARConstants.AR_CONTROLLER)) {
					aLevel = 99;
				}

				Item helm = pAttacker.getEquipment().getItems()[0];

				if ((helm != null) && (helm.getId() == 15492) && (defending.isNpc())
						&& (pAttacker.getSlayer().hasTask())) {
					Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
					if ((m != null) && (Slayer.isSlayerTask(pAttacker, m))) {
						aLevel = (int) (aLevel + aLevel * 0.125D);
					}

				}

				if (ItemCheck.wearingFullVoidMagic(pAttacker)) {
					vBonus += 0.15D;
				}

				if (pAttacker.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
					for (int i = 0; i < MAGIC_PRAYER_MODIFIERS.length; i++) {
						if (pAttacker.getPrayer().active((int) MAGIC_PRAYER_MODIFIERS[i][0])) {
							aPBonus += MAGIC_PRAYER_MODIFIERS[i][1];
							break;
						}
					}
				}
			}

			break;
		case MELEE:
			aLevel = (int) (attacking.getLevels()[0] * getDefenderPrayerModifier(attacking, defending, combatType));

			aBonus = attacking.getBonuses()[1];

			if (defending.getMeleeWeaknessMod() != 0.0D) {
				dDefensive = (int) (dDefensive * defending.getMeleeWeaknessMod());
			}

			if (aBonus < attacking.getBonuses()[0]) {
				aBonus = attacking.getBonuses()[0];
			}

			if (aBonus < attacking.getBonuses()[2]) {
				aBonus = attacking.getBonuses()[2];
			}

			if ((pAttacker == null) || (!pAttacker.getMelee().isVeracEffectActive())) {
				dBonus = defending.getBonuses()[6];

				if (dBonus < defending.getBonuses()[5]) {
					dBonus = defending.getBonuses()[5];
				}

				if (dBonus < defending.getBonuses()[7]) {
					dBonus = defending.getBonuses()[7];
				}
			}

			if (pAttacker != null) {
				if (pAttacker.getController().equals(ARConstants.AR_CONTROLLER)) {
					aLevel = 99;
				}

				Item helm = pAttacker.getEquipment().getItems()[0];

				if (((helm != null) && (helm.getId() == 8921))
						|| ((helm != null) && (helm.getId() == 15492))
						|| ((helm != null) && (helm.getId() == 13263) && (defending.isNpc()) && (pAttacker.getSlayer()
								.hasTask()))) {
					Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
					if ((m != null) && (Slayer.isSlayerTask(pAttacker, m))) {
						aLevel = (int) (aLevel + aLevel * 0.125D);
					}

				}

				if (pAttacker.getEquipment().getAttackStyle() == Equipment.AttackStyles.ACCURATE)
					aSBonus = 3;
				else if (pAttacker.getEquipment().getAttackStyle() == Equipment.AttackStyles.DEFENSIVE) {
					aSBonus = 1;
				}

				if (ItemCheck.wearingFullVoidMelee(pAttacker)) {
					vBonus += 0.15D;
				}

				if (pAttacker.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
					for (int i = 0; i < MELEE_PRAYER_MODIFIERS.length; i++)
						if (pAttacker.getPrayer().active((int) MELEE_PRAYER_MODIFIERS[i][0])) {
							aPBonus += MELEE_PRAYER_MODIFIERS[i][1];
							break;
						}
				} else {
					for (int i = 0; i < MELEE_CURSE_PRAYER_MODIFIERS.length; i++) {
						if (pAttacker.getPrayer().active((int) MELEE_CURSE_PRAYER_MODIFIERS[i][0])) {
							aPBonus += MELEE_CURSE_PRAYER_MODIFIERS[i][1];

							if (MELEE_CURSE_PRAYER_MODIFIERS[i][0] != 19.0D)
								break;
							aLevel = (int) (aLevel + defending.getLevels()[0] * 0.15D);

							break;
						}
					}
				}
			}

			break;
		case RANGED:
			dBonus = defending.getBonuses()[9];
			aBonus = (int) attacking.getBonuses()[4];
			aLevel = (int) (attacking.getLevels()[4] * getDefenderPrayerModifier(attacking, defending, combatType));

			if (defending.getRangedWeaknessMod() != 0.0D) {
				dDefensive = (int) (dDefensive * defending.getRangedWeaknessMod());
			}

			if (pAttacker != null) {
				if (pAttacker.getController().equals(ARConstants.AR_CONTROLLER)) {
					aLevel = 99;
				}

				Item helm = pAttacker.getEquipment().getItems()[0];

				if ((helm != null) && (helm.getId() == 15492) && (defending.isNpc())
						&& (pAttacker.getSlayer().hasTask())) {
					Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
					if ((m != null) && (Slayer.isSlayerTask(pAttacker, m))) {
						aLevel = (int) (aLevel + aLevel * 0.125D);
					}

				}

				if (pAttacker.getEquipment().getAttackStyle() == Equipment.AttackStyles.ACCURATE)
					aSBonus = 3;
				else if (pAttacker.getEquipment().getAttackStyle() == Equipment.AttackStyles.DEFENSIVE) {
					aSBonus = 1;
				}

				if (ItemCheck.wearingFullVoidRanged(pAttacker)) {
					vBonus += 0.15D;
				}

				if (isDiamondBoltSpec(pAttacker)) {
					dBonus = (int) (defending.getBonuses()[9] * 0.65D);
				}

				if (pAttacker.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
					for (double i[] : RANGED_PRAYER_MODIFIERS) {
						if (pAttacker.getPrayer().active((int) i[0])) {
							aPBonus += i[1];
						}
					}
				}
			}
			break;
		}
		double eA = (int) (((int) (aLevel * aPBonus) + 8 + aSBonus) * vBonus);
		double eD;
		if (combatType == CombatTypes.MAGIC) {
			int m = defending.getLevels()[6];

			if ((pDefender != null) && (pDefender.getController().equals(ARConstants.AR_CONTROLLER))) {
				m = 99;
			}

			eD = (int) (m * 0.3D + dDefensive * dPBonus * 0.7D);
		} else {
			eD = (int) (dDefensive * dPBonus) + 8 + dSBonus;
		}

		double aRoll = eA * (1.0D + aBonus / 64.0D);

		if ((combatType == CombatTypes.MAGIC) && (!attacking.isNpc()) && (defending.isNpc()))
			aRoll = eA * (1.0D + aBonus / 30.0D);
		double dRoll;
		if ((!attacking.isNpc()) && (!defending.isNpc())) {
			if (combatType == CombatTypes.MAGIC) {
				dRoll = eD * (1.0D + dBonus / 67.0D);
			} else {
				dRoll = eD * (1.0D + dBonus / 55.0D);
			}
		} else {
			dRoll = eD * (1.0D + dBonus / 64.0D);
		}

		if ((pAttacker != null) && (combatType != CombatTypes.MAGIC) && (pAttacker.getSpecialAttack().isInitialized())) {
			aRoll *= (1.0D + getMeleeSpecialAttackMod(pAttacker));
		}

		if (aRoll < dRoll) {
			return (int) ((aRoll - 1.0D) / (dRoll * 2.0D) * 100.0D);
		}
		return (int) ((1.0D - (dRoll + 1.0D) / (2.0D * aRoll)) * 100.0D);
	}

	public static boolean isDiamondBoltSpec(Player player) {
		Item ammo = player.getEquipment().getItems()[13];
		return (player.getSpecialAttack().isInitialized()) && (ammo != null) && (ammo.getId() == 9243);
	}

	public static double getMeleeSpecialAttackMod(Player player) {
		Item weapon = player.getEquipment().getItems()[3];

		if (weapon == null) {
			return 0.0D;
		}

		switch (weapon.getId()) {
		case 11694://all godswords
		case 11696:
		case 11698:
		case 11700:
			return 0.2;
		
		case 14484:
			return 0;
		case 1215:
		case 5698:
			return 0.1D;
		case 861:
			return 0.1D;
		case 4151:
			return 0.1D;
		case 10887:
			return 1.0D;
		case 1305:
			return 1.1D;
		}

		return 0.0D;
	}
}
