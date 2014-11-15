package org.endeavor.game.content.skill.summoning.specials;

import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.skill.summoning.FamiliarSpecial;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class UnicornStallion implements FamiliarSpecial {
	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		int tmp5_4 = 3;
		short[] tmp5_1 = player.getLevels();
		tmp5_1[tmp5_4] = ((short) (tmp5_1[tmp5_4] + (short) (int) (player.getMaxLevels()[3] * 0.2D)));

		if (player.getLevels()[3] > player.getMaxLevels()[3]) {
			player.getLevels()[3] = player.getMaxLevels()[3];
		}

		player.getSkill().update(3);

		if (player.isPoisoned()) {
			player.curePoison(100);
			player.getClient().queueOutgoingPacket(new SendMessage("Your poison has been cured."));
		}

		player.getUpdateFlags().sendGraphic(new Graphic(1298, 0, false));

		mob.getUpdateFlags().sendGraphic(new Graphic(1356, 0, false));
		mob.getUpdateFlags().sendAnimation(8267, 0);

		return true;
	}

	@Override
	public int getAmount() {
		return 20;
	}

	@Override
	public FamiliarSpecial.SpecialType getSpecialType() {
		return FamiliarSpecial.SpecialType.NONE;
	}

	@Override
	public double getExperience() {
		return 2.0D;
	}
}
