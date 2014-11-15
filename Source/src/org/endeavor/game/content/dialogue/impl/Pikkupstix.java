package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class Pikkupstix extends Dialogue {
	public Pikkupstix(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Low-level ingredients", "High-level ingredients" });
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.getShopping().open(50);
			return true;
		case 9158:
			player.getShopping().open(51);
			return true;
		}
		return false;
	}
}
