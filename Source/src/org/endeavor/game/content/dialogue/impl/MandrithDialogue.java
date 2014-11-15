package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class MandrithDialogue extends Dialogue {
	public MandrithDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Exchange artifacts", "View shop" });
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.getEarningPotential().trade();
			end();
			return true;
		case 9158:
			player.getShopping().open(99);
			return true;
		}
		return false;
	}
}
