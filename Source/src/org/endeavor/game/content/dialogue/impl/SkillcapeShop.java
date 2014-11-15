package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class SkillcapeShop extends Dialogue {
	public SkillcapeShop(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		if (next == 0)
			DialogueManager.sendOption(player, new String[] { "Shop A", "Shop B" });
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.getShopping().open(19);
			return true;
		case 9158:
			player.getShopping().open(20);
			return true;
		}

		return false;
	}
}
