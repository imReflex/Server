package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;

public class TrainingTeleport extends Dialogue {
	public TrainingTeleport(Player player) {
		setPlayer(player);
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), new String[] { "Basic training", "Taverly dungeon",
					"Daganoth cave", "Experiments", "More options" });
			setOption(0);
			break;
		case 1:
			getPlayer().getMagic().teleport(3161, 4237, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 2:
			getPlayer().getMagic().teleport(2884, 9798, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 3:
			getPlayer().getMagic().teleport(2443, 10147, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 4:
			getPlayer().getMagic().teleport(3577, 9927, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 5:
			DialogueManager.sendOption(getPlayer(), new String[] { "Slayer Dungeon", "Slayer tower",
					"Brimhaven dungeon", "Lumbridge swamp", "Back" });
			setOption(1);
			end();
			break;
		case 6:
			getPlayer().getMagic().teleport(3203, 9381, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 7:
			getPlayer().getMagic().teleport(3428, 3538, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 8:
			getPlayer().getMagic().teleport(2712, 9564, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 9:
			getPlayer().getMagic().teleport(3168, 9572, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
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
