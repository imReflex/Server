package org.endeavor.game.content.clanchat;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class EditClanDialogue extends Dialogue {
	
	public EditClanDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendOption(player, "Edit name", "Edit ranks", "Edit join permissions");
			break;
		}
	}

	@Override
	public boolean clickButton(int id) {
		
		return false;
	}
	
	

}
