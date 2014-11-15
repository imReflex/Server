package org.endeavor.game.content.lottery;

import org.endeavor.GameSettings;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class LotteryDialogue extends Dialogue {
	private final int id;

	public LotteryDialogue(Player player, Mob mob) {
		this.player = player;
		id = mob.getId();
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, id, Emotion.HAPPY_TALK, new String[] {
					"Would you like to join the lottery?", "It's 5M per entry." });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Sure!", "No thanks." });
			break;
		case 2:
			if ((PlayerConstants.isOwner(player)) && (!GameSettings.DEV_MODE)) {
				DialogueManager.sendStatement(player, new String[] { "Owners cannot do this." });
			} else if (!Lottery.canEnter(player)) {
				DialogueManager.sendStatement(player, new String[] { "You can only enter once." });
			} else if (player.getInventory().hasItemAmount(new Item(995, 5000000))) {
				DialogueManager.sendStatement(player, new String[] { "You give 5M to join the drawing." });
				player.getInventory().remove(new Item(995, 5000000));
				Lottery.enter(player);
			} else {
				DialogueManager.sendStatement(player, new String[] { "You do not have 5M." });
			}
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			next = 2;
			execute();
			return true;
		case 9158:
			end();
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		}
		return false;
	}
}
