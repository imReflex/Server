package org.endeavor.game.content.combat.impl;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.dialogue.impl.Scavvo;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PlayerDrops {
	public static final ItemComparator ITEM_VALUE_COMPARATOR = new ItemComparator();

	public static final void dropItemsOnDeath(Player player) {
		Entity killer = player.getCombat().getDamageTracker().getKiller();
		/*if(killer != null && killer instanceof Player)
			player.send(new SendMessage("You were killed by: " + ((Player) killer).getUsername()));*/
		player.getEarningPotential().onKilled(killer);

		int kept = player.isRespectedMember() && !player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER) ? 7 : 3;

		if (player.getSkulling().isSkulled()) {
			kept = 0;
		}

		if (player.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT) {
			if (player.getPrayer().active(10)) {
				kept++;
			}
		} else if (player.getPrayer().active(0)) {
			kept++;
		}

		Queue<Item> items = new PriorityQueue<Item>(42, ITEM_VALUE_COMPARATOR);

		for (Item i : player.getInventory().getItems()) {
			if (i != null) {
				items.add(i);
			}
		}

		for (Item i : player.getEquipment().getItems()) {
			if (i != null) {
				items.add(i);
			}
		}

		Item k = null;

		if ((killer != null) && (!killer.isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[killer.getIndex()];

			if (p == null) {
				return;
			}

			p.getEarningPotential().drop(player);
		}

		player.getInventory().clear();
		player.getEquipment().clear();

		for (int i = 0; i < kept; i++) {
			Item keep = (Item) items.poll();

			if (keep != null) {
				if (keep.getAmount() == 1) {
					player.getInventory().add(keep, false);
				} else {
					player.getInventory().add(new Item(keep.getId(), 1), false);
					keep.remove(1);
					items.add(keep);
				}

			}

		}

		while ((k = (Item) items.poll()) != null) {
			if ((k.getDefinition().isTradable()) || (ItemCheck.isItemDyedWhip(k))) {
				if (k.getId() == 15443)
					player.getInventory().add(1769, 1);
				else if (k.getId() == 15442)
					player.getInventory().add(1767, 1);
				else if (k.getId() == 15441)
					player.getInventory().add(1765, 1);
				else if (k.getId() == 15444) {
					player.getInventory().add(1771, 1);
				}

				if (ItemCheck.isItemDyedWhip(k)) {
					k.setId(4151);
				}

				Scavvo.doItemBreaking(k);

				GroundItemHandler.add(k, player.getLocation(), (killer == null) || (killer.isNpc()) ? player
						: org.endeavor.game.entity.World.getPlayers()[killer.getIndex()]);
			} else {
				player.getInventory().add(k, false);
			}
		}

		GroundItemHandler.add(new Item(526, 1), player.getLocation(), (killer == null) || (killer.isNpc()) ? player
				: org.endeavor.game.entity.World.getPlayers()[killer.getIndex()]);

		player.getInventory().update();
	}

	public static class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item arg0, Item arg1) {
			int val1 = (!arg0.getDefinition().isTradable()) && (!ItemCheck.isItemDyedWhip(arg0)) ? 0
					: GameDefinitionLoader.getHighAlchemyValue(arg0.getId());
			int val2 = (!arg1.getDefinition().isTradable()) && (!ItemCheck.isItemDyedWhip(arg1)) ? 0
					: GameDefinitionLoader.getHighAlchemyValue(arg1.getId());

			if (val1 > val2)
				return -1;
			if (val1 < val2) {
				return 1;
			}

			return 0;
		}
	}
}
