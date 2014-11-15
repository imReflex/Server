package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class Shantay extends Dialogue {
	
	public Shantay(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 836, Emotion.HAPPY_TALK, "Would you like a Shantay pass for 12,000 gp?");
			next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Sure!", "No thanks.");
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, "No thanks.");
			end();
			break;
		case 3:
			Item gold = new Item(995, 12000);
			Item pass = new Item(1854, 1);
			
			if (!player.getInventory().hasItemAmount(new Item(gold))) {
				DialogueManager.sendStatement(player, "You do not have 12,000 gp.");
				end();
			} else if (!player.getInventory().hasSpaceOnRemove(new Item(gold), new Item(pass))) {
				DialogueManager.sendStatement(player, "You do not have enough inventory space.");
				end();
			} else {
				player.getInventory().remove(new Item(gold), false);
				player.getInventory().add(new Item(pass), true);
				DialogueManager.sendStatement(player, "You purchase a Shantay pass.");
				end();
			}
			break;
		}
			
		
	}
	
	public static Item getShantayPass() {
		return new Item(1854);
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case DialogueConstants.OPTIONS_2_1:
			next = 3;
			execute();
			return true;
		case DialogueConstants.OPTIONS_2_2:
			next = 2;
			execute();
			return true;
		}
		return false;
	}

}
