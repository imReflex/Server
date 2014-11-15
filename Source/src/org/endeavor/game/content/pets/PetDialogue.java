package org.endeavor.game.content.pets;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;

public class PetDialogue extends Dialogue {
	private final int id;

	public PetDialogue(int id) {
		this.id = id;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			String[] chat = Pets.getPetChat(id);

			if (chat == null) {
				end();
				return;
			}

			DialogueManager.sendNpcChat(getPlayer(), id, Emotion.HAPPY_TALK,
					new String[] { chat[org.endeavor.engine.utility.Misc.randomNumber(chat.length)] });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}
}
