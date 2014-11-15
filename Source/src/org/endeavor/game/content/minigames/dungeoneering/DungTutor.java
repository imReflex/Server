package org.endeavor.game.content.minigames.dungeoneering;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.player.Player;

public class DungTutor extends Dialogue {
	public static final int DUNG_TUTOR = 9712;

	public DungTutor(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM,
					new String[] { "Would you like a Dungeoneering overview?" });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Sure!", "No thanks." });
			break;
		case 2:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM,
					new String[] { "Let me know if you change your mind." });
			end();
			break;
		case 3:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
					"In Dungeoneering, you or your team will face", "waves of monsters." });
			next += 1;
			break;
		case 4:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
					"Each wave will also contain atleast one chest.", "The chests have a 20% change of containing",
					"a trap." });
			next += 1;
			break;
		case 5:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
					"You need 85 theiving to search the chest for traps.",
					"You will gain Theiving experience for searching them." });
			next += 1;
			break;
		case 6:
			DialogueManager
					.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
							"Some waves will also contain Fishing spots.",
							"You only have 15 seconds per wave to fish there." });
			next += 1;
			break;
		case 7:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM,
					new String[] { "You must bring your own Harpoon or Lobster pot to fish.",
							"They are not dropped in Dungeoneering." });
			next += 1;
			break;
		case 8:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
					"You must have the required level to fish there,",
					"and you will gain Experience for what you catch and cook." });
			next += 1;
			break;
		case 9:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
					"The monsters will drop ingredients to make potions.",
					"You must have the required level to make them." });
			next += 1;
			break;
		case 10:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] {
					"It is important to choose your team wisely.",
					"Having team mates with the levels to Fish, Theive,",
					"and make Potions is critical to your survival." });
			next += 1;
			break;
		case 11:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] { "Every 5th wave is a boss wave.",
					"The bosses get stronger as the waves get higher." });
			next += 1;
			break;
		case 12:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM,
					new String[] { "It is important to reserve resources to fight them." });
			next += 1;
			break;
		case 13:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] { "That should be everything.",
					"Remember to also enjoy yourself and have fun!" });
			next += 1;
			break;
		case 14:
			DialogueManager.sendNpcChat(player, 9712, Emotion.CALM, new String[] { "Happy hunting." });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			next = 3;
			execute();
			return true;
		case 9158:
			next = 2;
			execute();
			return true;
		}
		return false;
	}
}
