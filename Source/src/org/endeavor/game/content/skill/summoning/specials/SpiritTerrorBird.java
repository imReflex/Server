package org.endeavor.game.content.skill.summoning.specials;

import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.skill.summoning.FamiliarSpecial;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SpiritTerrorBird implements FamiliarSpecial {
	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		player.getRunEnergy().add(player.getLevels()[16] / 2);

		if (player.getLevels()[16] < player.getMaxLevels()[16] + 1) {
			short[] tmp41_36 = player.getLevels();
			tmp41_36[16] = ((short) (tmp41_36[16] + 1));
		}

		mob.getUpdateFlags().sendGraphic(new Graphic(1521, 0, true));

		player.getUpdateFlags().sendGraphic(new Graphic(1306, 0, true));

		player.getClient().queueOutgoingPacket(new SendMessage("Your Spririt Terrorbird restores your run energy."));
		return true;
	}

	@Override
	public int getAmount() {
		return 8;
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
