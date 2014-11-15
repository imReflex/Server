package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class PetTutorial extends Dialogue {
	public PetTutorial(Player player) {
		this.player = player;
		player.setController(Tutorial.TUTORIAL_CONTROLLER);
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 6750, Emotion.CALM, new String[] { "Your pet will grow over time.",
					"But you must take care of it.", "Your pet will run away if you do not",
					"feed it every 45 minutes." });
			next += 1;
			break;
		case 1:
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}
}
