package org.endeavor.game.content.minigames.fightcave;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class SelectGameDialogue extends Dialogue {

	public SelectGameDialogue(Player player) {
		this.player = player;
	}
	
	@Override
	public void execute() {
		DialogueManager.sendOption(player, "Fight Cave", "Fight Kiln");
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case DialogueConstants.OPTIONS_2_1:
			TzharrGame.init(player, false);
			return true;
		case DialogueConstants.OPTIONS_2_2:
			TzharrGame.init(player, true);
			return true;
		}
		return false;
	}

}
