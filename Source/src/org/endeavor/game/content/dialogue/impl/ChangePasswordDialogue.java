package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class ChangePasswordDialogue extends Dialogue {
	private final String password;

	public ChangePasswordDialogue(Player player, String password) {
		this.player = player;
		this.password = password;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendStatement(player, new String[] { "Your new password will be:", "'" + password + "'",
					"Any trailing whitespace has been trimmed.", "Are you sure you want to make this change?" });
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
			player.setPassword(password);
			DialogueManager.sendStatement(player, new String[] { "Your password will now be:", "'" + password + "'" });
			end();
			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		}
		return false;
	}
}
