package org.endeavor.game.content.skill.smithing;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class Smelting extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = 332332375165821783L;
	private final Player player;
	private final SmeltingData data;
	private final int amount;
	private int smelted = 0;
	private final String name;
	public static final Animation SMELTING_ANIMATION = new Animation(899, 0);
	public static final String A = "You smelt ";
	public static final String B = ".";
	public static final String IRON_FAILURE = "You fail to refine the iron.";

	public Smelting(Player player, int amount, SmeltingData data) {
		super(player, 3, true, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.player = player;
		this.data = data;
		this.amount = amount;
		name = data.getResult().getDefinition().getName();

		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

		if (!canSmelt(player, data, false)) {
			stop();
		}
	}

	public boolean isSuccess(Player player, SmeltingData data) {
		return SkillConstants.isSuccess(player, 13, data.levelRequired);
	}

	public boolean canSmelt(Player player, SmeltingData data, boolean taskRunning) {
		if (player.getMaxLevels()[13] < data.getLevelRequired()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Smithing level of " + data.getLevelRequired() + " to smelt this bar."));
			return false;
		}

		for (Item i : data.getRequiredOres()) {
			if (!player.getInventory().hasItemAmount(i.getId(), i.getAmount())) {
				player.getClient().queueOutgoingPacket(
						new SendMessage(taskRunning ? "You have run out of " + i.getDefinition().getName() + "."
								: "You do not have any " + i.getDefinition().getName() + "!"));
				return false;
			}
		}

		return true;
	}

	@Override
	public void execute() {
		if (!canSmelt(player, data, true)) {
			stop();
			return;
		}

		player.getUpdateFlags().sendAnimation(SMELTING_ANIMATION);

		player.getClient().queueOutgoingPacket(new SendSound(469, 0, 0));

		player.getInventory().remove(data.getRequiredOres(), false);

		if (data == SmeltingData.IRON_BAR) {
			if (SkillConstants.isSuccess(player, 13, data.getLevelRequired())) {
				player.getInventory().add(data.getResult(), false);
				player.getClient().queueOutgoingPacket(
						new SendMessage("You smelt " + Misc.getAOrAn(name) + " " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You fail to refine the iron."));
			}
		} else {
			player.getInventory().add(data.getResult(), false);
			player.getClient().queueOutgoingPacket(
					new SendMessage("You smelt " + Misc.getAOrAn(name) + " " + name + "."));
		}

		player.getInventory().update();

		player.getSkill().addExperience(13, data.getExp());

		if (++smelted == amount)
			stop();
	}

	@Override
	public void onStop() {
	}
}
