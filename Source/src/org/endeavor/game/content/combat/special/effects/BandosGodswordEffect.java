package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class BandosGodswordEffect implements CombatEffect {
	public static final int[] BGS_DRAIN_IDS = { 1, 2, 5, 0, 6, 4 };

	@Override
	public void execute(Player p, Entity e) {
		int id = -1;
		int drain = p.getLastDamageDealt() / 10;

		Player p2 = null;

		if (!e.isNpc()) {
			p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];
		}

		if (drain <= 0) {
			return;
		}

		for (int i = 0; i < BGS_DRAIN_IDS.length; i++) {
			if (e.getLevels()[BGS_DRAIN_IDS[i]] != 0) {
				id = i;
			}
		}

		if (id == -1) {
			return;
		}

		if (e.getLevels()[BGS_DRAIN_IDS[id]] - drain < 0) {
			int diff = drain - e.getLevels()[BGS_DRAIN_IDS[id]];
			e.getLevels()[BGS_DRAIN_IDS[id]] = 0;
			p.getClient().queueOutgoingPacket(
					new SendMessage("You drain your opponents "
							+ org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[BGS_DRAIN_IDS[id]]
							+ " down to 0."));

			if (p2 != null) {
				p2.getSkill().update(id);
			}

			if (id < BGS_DRAIN_IDS.length - 1) {
				id++;
				int tmp202_201 = BGS_DRAIN_IDS[id];
				short[] tmp202_194 = e.getLevels();
				tmp202_194[tmp202_201] = ((short) (tmp202_194[tmp202_201] - diff));
				p2.getSkill().update(BGS_DRAIN_IDS[id]);
				p.getClient().queueOutgoingPacket(
						new SendMessage("You drain some of your opponents "
								+ org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[BGS_DRAIN_IDS[id]] + "."));
			}
		} else {
			int tmp277_276 = BGS_DRAIN_IDS[id];
			short[] tmp277_269 = e.getLevels();
			tmp277_269[tmp277_276] = ((short) (tmp277_269[tmp277_276] - drain));
			if (p2 != null) {
				p2.getSkill().update(BGS_DRAIN_IDS[id]);
			}
			if (e.getLevels()[BGS_DRAIN_IDS[id]] == 0)
				p.getClient().queueOutgoingPacket(
						new SendMessage("You drain your opponents "
								+ org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[BGS_DRAIN_IDS[id]]
								+ " down to 0."));
			else
				p.getClient().queueOutgoingPacket(
						new SendMessage("You drain some of your opponents "
								+ org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[BGS_DRAIN_IDS[id]] + "."));
		}
	}
}
