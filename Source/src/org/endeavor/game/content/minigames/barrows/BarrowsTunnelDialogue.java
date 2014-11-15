package org.endeavor.game.content.minigames.barrows;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class BarrowsTunnelDialogue extends Dialogue {
	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendStatement(getPlayer(),
					new String[] { "You've found a hidden tunnel, do you want to enter?" });
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), new String[] { "Yes, I'm fearless!", "No way, that looks scary!" });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			player.teleport(new Location(3552, 9690));
			getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 9158:
			getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		}

		return false;
	}
}
