package org.endeavor.game.content.quest.impl.hftd;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class JossikDialogue extends Dialogue {
	public static final int JOSSIK_ID = 1334;

	public JossikDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			if (player.getQuesting().isQuestCompleted(QuestConstants.HORROR_FROM_THE_DEEP)) {
				DialogueManager.sendNpcChat(player, 1334, Emotion.HAPPY_TALK, new String[] { "Thank you so much!",
						"We are forever in your debt." });
				next = 7;
			} else if (!player.getQuesting().isQuestActive(QuestConstants.HORROR_FROM_THE_DEEP)) {
				DialogueManager.sendNpcChat(player, 1334, Emotion.SCARED, new String[] { "Please, you must help!",
						"My wife went inside the lighthouse and has not returned!" });
				next = 1;
			} else {
				DialogueManager.sendNpcChat(player, 1334, Emotion.SCARED, new String[] { "Please, you must hurry!",
						"She is still trapped down there!" });
				end();
			}
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "I will help her!", "I'm not going down there!" });
			break;
		case 2:
			DialogueManager.sendPlayerChat(player, Emotion.CALM, new String[] { "I will help her!" });
			next = 4;
			break;
		case 3:
			DialogueManager.sendPlayerChat(player, Emotion.SCARED, new String[] { "I'm not going down there!" });
			next = 5;
			break;
		case 4:
			DialogueManager.sendNpcChat(player, 1334, Emotion.CALM, new String[] { "Thank you!",
					"You must go to The Seer in Seers' Village to obtain the key!" });
			player.getQuesting().setQuestActive(QuestConstants.HORROR_FROM_THE_DEEP, true);
			player.getQuesting().incrQuestStage(QuestConstants.HORROR_FROM_THE_DEEP);
			end();
			break;
		case 5:
			DialogueManager
					.sendNpcChat(player, 1334, Emotion.SCARED, new String[] { "Please, you are her only hope!" });
			end();
			break;
		case 7:
			DialogueManager.sendNpcChat(player, 1334, Emotion.HAPPY_TALK,
					new String[] { "Would you like a free Prayer book?" });
			next = 8;
			break;
		case 8:
			DialogueManager.sendOption(player, new String[] { "Sure!", "No thanks." });
			option = 1;
			break;
		case 9:
			DialogueManager.sendOption(player, new String[] { "Holy book", "Book of Balance", "Unholy book" });
			break;
		case 10:
			DialogueManager.sendPlayerChat(player, Emotion.HAPPY_TALK, new String[] { "No thanks." });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			player.getInventory().addOrCreateGroundItem(3840, 1, true);
			break;
		case 9168:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			player.getInventory().addOrCreateGroundItem(3844, 1, true);
			break;
		case 9169:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			player.getInventory().addOrCreateGroundItem(3842, 1, true);
			break;
		case 9157:
			if (option == 1) {
				next = 9;
				execute();
				return true;
			}

			next = 2;
			execute();
			return true;
		case 9158:
			if (option == 1) {
				next = 10;
				execute();
				return true;
			}

			next = 3;
			execute();
			return true;
		}
		return false;
	}
}
