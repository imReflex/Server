package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;

public class SwitchMagicInterfaceCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		if ((args[1] != null) && (args[1] != "")) {
			try {
				switch (Integer.parseInt(args[1])) {
				case 0:
					player.getMagic().setMagicBook(1151);
					break;
				case 1:
					player.getMagic().setMagicBook(12855);
					break;
				case 2:
					player.getMagic().setMagicBook(29999);
					break;
				default:
					player.getClient().queueOutgoingPacket(new SendMessage("syntax: ::switch or ::switch 0"));
				}

				return;
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(new SendMessage("syntax: ::switch or ::switch 0"));

				return;
			}
		}
		if (player.getMagic().getSpellBookType() == MagicSkill.SpellBookTypes.MODERN)
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.ANCIENT);
		else if (player.getMagic().getSpellBookType() == MagicSkill.SpellBookTypes.ANCIENT)
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.LUNAR);
		else if (player.getMagic().getSpellBookType() == MagicSkill.SpellBookTypes.LUNAR) {
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.MODERN);
		}

		switch (player.getMagic().getSpellBookType().ordinal()) {
		case 0:
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 1151));
			break;
		case 1:
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 12855));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 29999));
		}
	}

	@Override
	public int rightsRequired() {
		return 7;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
