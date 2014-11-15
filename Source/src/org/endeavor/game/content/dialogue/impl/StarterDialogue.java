package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class StarterDialogue extends Dialogue {

	public StarterDialogue(Player player) {
		this.player = player;
		player.setController(Tutorial.TUTORIAL_CONTROLLER);
	}
	
	@Override
	public void execute() {
		DialogueManager.sendOption(player, "Pure Pker", "Main Pker", "Main Skiller/PvM");
	}

	@Override
	public boolean clickButton(int id) {
		switch(id) {
		case DialogueConstants.OPTIONS_3_1:
			player.send(new SendMessage("You are now a pure pker!"));
			PlayerConstants.doStarter(player, 1);
			DialogueManager.sendStatement(player, "You may change your skills by talking to Mandrith!", "You can spawn certain items by using ::item id amount");
			end();
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			return false;
		case DialogueConstants.OPTIONS_3_2:
			player.send(new SendMessage("You are now a main pker!"));
			PlayerConstants.doStarter(player, 2);
			DialogueManager.sendStatement(player, "You may change your skills by talking to Mandrith!", "You can spawn certain items by using ::item id amount");
			end();
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			return false;
		case DialogueConstants.OPTIONS_3_3:
			player.send(new SendMessage("You are now a main skiller/pvmer!"));
			PlayerConstants.doStarter(player, 3);
			DialogueManager.sendStatement(player, "You may change your skills by talking to Mandrith!", "You can spawn certain items by using ::item id amount");
			end();
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			return false;
		}
		return false;
	}

}
