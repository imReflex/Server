package org.endeavor.game.content.skill.smithing;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class SmithingTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -692896105813062514L;
	private final Player player;
	private final Item smith;
	private final Item bar;
	private final int amount;
	private int loop = 0;

	public SmithingTask(Player player, Item smith, Item bar, int amount) {
		super(player, 2, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.smith = smith;
		this.bar = bar;
		this.amount = amount;

		int lvl = SmithingConstants.getLevel(smith.getId());

		if (player.getMaxLevels()[13] < lvl) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Smithing level of " + lvl + " to make that."));
			stop();
		} else if (!player.getInventory().hasItemAmount(new Item(bar))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough bars to make that."));
			stop();
		} else if (!player.getInventory().hasItemId(2347)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a hammer!"));
			stop();
		} else {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		}
	}

	public static void start(Player p, int item, int amount, int interfaceId, int slot) {
		String check = Item.getDefinition(item).getName().substring(0, 3);

		int bar = -1;

		int make = 1;

		if (Item.getDefinition(item).isStackable()) {
			make = 15;
		}

		switch (check) {
		case "Bro":
			bar = SmithingConstants.BARS[0];
			break;
		case "Iro":
			bar = SmithingConstants.BARS[1];
			break;
		case "Ste":
			bar = SmithingConstants.BARS[2];
			break;
		case "Mit":
			bar = SmithingConstants.BARS[3];
			break;
		case "Ada":
			bar = SmithingConstants.BARS[4];
			break;
		case "Run":
			bar = SmithingConstants.BARS[5];
			break;
		}

		TaskQueue.queue(new SmithingTask(p, new Item(item, make), new Item(bar, SmithingConstants.getBarAmount(
				interfaceId, slot)), amount));
	}

	@Override
	public void execute() {
		if (!hasRequirements()) {
			stop();
			return;
		}

		player.getSkill().addExperience(13, getExperience());

		player.getInventory().remove(new Item(bar), false);
		player.getInventory().add(new Item(smith), true);

		player.getUpdateFlags().sendAnimation(898, 0);
		player.getClient().queueOutgoingPacket(new SendSound(468, 10, 10));

		if (smith.getAmount() == 1)
			player.getClient().queueOutgoingPacket(
					new SendMessage("You make " + Misc.getAOrAn(smith.getDefinition().getName()) + " "
							+ smith.getDefinition().getName() + "."));
		else {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You make " + smith.getAmount() + " " + smith.getDefinition().getName()
							+ (!smith.getDefinition().getName().endsWith("s") ? "s" : "") + "."));
		}

		if (++loop == amount)
			stop();
	}

	public boolean hasRequirements() {
		if (!player.getInventory().hasItemAmount(new Item(bar))) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You have run out of " + bar.getDefinition().getName() + "s."));
			return false;
		}
		
		if (!player.getInventory().hasItemId(2347)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a hammer!"));
			return false;
		}

		return true;
	}

	public double getExperience() {
		switch (bar.getId()) {
		case 2349:
			return 12.5D * bar.getAmount();
		case 2351:
			return 25 * bar.getAmount();
		case 2353:
			return 37.5D * bar.getAmount();
		case 2359:
			return 50 * bar.getAmount();
		case 2361:
			return 62.5D * bar.getAmount();
		case 2363:
			return 75 * bar.getAmount();
		case 2350:
		case 2352:
		case 2354:
		case 2355:
		case 2356:
		case 2357:
		case 2358:
		case 2360:
		case 2362:
		}
		return 0.0D;
	}

	@Override
	public void onStop() {
	}
}
