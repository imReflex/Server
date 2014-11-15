package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class SkillingTeleport extends Dialogue {
	public SkillingTeleport(Player player) {
		setPlayer(player);
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), new String[] { "Catherby (Farm, Fish)", "Falador Mine", "Gnome Stronghold (Woodcut, Agility, Fletch)",
					"Fishing Guild", "Next" });
			setOption(0);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), new String[] { "Rouges' Den (Theiving)", "Draynor (Woodcut, Fish, Theive)", "Rellekka (Theive)",
				"Al Kharid (Jewelry craft, Smelt, Mine)", "Next" });
			setOption(1);
			break;
		case 2:
			DialogueManager.sendOption(getPlayer(), new String[] { "Summoning", "Slayer Master", "Runecraft",
				"Herblore", "No where" });
			setOption(2);
			break;
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9190:
			switch (option) {
			case 0:
				getPlayer().getMagic().teleport(2804, 3434, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 1:
				getPlayer().getMagic().teleport(3047, 4969, 2, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 2:
				getPlayer().getMagic().teleport(2926, 3444, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			}
			
			return true;
		case 9191:
			switch (option) {
			case 0:
				getPlayer().getMagic().teleport(3056, 9777, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 1:
				getPlayer().getMagic().teleport(3092, 3248, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 2:
				getPlayer().getMagic().teleport(2603, 3101, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			}
			
			return true;
		case 9192:
			switch (option) {
			case 0:
				getPlayer().getMagic().teleport(2446, 3432, 1, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 1:
				getPlayer().getMagic().teleport(2646, 3675, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 2:
				getPlayer().getMagic().teleport(3109, 3559, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			}
			
			return true;
		case 9193:
			switch (option) {
			case 0:
				getPlayer().getMagic().teleport(2590, 3420, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 1:
				getPlayer().getMagic().teleport(3277, 3185, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 2:
				getPlayer().getMagic().teleport(2899, 3427, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			}
			
			return true;
		case 9194:
			switch (option) {
			case 0:
				next = 1;
				execute();
			
				break;
			case 1:
				next = 2;
				execute();
				break;
			case 2:
				player.send(new SendRemoveInterfaces());
				end();
				break;
			}
			
			return true;
		}

		return false;
	}
}
