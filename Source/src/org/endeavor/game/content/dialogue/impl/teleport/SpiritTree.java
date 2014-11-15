package org.endeavor.game.content.dialogue.impl.teleport;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class SpiritTree extends Dialogue {
	public static final int NPC_ID = 3636;

	public SpiritTree(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		Emotion e = Emotion.HAPPY_TALK;
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 3636, e, new String[] { "Where would you like to go?" });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Gnome Stronghold", "Seers Village" });
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.teleport(new Location(2461, 3434, 0));
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 9158:
			player.teleport(new Location(2725, 3491, 0));
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		}

		return false;
	}
}
