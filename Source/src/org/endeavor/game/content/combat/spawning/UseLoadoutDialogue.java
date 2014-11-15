package org.endeavor.game.content.combat.spawning;

import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendEnterString;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;



public class UseLoadoutDialogue extends Dialogue {
	
	private final int index;
	
	public static final String NEW_LOADOUT_ITEMS_KEY = "newloadoutitemskey";
	public static final String NEW_LOADOUT_INDEX = "newloadoutindexkey";
	
	public UseLoadoutDialogue(Player p, int index) {
		this.player = p;
		this.index = index;
	}

	@Override
	public void execute() {
		if (player.getCustomSpawnLoadouts()[index] == null) {
			player.getAttributes().set(NEW_LOADOUT_INDEX, index);
			
			switch (next) {
			case 0:
				DialogueManager.sendOption(player, "Set to Equipment", "Set to Inventory", "Set to Both", "Nevermind");
				break;
			case 1:
				DialogueManager.sendStatement(player, "Enter a name for your new loadout.");
				next++;
				break;
			case 2:
				player.setEnterXInterfaceId(36471);
				player.send(new SendEnterString());
				end();
				break;
			}
		} else {
			switch (next) {
			case 0:
				DialogueManager.sendOption(player, "Spawn", "Clear");
				break;
			}
		}
	}

	@Override
	public boolean clickButton(int id) {
		if (player.getCustomSpawnLoadouts()[index] == null) {
			switch (id) {
			case DialogueConstants.OPTIONS_4_1:
				Item[] e = player.getEquipment().getItems().clone();
				boolean error = false;
				int items = 0;
				
				/*for (int i = 0; i < e.length; i++) {
					if (e[i] != null && !Spawn.spawnable(e[i].getId())) {
						e[i] = null;
						error = true;
					} else if (e[i] != null) {
						items++;
					}
				}
				
				if (items == 0) {
					DialogueManager.sendStatement(player, "No items were added to your loadout.",
							"Please make sure you have equipment equipped that", "is spawnable.");
					return true;
				}
				
				if (error) {
					player.send(new SendMessage("One of more of your items is not spawnable, and has been excluded.."));
					player.send(new SendMessage(".. from your new custom loadout."));
				}*/
				
				player.getAttributes().set(NEW_LOADOUT_ITEMS_KEY, e);
				
				next = 2;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_2:
				Item[] inv = player.getInventory().getItems().clone();
				boolean error1 = false;
				int items1 = 0;
				
				/*for (int i = 0; i < inv.length; i++) {
					if (inv[i] != null && !Spawn.spawnable(inv[i].getId())) {
						inv[i] = null;
						error1 = true;
					} else if (inv[i] != null) {
						items1++;
					}
				}
				
				if (items1 == 0) {
					DialogueManager.sendStatement(player, "No items were added to your loadout.",
							"Please make sure you have items in your", "iventory that are spawnable.");
					return true;
				}
				
				if (error1) {
					player.send(new SendMessage("One of more of your items is not spawnable, and has been excluded.."));
					player.send(new SendMessage(".. from your new custom loadout."));
				}*/
				
				player.getAttributes().set(NEW_LOADOUT_ITEMS_KEY, inv);
				
				next = 2;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_3:
				Item[] spawn = new Item[50];
				/*int c = 0;
				
				for (Item i : player.getInventory().getItems()) {
					if (i != null && Spawn.spawnable(i.getId())) {
						spawn[c] = new Item(i);
						c++;
					}
				}
				
				for (Item i : player.getEquipment().getItems()) {
					if (i != null && Spawn.spawnable(i.getId())) {
						spawn[c] = new Item(i);
						c++;
					}
				}
				
				if (c == 0) {
					DialogueManager.sendStatement(player, "No items were added to your loadout.",
							"Please make sure you have items that are spawnable.");
					return true;
				}*/
				
				player.getAttributes().set(NEW_LOADOUT_ITEMS_KEY, spawn);
				
				next = 2;
				execute();
				return true;
			case DialogueConstants.OPTIONS_4_4:
				end();
				player.send(new SendRemoveInterfaces());
				return true;
			}
		} else {
			switch (id) {
			case DialogueConstants.OPTIONS_2_1:
				Loadout l = player.getCustomSpawnLoadouts()[index];
				
				Spawn.spawn(player, l.getItems());
				
				end();
				player.send(new SendRemoveInterfaces());
				return true;
			case DialogueConstants.OPTIONS_2_2:
				player.getCustomSpawnLoadouts()[index] = null;

				player.send(new SendString(QuestTab.getLoadoutName(player, 0), QuestTab.QUEST_TAB_LINE_IDS[2]));
				player.send(new SendString(QuestTab.getLoadoutName(player, 1), QuestTab.QUEST_TAB_LINE_IDS[3]));
				player.send(new SendString(QuestTab.getLoadoutName(player, 2), QuestTab.QUEST_TAB_LINE_IDS[4]));
				player.send(new SendString(QuestTab.getLoadoutName(player, 3), QuestTab.QUEST_TAB_LINE_IDS[5]));
				player.send(new SendString(QuestTab.getLoadoutName(player, 4), QuestTab.QUEST_TAB_LINE_IDS[6]));
				player.send(new SendString(QuestTab.getLoadoutName(player, 5), QuestTab.QUEST_TAB_LINE_IDS[7]));
				
				end();
				player.send(new SendRemoveInterfaces());
				return true;
			}
		}
		
		return false;
	}

}
