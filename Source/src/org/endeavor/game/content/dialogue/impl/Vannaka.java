package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.skill.slayer.Slayer;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class Vannaka extends Dialogue {
	public static final int VANNAKA = 1597;
	private final boolean toTask;

	public Vannaka(Player player, boolean toTask) {
		this.player = player;
		this.toTask = toTask;
	}

	@Override
	public void execute() {
		if ((next < 2) && (toTask)) {
			next = 2;
		}

		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, 1597, Emotion.CALM,
					new String[] { "How can I help you, " + player.getUsername() + "?" });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "I would like a Slayer task.",
					"I want to go to the Slayer Tower.", "Can you reset my Slayer task?" });
			option = 1;
			break;
		case 2:
			if (player.getSlayer().hasTask()) {
				DialogueManager.sendNpcChat(player, 1597, Emotion.CALM, new String[] {
						"I'm sorry, you already have a task.", "You must complete the current task." });
				end();
			} else {
				DialogueManager.sendOption(player, new String[] { "Low-level", "Medium-level", "High-level" });
				option = 2;
			}
			end();
			break;
		case 3:
			player.teleport(new Location(3428, 3538));
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			end();
			break;
		case 4:
			String task = player.getSlayer().getTask();
			byte am = player.getSlayer().getAmount();
			DialogueManager.sendNpcChat(player, 1597, Emotion.CALM, new String[] { "Your new task is to kill " + am
					+ " " + task + "s." });
			QuestTab.updateSlayerTask(player);
			end();
			break;
		case 5:
			DialogueManager.sendNpcChat(player, 1597, Emotion.CALM, new String[] {
					"It costs " + (player.isRespectedMember() ? "10 rxp" : (player.isSuperMember() ? "25 rxp" : (player.isMember() ? "50 rxp" : "100 rxp"))) + " to reset your task.",
					"Are you sure you want to?" });
			next += 1;
			break;
		case 6:
			DialogueManager.sendOption(player, new String[] { "Yes.", "No." });
			break;
		case 7:
			if (!player.getSlayer().hasTask()) {
				DialogueManager.sendStatement(player, new String[] { "You do not have a Slayer task." });
				end();
			} else if (player.getRXP() < (player.isRespectedMember() ? 10 : (player.isSuperMember() ? 25 : (player.isMember() ? 50 : 100)))) {
				DialogueManager.sendStatement(player, new String[] { "You do not have " + 
						(player.isRespectedMember() ? "10 rxp" : (player.isSuperMember() ? "25 rxp" : (player.isMember() ? "50 rxp" : "100 rxp"))) + "." });
				end();
			} else {
				player.getSlayer().reset();
				QuestTab.updateSlayerPoints(player);
				DialogueManager.sendStatement(player, new String[] { "Your Slayer task has been reset." });
				//player.getInventory().remove(995, (player.isRespectedMember() ? 10 : (player.isSuperMember() ? 25 : (player.isMember() ? 50 : 100))), true);
				player.subtractRxp((player.isRespectedMember() ? 10 : (player.isSuperMember() ? 25 : (player.isMember() ? 50 : 100))));
				end();
			}
			break;
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			next = 7;
			execute();
			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		case 9167:
			if (option == 2) {
				if (player.getSlayer().hasTask()) {
					DialogueManager.sendNpcChat(player, 1597, Emotion.CALM, new String[] {
							"I'm sorry, you already have a task.", "You must complete the current task." });
					end();
				} else {
					player.getSlayer().assign(Slayer.SlayerDifficulty.LOW);
					next = 4;
					execute();
				}
			} else {
				next = 2;
				execute();
			}
			return true;
		case 9168:
			if (option == 2) {
				if (player.getSlayer().hasTask()) {
					DialogueManager.sendNpcChat(player, 1597, Emotion.CALM, new String[] {
							"I'm sorry, you already have a task.", "You must complete the current task." });
					end();
				} else {
					player.getSlayer().assign(Slayer.SlayerDifficulty.MEDIUM);
					next = 4;
					execute();
				}
			} else {
				next = 3;
				execute();
			}
			return true;
		case 9169:
			if (option == 2) {
				if (player.getSlayer().hasTask()) {
					DialogueManager.sendNpcChat(player, 1597, Emotion.CALM, new String[] {
							"I'm sorry, you already have a task.", "You must complete the current task." });
					end();
				} else {
					player.getSlayer().assign(Slayer.SlayerDifficulty.HIGH);
					next = 4;
					execute();
				}
			} else {
				next = 5;
				execute();
			}
			return true;
		}
		return false;
	}
}
