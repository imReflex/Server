package org.endeavor.game.content.skill.magic.spells;

import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.content.skill.magic.Spell;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Vengeance extends Spell {
	public static final int DELAY = 30000;
	public static final double RECOIL = 0.55D;

	@Override
	public String getName() {
		return "Vengeance";
	}

	@Override
	public int getLevel() {
		return 94;
	}

	@Override
	public Item[] getRunes() {
		return new Item[] { new Item(9075, 4), new Item(557, 10), new Item(560, 2) };
	}

	@Override
	public boolean execute(Player player) {
		if (!player.getController().equals(ARConstants.AR_CONTROLLER)) {
			if (player.getMagic().isVengeanceActive()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You have already cast Vengeance."));
				return false;
			}
			if (System.currentTimeMillis() - player.getMagic().getLastVengeance() < 30000L) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You can only cast Vengeance once every 30 seconds."));
				return false;
			}
		}

		player.getUpdateFlags().sendAnimation(4410, 0);
		player.getUpdateFlags().sendGraphic(Graphic.highGraphic(726, 0));
		player.getMagic().activateVengeance();
		return true;
	}

	public static void recoil(Player player, Hit hit) {
		if (hit.getDamage() <= 0) {
			return;
		}

		int recoil = (int) (hit.getDamage() * 0.55D);

		hit.getAttacker().hit(new Hit(player, recoil, Hit.HitTypes.NONE));
		player.getUpdateFlags().sendForceMessage("Taste Vengeance!");
		player.getMagic().deactivateVengeance();
	}

	@Override
	public double getExperience() {
		return 112.0D;
	}
}
