package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class AuburyDialogue extends Dialogue {
	public AuburyDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendOption(player, new String[] { "Teleport to Abyss", "Teleport to Essence mine" });
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			return true;
		case 9158:
			return true;
		}
		return false;
	}
}
