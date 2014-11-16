package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.GameSettings;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class BossesTeleport extends Dialogue {
	public BossesTeleport(Player player) {
		setPlayer(player);
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(getPlayer(), new String[] { "King Black Dragon",
					"Corporeal Beast", "Tormented demons", "God Wars", "Next" });
			setOption(0);
			break;
		case 1:
			getPlayer().getMagic().teleport(2258, 4683, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 2:
			getPlayer().getMagic().teleport(3404, 3090, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 3:
			getPlayer().getMagic().teleport(3106, 3959, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 4:
			//getPlayer().getMagic().teleport(2882, 5310, 2, MagicSkill.TeleportTypes.SPELL_BOOK);
			getPlayer().getClient().queueOutgoingPacket(new SendMessage("Godwars is Bugged, Need to fix"));
			end();
			break;
		case 5:
			DialogueManager.sendOption(getPlayer(), new String[] { "Bork", "Dagganoth kings",
					"Desert Strykeworms", "Back", "Next" });
			setOption(1);
			break;
		case 6:
			getPlayer().getMagic().teleport(1748, 5324, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 7:
			getPlayer().getMagic().teleport(2899, 4451, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 8:
			getPlayer().getMagic().teleport(3331, 3654, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 9:
			getPlayer().getMagic().teleport(2802, 10093, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 10:
			DialogueManager.sendOption(getPlayer(), new String[] { "Frost dragons", "Kalphite Queen",
					"Balance Elementals (Premium)", "Sea Troll Queen", "Back" });
			option = 2;
			end();
			break;
		case 11:
			getPlayer().getMagic().teleport(3508, 9493, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		case 12:
			getPlayer().getMagic().teleport(3017, 10248, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			end();
			break;
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9190:
			if (option == 1) {
				next = 6;
				execute();
				return true;
			} else if (option == 2/**3rd page*/) {
				next = 9;
				execute();
				return true;
			}

			next = 1;
			execute();
			return true;
		case 9191:
			if (option == 1) {
				next = 7;
				execute();
				return true;
			} else if (option == 2/**3rd page*/) {
				next = 11;
				execute();
				return true;
			}

			next = 2;
			execute();
			return true;
		case 9192:
			if (option == 1) {
				next = 8;
				execute();
				return true;
			} else if (option == 2/**3rd page*/) {
				if (!player.isMember()) {
					DialogueManager.sendStatement(player, new String[] { "This boss is only for members!", "Visit the Knowledge base on the forum to", 
							"learn the benefits of purchasing Premium."});
					end();
					return true;
				}
				
				getPlayer().getMagic().teleport(2441, 9416, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				return true;
			}

			next = 3;
			execute();
			return true;
		case 9193:
			if (option == 1) {
				next = 0;
				execute();
				return true;
			} else if (option == 2/**3rd page*/) {
				//sea troll
				getPlayer().getMagic().teleport(2338, 3688, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				end();
				return true;
			}

			next = 4;
			execute();
			return true;
		case 9194:
			if (option == 1) {
				next = 10;
				execute();
				return true;
			} else if (option == 2/**3rd page*/) {
				next = 5;
				execute();
				return true;
			}

			next = 5;
			execute();
			return true;
		}
		return false;
	}
}
