package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.player.Player;

public class SkillMaster extends Dialogue {
	public SkillMaster(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendOption(player, new String[] { "View shop", "Exchange for points",
					"View Skill points tutorial" });
			option = 0;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "x1", "x10", "x100", "All" });
			option = 1;
			break;
		case 2:
			DialogueManager.sendNpcChat(player, 805, Emotion.HAPPY_TALK, new String[] {
					"To obtain Skill points you exchange", "10 noted Raw rocktails, 10 noted Magic logs,",
					"and 10 noted Runite ore." });
			next += 1;
			break;
		case 3:
			DialogueManager.sendNpcChat(player, 805, Emotion.HAPPY_TALK, new String[] {
					"You can use these points to purchase items", "in my shop." });
			next = 0;
		}
	}

	public void exchange(int amount) {
		int a1 = player.getInventory().getItemAmount(15271) / 10;
		int a2 = player.getInventory().getItemAmount(1514) / 10;
		int a3 = player.getInventory().getItemAmount(452) / 10;

		if ((a1 < 1) || (a2 < 1) || (a3 < 1)) {
			DialogueManager.sendStatement(player, new String[] {
					"You need 10 noted Raw rocktails, 10 noted Magic logs,",
					"and 10 noted Runite ore to get any points." });
			end();
			return;
		}

		if (amount == -1) {
			amount = a1;

			if (amount > a2) {
				amount = a2;
			}

			if (amount > a3) {
				amount = a3;
			}
		} else {
			if (amount > a1) {
				amount = a1;
			}

			if (amount > a2) {
				amount = a2;
			}

			if (amount > a3) {
				amount = a3;
			}
		}

		player.getInventory().remove(15271, 10 * amount, false);
		player.getInventory().remove(1514, 10 * amount, false);
		player.getInventory().remove(452, 10 * amount, true);

		DialogueManager.sendStatement(player, new String[] { "You exchange " + (amount * 10) + " materials for " + amount
				+ " points." });

		player.setSkillPoints(player.getSkillPoints() + amount);
		QuestTab.updateSkillPoints(player);
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9178:
			exchange(1);
			break;
		case 9179:
			exchange(10);
			break;
		case 9180:
			exchange(100);
			break;
		case 9181:
			exchange(-1);
			break;
		case 9167:
			player.getShopping().open(90);
			break;
		case 9168:
			next = 1;
			execute();
			break;
		case 9169:
			next = 2;
			execute();
		case 9170:
		case 9171:
		case 9172:
		case 9173:
		case 9174:
		case 9175:
		case 9176:
		case 9177:
		}
		return false;
	}
}
