package org.endeavor.game.content.randoms;

import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public abstract class RandomEvent {
	public static final int RANDOM_EVENT_GIFT = 6183;
	public static final int EASTER_RING_CHANCE = 50;
	public static final String PAUSE_FAILURE_KEY = "pauserandom";
	public static final String CURRENT_RANDOM_KEY = "randomevent";
	public static final int[] RANDOM_EVENT_GIFTS = { 6180, 6181, 6182, 6188, 6184, 6185, 6654, 6655, 6654, 3057, 3058,
			3059, 3060, 3061, 7592, 7593, 7594, 7595, 7596 };

	public static void declare() {
		for (int i : RANDOM_EVENT_GIFTS) {
			ItemDefinition def = GameDefinitionLoader.getItemDef(i);

			if (def != null) {
				def.setUntradable();
			}
		}

		GameDefinitionLoader.getItemDef(7927).setUntradable();

		GameDefinitionLoader.getItemDef(6183).setUntradable();
	}

	public abstract void init(Player player);

	public abstract boolean doStage(Player player, Mob mob, byte stage);

	public void fail(Player p) {
		if (p.getController().canTeleport()) {
			doFailure(p);
			p.getController().onTeleport(p);
		}
	}

	public abstract void doFailure(Player player);

	public void doSuccess(Player player, Mob mob) {
		player.getClient().queueOutgoingPacket(new SendMessage("You recieve a gift for completing the Random Event."));

		if (player.getInventory().hasSpaceFor(new Item(6183))) {
			player.getInventory().add(6183, 1, true);
		} else {
			GroundItemHandler.add(new Item(6183), player.getLocation(), player);
		}

		onSuccess(mob);

		player.getAttributes().remove("randomevent");
		player.getAttributes().remove("pauserandom");
	}

	public abstract void onSuccess(Mob mob);

	public abstract void clickButton(Player player, int button);

	public static final void openGift(Player player) {
		player.getInventory().remove(6183);
		int item = -1;

		item = -1;
		
		for (int i : RANDOM_EVENT_GIFTS) {
			if (!player.getInventory().hasItemId(i)
					&& !player.getEquipment().contains(i)
					&& !player.getBank().hasItemId(i)) {
				item = i;
			}
		}
		
		if (item == -1) {
			player.getInventory().addOrCreateGroundItem(995, 150000, true);
			player.getClient().queueOutgoingPacket(new SendMessage("You recieve 150k from the Random Event Gift!"));
		} else {
		
			if (((item == 6184) || (item == 6185)) && (player.getGender() == 1)) {
				item += 2;
			}
	
			player.getInventory().addOrCreateGroundItem(item, 1, true);
	
			String name = Item.getDefinition(item).getName();
	
			player.getClient().queueOutgoingPacket(
					new SendMessage("You recieve " + (Misc.needsAnA(name) ? Misc.getAOrAn(name) + " " : "") + name
							+ " from the Random Event Gift!"));
		}
	}
}
