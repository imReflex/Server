package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;

public class HomeTeleport extends Dialogue {
	public HomeTeleport(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(player, new String[] { "Edgeville", "Catherby" });
			break;
		case 1:
			getPlayer().getMagic().teleport(3087, 3491, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 2:
			getPlayer().getMagic().teleport(2804, 3434, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			setNext(1);
			execute();
			return true;
		case 9158:
			setNext(2);
			execute();
			return true;
		}
		return false;
	}
}
