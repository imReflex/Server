package org.endeavor.game.entity.mob.impl;

import org.endeavor.engine.definitions.NpcCombatDefinition.Magic;
import org.endeavor.engine.definitions.NpcCombatDefinition.Melee;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.prayer.PrayerConstants;
import org.endeavor.game.content.skill.prayer.PrayerBook.PrayerBookType;
import org.endeavor.game.content.skill.prayer.impl.CursesPrayerBook;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

/**
 * Handles the sea troll queen boss
 * 
 * @author Arithium
 * 
 */
public class SeaTrollQueen extends Mob {

	public SeaTrollQueen() {
		super(3847, false, new Location(2342, 3702));
	}

	@Override
	public void updateCombatType() {
		CombatTypes type = CombatTypes.MELEE;
		if (getCombat().getAttacking() != null) {
			if (!getCombat().getAttacking().isNpc()) {
				Player player = (Player) getCombat().getAttacking();
				if (!getCombat().withinDistanceForAttack(CombatTypes.MELEE, true)) {
					if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT ? player.getPrayer().active(PrayerConstants.PROTECT_FROM_MAGIC) : player.getPrayer().active(CursesPrayerBook.DEFLECT_MAGIC)) {
						if (player.getSkill().getLevels()[SkillConstants.PRAYER] > 0) {
							World.sendProjectile(getProjectile(), player, this);
							int modifier = player.getSkill().getLevels()[SkillConstants.PRAYER] - 20 > 0 ? 20 : player.getSkill().getLevels()[SkillConstants.PRAYER];
							player.getPrayer().drain(modifier);
							type = CombatTypes.RANGED;
							getCombat().setAttackTimer(6);
						} else {
							type = CombatTypes.MAGIC;
						}
					} else {
						type = CombatTypes.MAGIC;
					}
				} else {
					if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT ? player.getPrayer().active(PrayerConstants.PROTECT_FROM_MAGIC) : player.getPrayer().active(CursesPrayerBook.DEFLECT_MAGIC)) {
						type = CombatTypes.MELEE;
					} else if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT ? player.getPrayer().active(PrayerConstants.PROTECT_FROM_MELEE) : player.getPrayer().active(CursesPrayerBook.DEFLECT_MELEE)) {
						type = CombatTypes.MAGIC;
					} else {
						type = Misc.randomNumber(10) < 5 ? CombatTypes.MELEE : CombatTypes.MAGIC;
					}
				}
			}
			
			getCombat().setCombatType(type);
			getCombat().setBlockAnimation(getCombatDefinition().getBlock());
			switch (getCombat().getCombatType()) {
			case MAGIC:
				byte combatIndex = (byte) Misc.randomNumber(getCombatDefinition().getMagic().length);
				Magic magic = getCombatDefinition().getMagic()[combatIndex];
				getCombat().getMagic().setAttack(magic.getAttack(), magic.getAnimation(), magic.getStart(), magic.getEnd(), magic.getProjectile());
				break;
			case MELEE:
				combatIndex = (byte) Misc.randomNumber(getCombatDefinition().getMelee().length);
				Melee melee = getCombatDefinition().getMelee()[combatIndex];
				getCombat().getMelee().setAttack(melee.getAttack(), melee.getAnimation());
				break;
			default:
				break;
			
			}
		}
	}

	@Override
	public int getRespawnTime() {
		return 60;
	}
	
	private static Projectile getProjectile() {
		return new Projectile(109, 1, 40, 70, 43, 31, 16);
	}

}
