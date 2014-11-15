package org.endeavor.game.content;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.MobDrops;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class MoneyCaskets {

	public MoneyCaskets() {
	}

	public static boolean openCasket(Player p, int id) {
		if (id == MobDrops.EASY_CASKET_ID) {
			p.getClient().queueOutgoingPacket(new SendSound(1641, 10, 0));
			return add(p, MobDrops.EASY_CASKET_ID, 50000);
		}

		if (id == MobDrops.HARD_CASKET_ID) {
			p.getClient().queueOutgoingPacket(new SendSound(1641, 10, 0));
			return add(p, MobDrops.HARD_CASKET_ID, 100000);
		}

		return false;
	}

	public static boolean add(Player p, int id, int am) {
		if (p.getInventory().hasSpaceFor(new Item(995, am))
				|| p.getInventory().hasSpaceOnRemove(new Item(id, 1), new Item(995, am))) {
			p.getInventory().remove(id, 1, false);
			p.getInventory().add(995, am);
			p.getClient().queueOutgoingPacket(new SendMessage("You recieve " + am + " coins from the casket."));
		} else {
			p.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to open this."));
		}

		return false;
	}

}
