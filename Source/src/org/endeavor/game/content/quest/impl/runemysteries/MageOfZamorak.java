package org.endeavor.game.content.quest.impl.runemysteries;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.player.Player;

public class MageOfZamorak extends Dialogue {
	public static final int MAGE = 2259;

	public MageOfZamorak(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		if ((!player.getQuesting().isQuestActive(QuestConstants.RUNE_MYSTERIES))
				&& (!player.getQuesting().isQuestCompleted(QuestConstants.RUNE_MYSTERIES))) {
			switch (next) {
			case 0:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] { "Zamorak blesses you." });
				next += 1;
				break;
			case 1:
				DialogueManager.sendOption(player, new String[] { "Thanks?", "Do you have any quests for me?" });
				break;
			case 2:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CRAZY,
						new String[] { "Don't thank me, thank our lord!" });
				end();
				break;
			case 3:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM,
						new String[] { "Ah, you must be " + player.getUsername() + "." });
				next += 1;
				break;
			case 4:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM,
						new String[] { "The prophecies said I'd meet you one day." });
				next += 1;
				break;
			case 5:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM,
						new String[] { "You see, " + player.getUsername() + ".",
								"Long ago when the gods created RevolutionX", "they created with it a great power." });
				next += 1;
				break;
			case 6:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] {
						"Anyone who holds this power has the ability to create", "runes from an ancient substance",
						"known as the Rune Essence." });
				next += 1;
				break;
			case 7:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] {
						"The Rune Essence holds a great power.", "No one knows why it is so powerful,",
						"but what I do know is you are the one", "who must hold this power." });
				next += 1;
				break;
			case 8:
				DialogueManager.sendPlayerChat(player, Emotion.CONFUSED, new String[] { "But why me?" });
				next += 1;
				break;
			case 9:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] { "I'm not sure.",
						"But you must not take this lightly." });
				next += 1;
				break;
			case 10:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM,
						new String[] { "I need you to take this talisman to the mage in Falador." });
				next += 1;
				break;
			case 11:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM,
						new String[] { "He will know what to do with it." });
				next += 1;
				break;
			case 12:
				DialogueManager.sendStatement(player, new String[] { "He hands you the talisman." });
				end();
				player.getInventory().addOrCreateGroundItem(1438, 1, true);
				player.getQuesting().setQuestActive(QuestConstants.RUNE_MYSTERIES, true);
				player.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
			default:
				break;
			}
		} else if (player.getQuesting().isQuestCompleted(QuestConstants.RUNE_MYSTERIES)) {
			DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] { "I've tought you all I can.",
					"I can now teleport you to the Abyss." });
			end();
		} else if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 1) {
			switch (next) {
			case 0:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] { "What is it my child?" });
				next += 1;
				break;
			case 1:
				DialogueManager.sendOption(player,
						new String[] { "I lost the talisman!", "What do I have to do again?" });
				option = 2;
				break;
			case 2:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM, new String[] {
						"Thankfully I have another one!", "Please, do not lose it again." });
				next += 1;
				break;
			case 3:
				DialogueManager.sendStatement(player, new String[] { "He hands you the talisman." });
				end();
				player.getInventory().addOrCreateGroundItem(1438, 1, true);
				break;
			case 4:
				DialogueManager.sendNpcChat(player, 2259, Emotion.CALM,
						new String[] { "I need you to take this talisman to the mage in Falador.",
								"He will know what to do with it." });
				end();
			}
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			if (option == 2) {
				next = 2;
				execute();
			} else {
				next = 2;
				execute();
			}
			break;
		case 9158:
			if (option == 2) {
				next = 4;
				execute();
			} else {
				next = 3;
				execute();
			}
			break;
		}
		return false;
	}
}
