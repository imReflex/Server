package org.endeavor.game.content.skill.crafting;

import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendItemOnInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class HideTanning {
	public static final int[][] TANNING_HIDE = { { 1739, 1741, 2 }, { 1739, 1743, 3 }, { 6287, 6289, 45 },
			{ 1753, 1745, 45 }, { 1751, 2505, 45 }, { 1749, 2507, 45 }, { 1747, 2509, 45 } };

	public static final void sendTanningInterface(Player player) {
		for (int i = 0; i < TANNING_HIDE.length; i++) {
			player.getClient().queueOutgoingPacket(new SendItemOnInterface(14769 + i, 250, TANNING_HIDE[i][1]));
			player.getClient().queueOutgoingPacket(
					new SendString(Item.getDefinition(TANNING_HIDE[i][1]).getName(), 14777 + i));
			player.getClient().queueOutgoingPacket(new SendString(TANNING_HIDE[i][2] + " gp", 14785 + i));
		}

		player.getClient().queueOutgoingPacket(new SendString("", 14784));
		player.getClient().queueOutgoingPacket(new SendString("", 14792));
		player.getClient().queueOutgoingPacket(new SendInterface(14670));
	}

	public static final void tan(Player player, int amount, int index) {
		int price = TANNING_HIDE[index][2];
		int coins = player.getInventory().getItemAmount(995);

		if (coins < price) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough coins to purchase this."));
			return;
		}

		int toTan = TANNING_HIDE[index][0];

		int invAm = player.getInventory().getItemAmount(toTan);

		if (invAm == 0) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have any of this hide."));
			return;
		}

		if (invAm < amount) {
			amount = invAm;
		}

		int total = amount * price;

		if (total > coins) {
			amount = coins / price;
			total = amount * price;
		}

		player.getInventory().remove(toTan, amount, false);
		player.getInventory().add(TANNING_HIDE[index][1], amount);

		player.getClient().queueOutgoingPacket(new SendMessage("You tan " + amount + " hide(s) for " + total + "gp."));
	}

	public static final boolean clickButton(Player player, int id) {
		switch (id) {
		case 57225:
			tan(player, 1, 0);
			return true;
		case 57226:
			tan(player, 1, 1);
			return true;
		case 57227:
			tan(player, 1, 2);
			return true;
		case 57228:
			tan(player, 1, 3);
			return true;
		case 57229:
			tan(player, 1, 4);
			return true;
		case 57230:
			tan(player, 1, 5);
			return true;
		case 57231:
			tan(player, 1, 6);
			return true;
		case 57217:
			tan(player, 5, 0);
			return true;
		case 57218:
			tan(player, 5, 1);
			return true;
		case 57219:
			tan(player, 5, 2);
			return true;
		case 57220:
			tan(player, 5, 3);
			return true;
		case 57221:
			tan(player, 5, 4);
			return true;
		case 57222:
			tan(player, 5, 5);
			return true;
		case 57223:
			tan(player, 5, 6);
			return true;
		case 57201:
		case 57209:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[0][0]), 0);
			return true;
		case 57202:
		case 57210:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[1][0]), 1);
			return true;
		case 57203:
		case 57211:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[2][0]), 2);
			return true;
		case 57204:
		case 57212:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[3][0]), 3);
			return true;
		case 57205:
		case 57213:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[4][0]), 4);
			return true;
		case 57206:
		case 57214:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[5][0]), 5);
			return true;
		case 57207:
		case 57215:
			tan(player, player.getInventory().getItemAmount(TANNING_HIDE[6][0]), 6);
			return true;
		case 57208:
		case 57216:
		case 57224:
		}
		return false;
	}
}
