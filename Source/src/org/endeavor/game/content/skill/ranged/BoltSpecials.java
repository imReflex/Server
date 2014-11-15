package org.endeavor.game.content.skill.ranged;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class BoltSpecials {
	public static void checkForBoltSpecial(Player player, Entity attack) {
		Item arrow = player.getEquipment().getItems()[13];
		Item weapon = player.getEquipment().getItems()[3];

		if ((arrow == null) || (weapon == null) || (player.getCombat().getCombatType() != CombatTypes.RANGED)
				|| (!RangedSkill.requiresArrow(player, weapon.getId()))) {
			return;
		}
		
		int decrease = (attack instanceof Player && attack.getPlayer() != null
				&& attack.getPlayer().getEquipment().contains(9944) ? 5 : 0);

		if (Misc.randomNumber(100) > 20 - decrease) {
			return;
		}

		switch (arrow.getId()) {
		case 9244:
			if (!attack.isNpc()) {
				Player p = org.endeavor.game.entity.World.getPlayers()[attack.getIndex()];

				if (p == null) {
					return;
				}

				Item shield = p.getEquipment().getItems()[5];

				if ((shield != null) && ((shield.getId() == 1540) || (shield.getId() == 11283))) {
					return;
				}

			}

			attack.getUpdateFlags().sendGraphic(Graphic.lowGraphic(756, 0));
			player.getSpecialAttack().toggleSpecial();
			break;
		case 9245:
			player.getRanged().setOnyxEffectActive(true);
			attack.getUpdateFlags().sendGraphic(Graphic.lowGraphic(753, 0));
			player.getSpecialAttack().toggleSpecial();
			break;
		case 9241:
			attack.getUpdateFlags().sendGraphic(Graphic.lowGraphic(752, 0));
			player.getSpecialAttack().toggleSpecial();
			if (Misc.randomNumber(3) == 0) {
				attack.poison(5);
			}
			break;
		case 9243:
			attack.getUpdateFlags().sendGraphic(Graphic.lowGraphic(758, 0));
			player.getSpecialAttack().toggleSpecial();
			break;
		case 9242:
			attack.getUpdateFlags().sendGraphic(Graphic.lowGraphic(754, 0));
			player.getSpecialAttack().toggleSpecial();
			int pMaxHp = player.getMaxLevels()[3];
			int pHp = player.getLevels()[3];

			if (pHp / pMaxHp >= 0.25D) {
				player.getLevels()[3] = ((short) (int) (player.getLevels()[3] * 0.95D));
				attack.getLevels()[3] = ((short) (int) (attack.getLevels()[3] * 0.9D));

				player.getSkill().update(3);

				attack.checkForDeath();
				player.checkForDeath();

				if (!attack.isNpc()) {
					org.endeavor.game.entity.World.getPlayers()[attack.getIndex()].getSkill().update(3);
				}

				player.getClient().queueOutgoingPacket(
						new SendMessage("You drain 5% of your hitpoints and 10% of your opponent's hitpoints."));
			}
			break;
		}
	}
}
