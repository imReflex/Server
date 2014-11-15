package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;

public class TownTeleport extends Dialogue {
	public TownTeleport(Player player) {
		setPlayer(player);
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), new String[] { "Falador", "Rellekka", "Lumbridge", "Al Kharid",
					"Draynor" });
			setOption(0);
			break;
		case 1:
			getPlayer().getMagic().teleport(2965, 3383, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 2:
			getPlayer().getMagic().teleport(2658, 3657, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 3:
			getPlayer().getMagic().teleport(3222, 3218, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 4:
			getPlayer().getMagic().teleport(3293, 3183, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 5:
			getPlayer().getMagic().teleport(3092, 3248, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			setOption(1);
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9190:
			switch (getOption()) {
			case 0:
				setNext(1);
				execute();
				return true;
			case 1:
				setNext(6);
				execute();
				return true;
			}
			return true;
		case 9191:
			switch (getOption()) {
			case 0:
				setNext(2);
				execute();
				return true;
			case 1:
				setNext(7);
				execute();
				return true;
			}
			return true;
		case 9192:
			switch (getOption()) {
			case 0:
				setNext(3);
				execute();
				return true;
			case 1:
				setNext(8);
				execute();
				return true;
			}
			return true;
		case 9193:
			switch (getOption()) {
			case 0:
				setNext(4);
				execute();
				return true;
			case 1:
				setNext(9);
				execute();
				return true;
			}
			return true;
		case 9194:
			switch (getOption()) {
			case 0:
				setNext(5);
				execute();
				return true;
			case 1:
				setNext(0);
				execute();
				return true;
			}
			return true;
		}
		return false;
	}
}
