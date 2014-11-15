package org.endeavor.game.content.combat.impl;

import org.endeavor.engine.definitions.RangedWeaponDefinition;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.magic.spells.Charge;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.content.skill.prayer.impl.CursesPrayerBook;
import org.endeavor.game.content.skill.slayer.Slayer;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.Equipment.AttackStyles;

public class MaxHitFormulas {
	public static final double[][] MELEE_PRAYER_MODIFIERS = { { 1.0D, 0.05D }, { 6.0D, 0.1D }, { 14.0D, 0.15D },
			{ 24.0D, 0.18D }, { 25.0D, 0.23D } };

	public static final double[][] MELEE_CURSE_PRAYER_MODIFIERS = { { 19.0D, 0.23D } };

	public static final double[][] RANGED_PRAYER_MODIFIERS = { { 3.0D, 0.05D }, { 11.0D, 0.1D }, { 19.0D, 0.15D } };

	public static int getMeleeMaxHit(Player player) {
		double b = 1.0;
		int a = player.getLevels()[SkillConstants.STRENGTH];
		Entity defending = player.getCombat().getAttacking();
		
		if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
			a = 99;
		}
		
		Item helm = player.getEquipment().getItems()[0];

		if (((helm != null) && (helm.getId() == 8921)) || ((helm != null) && (helm.getId() == 15492))
				|| ((helm != null) && (helm.getId() == 13263) && (defending.isNpc()) && (player.getSlayer().hasTask()))) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
			if ((m != null) && (Slayer.isSlayerTask(player, m))) {
				b += 0.125;
			}

		}
		
		if (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT] != null) {
			switch (player.getEquipment().getItems()[EquipmentConstants.WEAPON_SLOT].getId()) {
			case 4718:
			case 4886:
			case 4887:
			case 4888:
			case 4889:
				if (ItemCheck.wearingFullBarrows(player, "Dharok")) {
					int maximumHitpoints = player.getSkill().getLevelForExperience(SkillConstants.HITPOINTS, player.getSkill().getExperience()[SkillConstants.HITPOINTS]);
					int currentHitpoints = player.getSkill().getLevels()[SkillConstants.HITPOINTS];
					double dharokEffect = ((maximumHitpoints - currentHitpoints) * 0.01) + 1;
					b *= dharokEffect;
				}
			}
		}

		if ((ItemCheck.isUsingBalmung(player)) && (defending.isNpc())) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
			if ((m != null) && (MobConstants.isDagannothKing(m))) {
				b += 0.25;
			}
		}
		
		if ((defending != null) && (!defending.isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[defending.getIndex()];

			if ((p != null) && (p.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES)
					&& (player.getPrayer().active(1))) {
				a = (int) (a * (defending.hasAttackedConsecutively(player, 25) ? 0.8D : 0.9D));
			}

		}
		
		/**
		 * Prayer bonuses
		 */
		if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
			/**
			 * Default
			 */
			for (int i = 0; i < MELEE_PRAYER_MODIFIERS.length; i++)
				if (player.getPrayer().active((int) MELEE_PRAYER_MODIFIERS[i][0])) {
					b += MELEE_PRAYER_MODIFIERS[i][1];
					break;
				}
		} else {
			/**
			 * Curses
			 */
			for (int i = 0; i < MELEE_CURSE_PRAYER_MODIFIERS.length; i++) {
				if (player.getPrayer().active((int) MELEE_CURSE_PRAYER_MODIFIERS[i][0])) {
					b += MELEE_CURSE_PRAYER_MODIFIERS[i][1];

					
					/**
					 * Turmoil
					 */
					if (MELEE_CURSE_PRAYER_MODIFIERS[i][0] == CursesPrayerBook.TURMOIL) {
					
						if (player.getCombat().getAttacking() == null) {
							break;
						}
						
						if (player.getCombat().getAttacking().getMaxLevels()[2] > 99) {
							a = (int) (a + 9.9D);
							break;
						}
						
						a = (int) (a + (player.getCombat().getAttacking().getMaxLevels()[2] * 0.1));
					}

					break;
				}
			}
		}
		
		int c = (int) ((a * b) + 8);
		
		if (player.getEquipment().getAttackStyle() == AttackStyles.AGGRESSIVE) {
			c += 3;
		} else if (player.getEquipment().getAttackStyle() == AttackStyles.CONTROLLED) {
			c += 1;
		}
		
		/*if (player.getPrayer().getPrayerBookType() != PrayerBook.PrayerBookType.DEFAULT
				&& player.getPrayer().active(CursesPrayerBook.TURMOIL)) {
			affectiveStrength += 9;
		}*/
		
		if (ItemCheck.wearingFullVoidMelee(player)) {
			c = (int) (c * 1.3);
		}
		
		int d = player.getBonuses()[EquipmentConstants.STRENGTH];
		
		int baseMaxHit = 5 + c + c*d/64;
		
		baseMaxHit = (int) (baseMaxHit * getMeleeSpecialModifier(player));
		
		return baseMaxHit / 10;
	}
	
	/*
	 double pBonus = 1.0D;
		int sBonus = 0;

		int str = player.getSkill().getLevels()[2];
		Entity defending = player.getCombat().getAttacking();

		if ((defending != null) && (!defending.isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[defending.getIndex()];

			if ((p != null) && (p.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES)
					&& (player.getPrayer().active(1))) {
				str = (int) (str * (defending.hasAttackedConsecutively(player, 25) ? 0.8D : 0.9D));
			}

		}

		if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
			str = 99;
		}

		if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
			for (int i = 0; i < MELEE_PRAYER_MODIFIERS.length; i++)
				if (player.getPrayer().active((int) MELEE_PRAYER_MODIFIERS[i][0])) {
					pBonus += MELEE_PRAYER_MODIFIERS[i][1];
					break;
				}
		} else {
			for (int i = 0; i < MELEE_CURSE_PRAYER_MODIFIERS.length; i++) {
				if (player.getPrayer().active((int) MELEE_CURSE_PRAYER_MODIFIERS[i][0])) {
					pBonus += MELEE_CURSE_PRAYER_MODIFIERS[i][1];

					if (MELEE_CURSE_PRAYER_MODIFIERS[i][0] != 19.0D)
						break;
					if ((player.getCombat().getAttacking() != null)
							&& (player.getCombat().getAttacking().getMaxLevels()[2] > 99)) {
						str = (int) (str + 9.9D);
						break;
					}
					if (player.getCombat().getAttacking() == null)
						break;
					str = (int) (str + player.getCombat().getAttacking().getMaxLevels()[2] * 0.1D);

					break;
				}
			}
		}

		switch (player.getEquipment().getAttackStyle()) {
		case AGGRESSIVE:
			sBonus = 3;
			break;
		case CONTROLLED:
			sBonus = 1;
		case ACCURATE:
			break;
		case DEFENSIVE:
			break;
		default:
			break;
		}

		double eS = 8 + (int) (str * pBonus) + sBonus;

		if (ItemCheck.wearingFullVoidMelee(player)) {
			eS = (int) (eS * 1.1D);
		}

		double base = 5.0D + eS * (1.0D + player.getBonuses()[10] / 64.0D);

		if (player.getSpecialAttack().isInitialized()) {
			base = (int) (base * getMeleeSpecialModifier(player));
		}

		Item helm = player.getEquipment().getItems()[0];

		if (((helm != null) && (helm.getId() == 8921)) || ((helm != null) && (helm.getId() == 15492))
				|| ((helm != null) && (helm.getId() == 13263) && (defending.isNpc()) && (player.getSlayer().hasTask()))) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
			if ((m != null) && (Slayer.isSlayerTask(player, m))) {
				base += base * 0.125D;
			}

		}

		if ((ItemCheck.isUsingBalmung(player)) && (defending.isNpc())) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[defending.getIndex()];
			if ((m != null) && (MobConstants.isDagannothKing(m))) {
				base += base * 0.25D;
			}

		}

		return (int) base / 10;
	 */

	public static int getRangedMaxHit(Player player) {
		double pBonus = 1.0D;
		int vBonus = 0;
		int sBonus = 0;

		if (ItemCheck.wearingFullVoidRanged(player)) {
			vBonus = (int) (player.getSkill().getLevels()[4] / 5 + 1.8D);
		}

		switch (player.getEquipment().getAttackStyle()) {
		case ACCURATE:
			sBonus = 3;
			break;
		case DEFENSIVE:
			sBonus = 1;
			break;
		default:
			break;
		}
		if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
			for (int i = 0; i < RANGED_PRAYER_MODIFIERS.length; i++) {
				if (player.getPrayer().active((int) RANGED_PRAYER_MODIFIERS[i][0])) {
					pBonus += RANGED_PRAYER_MODIFIERS[i][1];
					break;
				}
			}
		}

		int str = player.getSkill().getLevels()[4];

		if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
			str = 99;
		}

		double eS = (int) (str * pBonus + sBonus + vBonus);

		int rngStr = getEffectiveRangedStrength(player);

		Entity defending = player.getCombat().getAttacking();

		if ((defending != null) && (!defending.isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[defending.getIndex()];

			if ((p != null) && (p.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES)
					&& (player.getPrayer().active(2))) {
				rngStr = (int) (rngStr * (defending.hasAttackedConsecutively(player, 25) ? 0.8D : 0.9D));
			}

		}

		double base = 5.0D + (eS + 8.0D) * (rngStr + 64) / 64.0D;

		if (player.getSpecialAttack().isInitialized()) {
			base = (int) (base * getRangedSpecialModifier(player));
		}

		Item helm = player.getEquipment().getItems()[0];

		if ((helm != null) && (helm.getId() == 15492) && (player.getCombat().getAttacking().isNpc())
				&& (player.getSlayer().hasTask())) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[player.getCombat().getAttacking().getIndex()];
			if ((m != null) && (Slayer.isSlayerTask(player, m))) {
				base += base * 0.125D;
			}

		}

		return (int) base / 10;
	}

	public static int getMagicMaxHit(Player player) {
		if (player.getMagic().isDFireShieldEffect()) {
			return 28;
		}

		if (player.getMagic().getSpellCasting().getCurrentSpellId() == -1) {
			return 0;
		}

		int spellId = player.getMagic().getSpellCasting().getCurrentSpellId();

		int base = player.getMagic().getSpellCasting().getDefinition(spellId).getBaseMaxHit();

		double mod = 1.0D;

		Item gloves = player.getEquipment().getItems()[9];
		Item weapon = player.getEquipment().getItems()[3];
		Item necklace = player.getEquipment().getItems()[2];

		if (weapon != null) {
			switch (weapon.getId()) {
			case 2415:
			case 2416:
			case 2417:
			case 4675:
			case 4710:
			case 4862:
			case 4863:
			case 4864:
			case 4865:
			case 6914:
			case 8841:
			case 13867:
			case 13869:
				mod += 0.1D;
				break;
			case 15486:
				mod += 0.15D;
				break;
			case 18355:
				mod += 0.2D;
			}

		}

		if (necklace != null) {
			switch (necklace.getId()) {
			case 18333:
				mod += 0.05D;
				break;
			case 18334:
				mod += 0.1D;
				break;
			case 18335:
				mod += 0.15D;
			}

		}

		if (gloves != null) {
			switch (gloves.getId()) {
			case 11079:
			case 11081:
			case 11083:
				mod += 0.2D;
			case 11080:
			case 11082:
			}
		}
		Item helm = player.getEquipment().getItems()[0];

		if ((helm != null) && (helm.getId() == 15492) && (player.getCombat().getAttacking().isNpc())
				&& (player.getSlayer().hasTask())) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[player.getCombat().getAttacking().getIndex()];
			if ((m != null) && (Slayer.isSlayerTask(player, m))) {
				mod += 0.125D;
			}

		}

		if ((spellId >= 1190) && (spellId <= 1192) && (Charge.isChargeActive(player))) {
			mod += 0.2D;
		}

		Entity defending = player.getCombat().getAttacking();

		if ((defending != null) && (!defending.isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[defending.getIndex()];

			if ((p != null) && (p.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.CURSES)
					&& (player.getPrayer().active(3))) {
				base = (int) (base * (defending.hasAttackedConsecutively(player, 25) ? 0.8D : 0.9D));
			}

		}

		return (int) (base * mod);
	}

	public static int getEffectiveRangedStrength(Player player) {
		Item weapon = player.getEquipment().getItems()[3];

		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return 0;
		}

		int rStr = player.getBonuses()[12];

		if ((weapon.getRangedDefinition().getType() == RangedWeaponDefinition.RangedTypes.THROWN)
				|| (weapon.getRangedDefinition().getArrows() == null)
				|| (weapon.getRangedDefinition().getArrows().length == 0)) {
			rStr = weapon.getRangedStrengthBonus();
		} else {
			Item ammo = player.getEquipment().getItems()[13];
			if (ammo != null) {
				rStr = ammo.getRangedStrengthBonus();
			}
		}

		return rStr;
	}

	public static double getMeleeSpecialModifier(Player player) {
		Item weapon = player.getEquipment().getItems()[3];

		if (weapon == null || !player.getSpecialAttack().isInitialized()) {
			return 1.0;
		}

		switch (weapon.getId()) {
		case 1215:
		case 1231:
		case 5680:
		case 5698:
			return 1.25D;
		case 4587:
			return 1.0D;
		case 1305:
			return 1.15D;
		case 1434:
			return 1.45D;
		case 14484:
			return 1.3D;
		case 13902:
		case 13926:
		case 13928:
			return 1.25D;
		case 13899:
		case 13901:
			return 1.2D;
		case 11694:
			return 1.375D;
		case 11696:
			return 1.21D;
		case 11698:
			return 1.1D;
		}

		return 1.0D;
	}

	public static double getRangedSpecialModifier(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		Item arrow = player.getEquipment().getItems()[13];

		if (weapon == null) {
			return 1.0D;
		}

		switch (weapon.getId()) {
		case 11235:
			if (arrow.getId() == 11212) {
				return 1.5D;
			}
			return 1.3D;
		case 13883:
		case 15241:
			return 1.2D;
		case 9185:
			if (arrow.getId() == 9243)
				return 1.15D;
			if (arrow.getId() == 9244)
				return 1.45D;
			if (arrow.getId() == 9245) {
				return 1.15D;
			}
			return 1.0D;
		}

		return 1.0D;
	}
}
