package org.endeavor.game.content.minigames.duelarena;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class DuelArenaForfeit extends Dialogue {
	public DuelArenaForfeit(Player player) {
		setPlayer(player);
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendStatement(getPlayer(), new String[] { "Are you sure you would like to forfeit?" });
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), new String[] { "Yes", "No" });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			if (!getPlayer().getDueling().isDueling()) {
				return true;
			}
			getPlayer().getDueling().onDuelEnd(true, false);
			getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		case 9158:
			getPlayer().getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		}

		return false;
	}
}
