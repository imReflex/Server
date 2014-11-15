package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class ChangeBookDialogue extends Dialogue {
	public ChangeBookDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendOption(player, new String[] { "Change Magic book", "Change Prayer book" });
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Modern", "Ancient", "Lunar" });
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendMessage("You convert to Modern magicks."));
			player.getMagic().setMagicBook(1151);
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.MODERN);
			end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendMessage("You convert to Ancient magicks."));
			player.getMagic().setMagicBook(12855);
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.ANCIENT);
			end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendMessage("You convert to Lunar magicks."));
			player.getMagic().setMagicBook(29999);
			player.getMagic().setSpellBookType(MagicSkill.SpellBookTypes.LUNAR);
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			end();
			break;
		case 5:
			DialogueManager.sendOption(player, new String[] { "Default", "Curses" });
			option = 2;
			end();
			break;
		case 6:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			PrayerBook.setBook(player, PrayerBook.PrayerBookType.DEFAULT);
			end();
			break;
		case 7:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			PrayerBook.setBook(player, PrayerBook.PrayerBookType.CURSES);
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			if (option == 2) {
				setNext(6);
				execute();
				return true;
			}
			setNext(1);
			execute();
			return true;
		case 9158:
			if (option == 2) {
				setNext(7);
				execute();
				return true;
			}
			setNext(5);
			execute();
			return true;
		case 9167:
			setNext(2);
			execute();
			return true;
		case 9168:
			setNext(3);
			execute();
			return true;
		case 9169:
			setNext(4);
			execute();
			return true;
		}
		return false;
	}
}
