package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public abstract class WildernessWarning extends Dialogue {
	public WildernessWarning(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendStatement(player, new String[] { "This teleport is to the Wilderness.",
					"You can be killed and lose your items here.", "Revenants surround themselves with a multi area.",
					"Are you sure you want to go here?" });
			next += 1;
			break;
		case 1:
			DialogueManager
					.sendOption(player, new String[] { "Yes, teleport me to the Wilderness.", "No, stay here." });
			end();
		}
	}

	public abstract void onConfirm();

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			onConfirm();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		}
		return false;
	}
}
