package org.endeavor.game.content.skill.summoning.specials;

import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.skill.summoning.FamiliarSpecial;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.player.Player;

public class SpiritSpider implements FamiliarSpecial {
	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		mob.getUpdateFlags().sendForceMessage("Clicketyclack");

		Location a = new Location(player.getX() + 1, player.getY(), player.getZ());
		Location b = new Location(player.getX(), player.getY() + 1, player.getZ());

		GroundItemHandler.add(new Item(223), a, player);
		GroundItemHandler.add(new Item(223), b, player);

		World.sendStillGraphic(1342, 0, a);
		World.sendStillGraphic(1342, 0, b);
		return true;
	}

	@Override
	public int getAmount() {
		return 6;
	}

	@Override
	public FamiliarSpecial.SpecialType getSpecialType() {
		return FamiliarSpecial.SpecialType.NONE;
	}

	@Override
	public double getExperience() {
		return 0.2D;
	}
}
