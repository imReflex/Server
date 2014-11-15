package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.GameSettings;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class YanilleMinigamePortal extends Dialogue {
	public YanilleMinigamePortal(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		if (GameSettings.DEV_MODE){ 
		
			switch (getNext()) {
			case 0:
				DialogueManager.sendOption(player, new String[] { "Duel Arena", "Zombies",
						"Pest Control", "Dungeoneering", "Next" });
				option = 1;
				break;
			case 1:
				getPlayer().getMagic().teleport(3364, 3266, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 2:
				getPlayer().getMagic().teleport(3485, 3250, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 3:
				getPlayer().getMagic().teleport(2662, 2651, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 4:
				getPlayer().getMagic().teleport(1841, 5222, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
				
			case 6:
				DialogueManager.sendOption(player, new String[] { "Barrows", "Fight Cave / Fight Kiln",
						"Fight Pits", "Warriors' guild", "Back" });
				option = 2;
				break;
			case 7:
				getPlayer().getMagic().teleport(3567, 3304, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 8:
				getPlayer().getMagic().teleport(2439, 5169, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 9:
				getPlayer().getMagic().teleport(2399, 5178, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 10:
				getPlayer().getMagic().teleport(2880, 3546, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			}
		} else {
			switch (getNext()) {
			case 0:
				DialogueManager.sendOption(player, new String[] { "Duel Arena", "Warriors' Guild",
						"Pest Control", "Dungeoneering", "Next" });
				break;
			case 1:
				getPlayer().getMagic().teleport(3364, 3266, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 2:
				getPlayer().getMagic().teleport(2880, 3546, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 3:
				getPlayer().getMagic().teleport(2662, 2651, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 4:
				getPlayer().getMagic().teleport(1841, 5222, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
				
			case 6:
				DialogueManager.sendOption(player, new String[] { "Barrows", "Fight Cave / Fight Kiln",
						"Fight Pits", "Back" });
				break;
			case 7:
				getPlayer().getMagic().teleport(3567, 3304, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 8:
				getPlayer().getMagic().teleport(2439, 5169, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			case 9:
				getPlayer().getMagic().teleport(2399, 5178, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				break;
			}
		}
	}

	@Override
	public boolean clickButton(int id) {
		
		if (GameSettings.DEV_MODE){ 
			switch (id) {
			case DialogueConstants.OPTIONS_4_1:
				next = 7;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_2:
				next = 8;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_3:
				next = 9;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_4:
				next = 0;
				execute();
				return true;
			
			case DialogueConstants.OPTIONS_5_1:
				if (option == 2) {
					next = 7;
					execute();
				} else {
					next = 1;
					execute();
				}
				return true;
			case DialogueConstants.OPTIONS_5_2:
				if (option == 2) {
					next = 8;
					execute();
				} else {
					next = 2;
					execute();
				}
				return true;
			case DialogueConstants.OPTIONS_5_3:
				if (option == 2) {
					next = 9;
					execute();
				} else {
					next = 3;
					execute();
				}
				return true;
			case DialogueConstants.OPTIONS_5_4:
				if (option == 2) {
					next = 10;
					execute();
				} else {
					next = 4;
					execute();
				}
				return true;
			case DialogueConstants.OPTIONS_5_5:
				if (option == 2) {
					next = 0;
					execute();
				} else {
					next = 6;
					execute();
				}
				return true;
			}
		} else {
			switch (id) {
			case DialogueConstants.OPTIONS_4_1:
				next = 7;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_2:
				next = 8;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_3:
				next = 9;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_4:
				next = 0;
				execute();
				return true;
			
			case DialogueConstants.OPTIONS_5_1:
				next = 1;
				execute();
				return true;
			case DialogueConstants.OPTIONS_5_2:
				next = 2;
				execute();
				return true;
			case DialogueConstants.OPTIONS_5_3:
				next = 3;
				execute();
				return true;
			case DialogueConstants.OPTIONS_5_4:
				next = 4;
				execute();
				return true;
			case DialogueConstants.OPTIONS_5_5:
				next = 6;
				execute();
				return true;
			}
		}
		
		return false;
	}

}
