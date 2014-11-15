package org.endeavor.game.content.skill.herblore;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendEnterXInterface;
import org.endeavor.game.entity.player.net.out.impl.SendItemOnInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendMoveComponent;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class HerbloreUnfinishedPotionTask extends Task {
	public static final String HERBLORE_ITEM_1_KEY = "herbloreitem1";
	public static final String HERBLORE_ITEM_2_KEY = "herbloreitem2";
	private static final int ANIM = 363;
	private static final int[][] BUTTON_IDS = { { 10239, 1 }, { 10238, 5 }, { 6211, 28 }, { 6212, 100 } };
	private final Player player;
	private final UnfinishedPotionData data;
	private Item used;
	private Item usedWith;
	private int amountToMake;

	public HerbloreUnfinishedPotionTask(Player player, UnfinishedPotionData data, Item used, Item usedWith,
			int amountToMake) {
		super(player, 3, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.data = data;
		this.used = used;
		this.usedWith = usedWith;
		this.amountToMake = amountToMake;
	}

	public static boolean handleHerbloreButtons(Player player, int buttonId) {
		int amount = 0;
		for (int i = 0; i < BUTTON_IDS.length; i++) {
			if (BUTTON_IDS[i][0] == buttonId) {
				amount = BUTTON_IDS[i][1];
				break;
			}
		}
		if (amount == 0) {
			return false;
		}
		if (amount != 100)
			attemptToCreateUnfinishedPotion(player, amount, (Item) player.getAttributes().get("herbloreitem1"),
					(Item) player.getAttributes().get("herbloreitem2"));
		else {
			player.getClient().queueOutgoingPacket(
					new SendEnterXInterface(4429, ((Item) player.getAttributes().get("herbloreitem1")).getId()));
		}
		return true;
	}

	public static void displayInterface(Player player, Item used, Item usedWith) {
		UnfinishedPotionData data = UnfinishedPotionData.forId(used.getId() == 227 ? usedWith.getId() : used.getId());
		if (data == null) {
			return;
		}

		player.getClient().queueOutgoingPacket(new SendChatInterface(4429));
		Item unfinishedPotion = new Item(data.getUnfPotion(), 1);
		player.getClient().queueOutgoingPacket(new SendMoveComponent(0, 25, 1746));
		player.getClient().queueOutgoingPacket(new SendItemOnInterface(1746, 170, unfinishedPotion.getId()));
		player.getClient().queueOutgoingPacket(
				new SendString("\\n \\n \\n \\n @dre@" + unfinishedPotion.getDefinition().getName(), 2799));

		player.getAttributes().set("herbloreitem1", used);
		player.getAttributes().set("herbloreitem2", usedWith);
	}

	private static boolean meetsRequirements(Player player, UnfinishedPotionData data, Item used, Item used2) {
		if (player.getSkill().getLevels()[15] < data.getLevelReq()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need an herblore level of " + data.getLevelReq() + " to make this."));
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return false;
		}

		if ((!player.getInventory().hasItemId(used.getId())) || (!player.getInventory().hasItemId(used2.getId()))) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You don't have the ingredients required to make this potion."));
			return false;
		}

		return true;
	}

	public static boolean attemptToCreateUnfinishedPotion(Player player, int amount, Item used, Item usedWith) {
		Item[] items = { (Item) player.getAttributes().get("herbloreitem1"),
				(Item) player.getAttributes().get("herbloreitem2") };

		UnfinishedPotionData data = UnfinishedPotionData.forId(items[0].getId() == 227 ? items[1].getId() : items[0]
				.getId());

		if (data == null) {
			return false;
		}

		if (!meetsRequirements(player, data, used, usedWith)) {
			return false;
		}

		TaskQueue.queue(new HerbloreUnfinishedPotionTask(player, data, new Item(items[0]), new Item(items[1]), amount));
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		return true;
	}

	private void createUnfinishedPotion() {
		player.getInventory().add(new Item(data.getUnfPotion(), 1));
		Item herb = used.getId() == 227 ? usedWith : used;
		player.getClient().queueOutgoingPacket(
				new SendMessage("You put the " + herb.getDefinition().getName() + " in the vial of water."));
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, data, used, usedWith)) {
			stop();
			return;
		}

		player.getUpdateFlags().sendAnimation(new Animation(363));
		player.getClient().queueOutgoingPacket(new SendSound(281, 0, 0));

		player.getInventory().remove(new Item(used));
		player.getInventory().remove(new Item(usedWith));
		createUnfinishedPotion();

		if (--amountToMake == 0)
			stop();
	}

	@Override
	public void onStop() {
	}
}
