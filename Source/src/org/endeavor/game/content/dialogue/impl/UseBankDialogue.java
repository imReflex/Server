package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class UseBankDialogue extends Dialogue {

	public UseBankDialogue(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute() {
		DialogueManager.sendOption(player, "Open bank", "None");
	}

	@Override
	public boolean clickButton(int button) {
		switch (button) {
		case DialogueConstants.OPTIONS_2_1:
			player.getBank().openBank();
			return true;
		case DialogueConstants.OPTIONS_2_2:
			//player.start(new UsePlayerShopDialogue(player));
			return true;
		}
		return false;
	}

}
