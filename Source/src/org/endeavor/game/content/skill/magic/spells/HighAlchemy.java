package org.endeavor.game.content.skill.magic.spells;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.skill.magic.Spell;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendOpenTab;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class HighAlchemy extends Spell {
	@Override
	public String getName() {
		return "High alchemy";
	}

	@Override
	public int getLevel() {
		return 55;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(554, 5), new Item(561, 1) };
	}

	@Override
	public boolean execute(Player player) {
		if (player.getSkill().locked())
			return false;
		if (player.getAttributes().get("magicitem") == null) {
			return false;
		}

		int item = player.getAttributes().getInt("magicitem");

		if (item == 995) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast Alchemy on gold!"));
			return false;
		}

		Item coins = new Item(995, GameDefinitionLoader.getHighAlchemyValue(item));

		if (!player.getInventory().hasSpaceFor(coins)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to cast High alchemy."));
			return false;
		}
		player.getInventory().remove(item);
		player.getInventory().add(coins);

		player.getUpdateFlags().sendAnimation(713, 0);
		player.getUpdateFlags().sendGraphic(new Graphic(113, 0, false));

		player.getClient().queueOutgoingPacket(new SendOpenTab(PlayerConstants.MAGIC_TAB));

		player.getSkill().lock(4);

		player.getClient().queueOutgoingPacket(new SendSound(223, 0, 0));

		if ((item == 4151) || (item == 4152) || (ItemCheck.isItemDyedWhip(new Item(item)))) {
			player.getAchievements().incr(player, "Alch an Abyssal whip");
		}

		player.getAchievements().incr(player, "Alch 10,000,000gp", coins.getAmount());
		player.getAchievements().incr(player, "Alch 50,000,000gp", coins.getAmount());
		return true;
	}

	@Override
	public double getExperience() {
		return 65.0D;
	}
}
