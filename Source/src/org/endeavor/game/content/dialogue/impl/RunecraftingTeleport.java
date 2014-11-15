package org.endeavor.game.content.dialogue.impl;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.TeleOtherTask;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class RunecraftingTeleport extends Dialogue {
	final Mob mob;

	public RunecraftingTeleport(Player player, Mob mob) {
		this.player = player;
		this.mob = mob;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Abyss", "Essence mine" });
	}

	@Override
	public boolean clickButton(int id) {
		if (id == 9157) {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			TaskQueue.queue(new TeleOtherTask(mob, player, new Location(3039, 4834)));
			return true;
		}
		if (id == 9158) {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			TaskQueue.queue(new TeleOtherTask(mob, player, new Location(2923, 4819)));
			return true;
		}
		return false;
	}
}
