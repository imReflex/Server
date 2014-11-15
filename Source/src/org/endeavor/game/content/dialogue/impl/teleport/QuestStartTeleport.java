package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill.TeleportTypes;
import org.endeavor.game.entity.player.Player;

public class QuestStartTeleport extends Dialogue {
	
	public QuestStartTeleport(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, "Recipe For Disaster", "Horror from the Deep", "Rune Mysteries");
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case DialogueConstants.OPTIONS_3_1:
			player.getMagic().teleport(3212, 9619, 0, TeleportTypes.SPELL_BOOK);
			end();
			return true;
		case DialogueConstants.OPTIONS_3_2:
			player.getMagic().teleport(2602, 3608, 0, TeleportTypes.SPELL_BOOK);
			end();
			return true;
		case DialogueConstants.OPTIONS_3_3:
			player.getMagic().teleport(3109, 3559, 0, TeleportTypes.SPELL_BOOK);
			end();
			return true;
		}
		return false;
	}

}
