package org.endeavor.game.entity.player;

import org.endeavor.engine.cache.map.MapLoading;
import org.endeavor.engine.task.RunOnceTask;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class GraveStone extends GameObject {
	private final Player player;
	private Item[] items;
	private final int message = Misc.randomNumber(RANDOM_MESSAGES.length);
	private boolean looted = false;

	public static final String[][] RANDOM_MESSAGES = { { "Here lies ", "He was young and dumb." },
			{ "Here lies ", "A brave warrior, but a bit of a drunk." }, { "Here lies ", "He was old." },
			{ "Here lies ", "I guess the dragon won." },
			{ "Here lies ", "There's a fine line between brave and stupid." },
			{ "Here lies ", "I told him not to drink it." },
			{ "Here lies ", "Sometimes you have to cut your losses." }, { "Here lies ", "He forgot his lobsters." },
			{ "Here lies ", "He went down the rabbit hole." },
			{ "Here lies ", "I dared him 100k he couldn't swallow him scimitar, I won." },
			{ "Here lies ", "Never run with daggers." } };

	private GraveStone(Player player, Item[] items) {
		super(12717, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10, 0);
		this.player = player;
		this.items = items;

		ObjectManager.add(this);
		ObjectManager.send(this);
		MapLoading.addObject(false, getId(), getLocation().getX(), getLocation().getY(), getLocation().getZ(), 10, 0);

		TaskQueue.queue(new GraveStoneTask(player));
	}

	public static void setGraveStone(Player p, Item[] items) {
		GraveStone stone = (GraveStone) ObjectManager.getGraveFor(p);

		if (stone != null) {
			stone.drop();
		}

		new GraveStone(p, items);
	}

	public void drop() {
		for (Item i : items) {
			if (i != null) {
				GroundItemHandler.add(new Item(i), player.getLocation(), player);
			}
		}

		items = null;

		looted = true;
		player.getClient().queueOutgoingPacket(
				new SendMessage("Your previous Grave stone has been removed and your items have been dropped."));

		ObjectManager.remove(this);
		MapLoading.removeObject(getId(), getLocation().getX(), getLocation().getY(), getLocation().getZ(), 10, 0);
	}

	public void retrieve() {
		int exceeded = -1;

		for (Item i : items) {
			if (i != null) {
				if (player.getInventory().hasSpaceFor(new Item(i))) {
					player.getInventory().add(i);
				} else if (player.getBank().hasSpaceFor(new Item(i))) {
					exceeded = 0;
					player.getBank().add(i);
				} else {
					exceeded = 1;
					GroundItemHandler.add(new Item(i), player.getLocation(), player);
				}
			}
		}

		items = null;

		if (exceeded == 0)
			player.getClient().queueOutgoingPacket(
					new SendMessage("The rest of your items were deposited into your bank."));
		else if (exceeded == 1) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("Your bank and inventory are full, the rest of your items were dropped."));
		}

		looted = true;
		ObjectManager.remove(this);
		MapLoading.removeObject(getId(), getLocation().getX(), getLocation().getY(), getLocation().getZ(), 10, 0);
	}

	public void readGraveStone(Player p) {
		for (int i = 11112; i <= 11123; i++) {
			p.getClient().queueOutgoingPacket(new SendString("", i));
		}

		p.getClient().queueOutgoingPacket(
				new SendString(RANDOM_MESSAGES[message][0] + player.getUsername() + ".", 11112));
		p.getClient().queueOutgoingPacket(new SendString(RANDOM_MESSAGES[message][1], 11117));

		p.getClient().queueOutgoingPacket(new SendInterface(11104));

		if (p.equals(player))
			retrieve();
	}

	public class GraveStoneTask extends RunOnceTask {
		public GraveStoneTask(Player entity) {
			super(null, 1500);
		}

		@Override
		public void onStop() {
			if (looted) {
				return;
			}
			drop();
		}
	}
}
