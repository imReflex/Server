package org.endeavor.game.content.minigames.dungeoneering;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class DungExiterDialogue extends Dialogue {
	public DungExiterDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 650, Emotion.CALM,
					new String[] { "Would you like to exit Dungeoneering?" });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Yes.", "No." });
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			if (player.getDungGame() != null) {
				player.getDungGame().remove(player);
			}
			end();
			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		}
		return false;
	}
}
