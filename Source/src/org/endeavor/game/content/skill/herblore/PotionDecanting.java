package org.endeavor.game.content.skill.herblore;

import org.endeavor.game.content.consumables.Consumables;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PotionDecanting {
	private static final int[][] DECANT_DATA = { { 2458, 2458, 229, 2456 }, { 2458, 2456, 229, 2454 },
			{ 2458, 2454, 229, 2452 }, { 2456, 2456, 229, 2452 }, { 2454, 2456, 2458, 2452 },
			{ 2454, 2454, 2456, 2452 }, { 189, 193, 229, 2450 }, { 193, 191, 229, 189 }, { 191, 191, 229, 2450 },
			{ 191, 189, 193, 2450 }, { 189, 189, 191, 2450 }, { 193, 193, 229, 191 }, { 6687, 6689, 6691, 6685 },
			{ 6687, 6687, 6689, 6685 }, { 6687, 6691, 229, 6685 }, { 6691, 6689, 229, 6687 },
			{ 6689, 6689, 229, 6685 }, { 6691, 6691, 229, 6689 }, { 127, 131, 229, 2430 }, { 131, 129, 229, 127 },
			{ 129, 129, 229, 2430 }, { 127, 129, 131, 2430 }, { 127, 127, 129, 2430 }, { 131, 131, 229, 129 },
			{ 139, 143, 229, 2434 }, { 139, 141, 143, 2434 }, { 141, 141, 229, 2434 }, { 139, 143, 229, 2434 },
			{ 139, 139, 141, 2434 }, { 143, 143, 229, 141 }, { 133, 137, 229, 2432 }, { 137, 135, 229, 2432 },
			{ 135, 135, 229, 2432 }, { 135, 133, 137, 2432 }, { 133, 133, 135, 2432 }, { 137, 137, 229, 135 },
			{ 121, 123, 229, 121 }, { 121, 125, 229, 2428 }, { 123, 123, 229, 113 }, { 121, 123, 125, 2428 },
			{ 121, 121, 123, 2428 }, { 125, 125, 229, 123 }, { 119, 117, 229, 115 }, { 119, 115, 229, 113 },
			{ 117, 117, 229, 113 }, { 115, 117, 119, 113 }, { 115, 115, 117, 113 }, { 119, 119, 229, 117 },
			{ 3042, 3046, 229, 3040 }, { 3046, 3044, 229, 3042 }, { 3044, 3044, 229, 3040 },
			{ 3042, 3044, 3046, 3040 }, { 3042, 3042, 3044, 3040 }, { 3046, 3406, 229, 3044 },
			{ 3018, 3022, 229, 3016 }, { 3022, 3020, 229, 3018 }, { 3020, 3020, 229, 3016 },
			{ 3018, 3020, 3022, 3016 }, { 3018, 3018, 3020, 3016 }, { 3022, 3022, 229, 3020 },
			{ 3010, 3014, 229, 3008 }, { 3014, 3012, 229, 3010 }, { 3012, 3012, 229, 3008 },
			{ 3010, 3012, 3014, 3008 }, { 3010, 3010, 3012, 3008 }, { 3014, 3014, 229, 3012 }, { 163, 167, 229, 2442 },
			{ 167, 165, 229, 163 }, { 165, 165, 229, 2442 }, { 165, 163, 167, 2442 }, { 163, 163, 165, 2442 },
			{ 167, 167, 229, 165 }, { 3026, 3030, 229, 3024 }, { 3030, 3028, 229, 3026 }, { 3028, 3028, 229, 3024 },
			{ 3028, 3026, 3030, 3024 }, { 3030, 3030, 229, 3028 }, { 3026, 3026, 3028, 3024 }, { 159, 161, 229, 157 },
			{ 157, 161, 229, 2440 }, { 159, 159, 229, 2440 }, { 159, 157, 161, 2440 }, { 157, 157, 159, 2440 },
			{ 161, 161, 229, 159 }, { 171, 173, 229, 169 }, { 169, 173, 229, 2444 }, { 171, 171, 229, 2444 },
			{ 171, 169, 173, 2444 }, { 169, 169, 171, 2444 }, { 173, 173, 229, 171 }, { 147, 149, 229, 145 },
			{ 145, 149, 229, 2436 }, { 147, 147, 229, 2436 }, { 147, 145, 229, 2436 }, { 145, 145, 147, 2436 },
			{ 149, 149, 229, 147 }, { 177, 175, 179, 2446 }, { 175, 175, 177, 2446 }, { 177, 179, 229, 2446 },
			{ 175, 179, 229, 175 }, { 175, 175, 229, 2446 }, { 179, 179, 229, 177 }, { 183, 181, 185, 2448 },
			{ 181, 181, 183, 2448 }, { 183, 185, 229, 2448 }, { 181, 185, 229, 181 }, { 181, 181, 229, 2448 },
			{ 185, 185, 229, 183 }, { 5947, 5945, 5949, 5943 }, { 5945, 5945, 5947, 5943 }, { 5947, 5949, 229, 5943 },
			{ 5945, 5949, 229, 5945 }, { 5945, 5945, 229, 5943 }, { 5949, 5949, 229, 5947 },
			{ 5956, 5954, 5958, 5952 }, { 5954, 5954, 5956, 5952 }, { 5956, 5958, 229, 5952 },
			{ 5954, 5958, 229, 5954 }, { 5954, 5954, 229, 5952 }, { 5958, 5958, 229, 5956 },
			{ 9743, 9741, 9745, 9739 }, { 9741, 9741, 9743, 9739 }, { 9743, 9745, 229, 9739 },
			{ 9741, 9745, 229, 9741 }, { 9741, 9741, 229, 9739 }, { 9745, 9745, 229, 9743 },
			{ 3036, 3034, 3038, 3032 }, { 3034, 3034, 3036, 3032 }, { 3036, 3038, 229, 3032 },
			{ 3034, 3038, 229, 3034 }, { 3034, 3034, 229, 3032 }, { 3038, 3038, 229, 3036 } };

	public static void decantAll(Player p) {
		final Item[] items = p.getInventory().getItems();

		int decanted = 0;

		main: for (int i = 0; i < items.length; i++) {
			if (items[i] != null && Consumables.isPotion(items[i])) {

				for (int k = 0; k < items.length; k++) {
					if (i != k && items[k] != null && Consumables.isPotion(items[k])) {

						if (!p.getInventory().hasItemAmount(new Item(995, 300))) {
							p.getClient().queueOutgoingPacket(
									new SendMessage("You do not have enough coins to decant any potions!"));
							p.getInventory().update();
							return;
						}

						final int id1 = items[i].getId();
						final int id2 = items[k].getId();

						for (int[] j : DECANT_DATA) {
							if (((id1 == j[0]) && (id2 == j[1])) || ((id1 == j[1]) && (id2 == j[0]))) {
								p.getInventory().setSlot(new Item(j[3], 1), i);
								p.getInventory().setSlot(new Item(j[2], 1), k);
								p.getInventory().remove(new Item(995, 300), false);
								decanted++;
								continue main;
							}
						}

					}
				}

			}
		}

		if (decanted == 0) {
			p.getClient().queueOutgoingPacket(new SendMessage("You do not have any potions to decant."));
		} else {
			p.getClient().queueOutgoingPacket(new SendMessage("Bob Barter decants your potions."));
			p.getInventory().update();
		}
	}

	public static boolean decant(Player p, int slot1, int slot2) {
		if ((p.getInventory().get(slot1) == null) || (p.getInventory().get(slot2) == null)) {
			return true;
		}

		int id1 = p.getInventory().get(slot1).getId();
		int id2 = p.getInventory().get(slot2).getId();

		for (int[] i : DECANT_DATA) {
			if (((id1 == i[0]) && (id2 == i[1])) || ((id1 == i[1]) && (id2 == i[0]))) {
				p.getInventory().setSlot(new Item(i[3], 1), slot1);
				p.getInventory().setSlot(new Item(i[2], 1), slot2);

				p.getInventory().update();

				p.getClient().queueOutgoingPacket(new SendMessage("You mix the potions into one."));
				return true;
			}
		}

		return true;
	}
}
