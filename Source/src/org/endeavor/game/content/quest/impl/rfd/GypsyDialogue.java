package org.endeavor.game.content.quest.impl.rfd;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.player.Player;

public class GypsyDialogue extends Dialogue {
	public static final int GYPSY = 3385;

	public GypsyDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			if (player.getQuesting().isQuestCompleted(QuestConstants.RECIPE_FOR_DISASTER)) {
				DialogueManager.sendNpcChat(player, 3385, Emotion.HAPPY_TALK,
						new String[] { "Thanks for killing those evil things!" });
				end();
			} else {
				DialogueManager.sendNpcChat(player, 3385, Emotion.SCARED, new String[] { "Hurry you must help!",
						"The bridge between this world and the next", "is being destroyed by evil creatures!" });
				next += 1;
			}
			break;
		case 1:
			DialogueManager.sendNpcChat(player, 3385, Emotion.SCARED,
					new String[] { "You must enter the portal and stop them!" });
			next += 1;
			break;
		case 2:
			DialogueManager.sendOption(player, new String[] { "I'm on it!", "Ahhhhhh!" });
			option = 0;
			break;
		case 3:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, new String[] { "I'm on it!" });
			end();
			break;
		case 4:
			DialogueManager.sendPlayerChat(player, Emotion.SCARED, new String[] { "Ahhhhhh!" });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		if (id == 9157) {
			next = 3;
			execute();
			return true;
		}
		if (id == 9158) {
			next = 4;
			execute();
			return true;
		}
		return false;
	}
}
