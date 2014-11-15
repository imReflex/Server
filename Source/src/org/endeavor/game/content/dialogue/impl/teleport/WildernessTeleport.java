package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.impl.WildernessWarning;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

public class WildernessTeleport extends Dialogue {
	public WildernessTeleport(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Edgeville Wilderness", "Mage Bank", "Clan Wars", "Green dragons - lvl 9",
				"Bounty Hunter Crater - lvl 19" });
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9190:
			player.start(new WildernessWarning(player) {
				@Override
				public void onConfirm() {
					getPlayer().getMagic().teleport(3089, 3524, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			break;
		case 9191:
			player.start(new WildernessWarning(player) {
				@Override
				public void onConfirm() {
					getPlayer().getMagic().teleport(2540, 4716, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			break;
		case 9192:
			player.start(new WildernessWarning(player) {
				@Override
				public void onConfirm() {
					Clan clan = Clans.getClanForOwner(getPlayer());
					if(clan != null) {
						System.out.println("clan challenge option");
						getPlayer().getClient().queueOutgoingPacket(new SendPlayerOption("null", 3));
						getPlayer().getClient().queueOutgoingPacket(new SendPlayerOption("Challenge", 4));
					}
					getPlayer().getMagic().teleport(3274, 3684, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			break;
		case 9193:
			player.start(new WildernessWarning(player) {
				@Override
				public void onConfirm() {
					getPlayer().getMagic().teleport(2981, 3595, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			break;
		case 9194:
			player.start(new WildernessWarning(player) {
				@Override
				public void onConfirm() {
					getPlayer().getMagic().teleport(3082, 3670, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}

			});
		}

		return false;
	}
}
