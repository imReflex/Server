package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class MeleeInstructor extends Dialogue {
	public static final int INSTRUCTOR = 705;
	private boolean a = false;

	public MeleeInstructor(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		Emotion e = Emotion.HAPPY_TALK;
		switch (next) {
		case 0:
			DialogueManager.sendOption(player, new String[] { "Reset all combat levels.", "Reset Defence level.",
					"Reset Attack level.", "Reset Prayer level.",
					!player.getSkill().isExpLocked() ? "Lock experience." : "Unlock experience." });
			break;
		case 1:
			if (!player.getSkill().isExpLocked()) {
				DialogueManager.sendNpcChat(player, 705, e, new String[] { "Your experience is now locked.",
						"Come back to unlock it." });
				player.getSkill().setExpLock(true);
				QuestTab.updateExpLock(player);
			} else {
				DialogueManager.sendNpcChat(player, 705, e, new String[] { "Your experience is now unlocked.",
						"Come back to lock it." });
				player.getSkill().setExpLock(false);
				QuestTab.updateExpLock(player);
			}
			end();
			break;
		case 2:
			DialogueManager.sendStatement(player, new String[] { "Are you sure?", "This cannot be undone." });
			next = 3;
			break;
		case 3:
			DialogueManager.sendOption(player, new String[] { "Yes.", "No." });
			break;
		case 4:
			DialogueManager.sendStatement(player, new String[] { "Are you sure?",
					"This costs 1 million gp and cannot be undone." });
			next = 3;
			a = true;
		}
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			if (a) {
				if (player.getInventory().hasItemAmount(995, 1000000)) {
					if (!ItemCheck.hasEquipmentOn(player)) {
						player.getInventory().remove(995, 1000000);
						if (option == 2)
							player.getSkill().reset(1);
						else if (option == 3)
							player.getSkill().reset(0);
						else if (option == 4) {
							player.getSkill().reset(5);
						}

						player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					} else {
						DialogueManager.sendStatement(player,
								new String[] { "You must remove your equipment before doing this." });
						end();
					}
				} else {
					DialogueManager.sendStatement(player, new String[] { "You do not have 1 million gp." });
					end();
				}
			} else if (!ItemCheck.hasEquipmentOn(player)) {
				for (int i = 0; i <= 6; i++) {
					player.getSkill().reset(i);
				}

				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			} else {
				DialogueManager.sendStatement(player,
						new String[] { "You must remove your equipment before doing this." });
				end();
			}

			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		case 9190:
			option = 1;
			next = 2;
			execute();
			return true;
		case 9191:
			option = 2;
			next = 4;
			execute();
			return true;
		case 9192:
			option = 3;
			next = 4;
			execute();
			return true;
		case 9193:
			option = 4;
			next = 4;
			execute();
			return true;
		case 9194:
			next = 1;
			execute();
			return true;
		}
		return false;
	}
}
