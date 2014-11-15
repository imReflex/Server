package org.endeavor.game.content;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class CrystalChest {
	private static final Item[] TABLE = { new Item(995, 25000), new Item(208, 2), new Item(200, 10),
			new Item(560, 200), new Item(561, 100), new Item(454, 200), new Item(1617, 4), new Item(987, 1),
			new Item(985, 1), new Item(1127, 1), new Item(1620, 10), new Item(210, 6), new Item(1079, 1),
			new Item(1215, 1), new Item(1516, 25), new Item(1514, 25), new Item(228, 250), new Item(7937, 75),
			new Item(384, 25) };

	public static void click(Player p) {
		if (p.getInventory().hasItemId(989)) {
			p.getInventory().remove(989);

			p.getInventory().addOrCreateGroundItem(1631, 1, false);

			for (int i = 0; i < 2; i++) {
				Item k = new Item(TABLE[org.endeavor.engine.utility.Misc.randomNumber(TABLE.length)]);

				if ((!k.getDefinition().isStackable()) && (k.getAmount() > 1)) {
					k.setId(k.getDefinition().getNoteId() > -1 ? k.getDefinition().getNoteId() : k.getId());
				}

				p.getInventory().addOrCreateGroundItem(k.getId(), k.getAmount(), i == 1);
			}
		} else {
			p.getClient().queueOutgoingPacket(new SendMessage("You need a crystal key to open this chest."));
		}
	}
}
