package org.endeavor.game.entity.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.quest.Quest;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.content.quest.impl.runemysteries.CaveController;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class Questing implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4076141652277937011L;
	private final Player player;
	private final byte[] questStages = new byte[QuestConstants.TOTAL_QUESTS];
	private final List<Quest> activeQuests = new ArrayList<Quest>();

	public Questing(Player player) {
		this.player = player;
	}

	public void onLogin() {
		//player.getClient().queueOutgoingPacket(new SendString("Quests", 682));
		updateQuestPoints();

		//for (int i = 0; i < QuestConstants.QUEST_LINE_IDS.length; i++) {
			//updateQuestLine(i);
		//}

		if ((isQuestActive(QuestConstants.RUNE_MYSTERIES))
				&& ((questStages[QuestConstants.RUNE_MYSTERIES.getId()] == 4) || (questStages[QuestConstants.RUNE_MYSTERIES
						.getId()] == 5)))
			player.setControllerNoInit(new CaveController() {
				@Override
				public boolean canMove(Player p) {
					return true;
				}

				@Override
				public boolean onClick(Player player, int buttonID) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onObject(Player player, int objectID) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onNpc(Player player, int npcID) {
					// TODO Auto-generated method stub
					return false;
				}
			});
	}

	public void updateQuestLine(int i) {
		QuestTab.onLogin(player);
	}

	public boolean clickQuestButton(int id) {
		/*for (int i = 0; i < QuestConstants.QUEST_BUTTONS.length; i++) {
			if (id == QuestConstants.QUEST_BUTTONS[i]) {
				
				  Misc.openBlankQuestDialogue(player);
				  
				  String[] lines =
				  QuestConstants.QUESTS_BY_ID[i].getLinesForStage
				  (questStages[i]);
				  
				  player.getClient().queueOutgoingPacket(new
				  SendString(QuestConstants.QUESTS_BY_ID[i].getName(), 8144));
				  
				  if (lines == null) { return true; }
				  
				  for (int k = 0; k < lines.length; k++) {
				  player.getClient().queueOutgoingPacket(new
				  SendString(lines[k], 8149 + k)); }
				 
				return true;
			}
		}*/

		return false;
	}
	
	public void openQuestPage(int id) {
		 Misc.openBlankQuestDialogue(player);
		  
		  String[] lines =
		  QuestConstants.QUESTS_BY_ID[id].getLinesForStage
		  (questStages[id]);
		  
		  player.getClient().queueOutgoingPacket(new
		  SendString(QuestConstants.QUESTS_BY_ID[id].getName(), 8144));
		  
		  if (lines == null) { return; }
		  
		  for (int k = 0; k < lines.length; k++) {
		  player.getClient().queueOutgoingPacket(new
		  SendString(lines[k], 8149 + k)); }
	}

	public void reset(int id) {
		activeQuests.remove(QuestConstants.QUESTS_BY_ID[id]);
		questStages[id] = 0;
		updateQuestLine(id);
	}

	public void updateQuestPoints() {
		//player.getClient().queueOutgoingPacket(new SendString("Quest Points: " + getQuestPoints(), 640));
	}

	public int getQuestPoints() {
		int points = 0;

		for (int i = 0; i < QuestConstants.TOTAL_QUESTS; i++) {
			if (isQuestCompleted(QuestConstants.QUESTS_BY_ID[i])) {
				points += QuestConstants.QUESTS_BY_ID[i].getPoints();
			}
		}

		return points;
	}

	public byte getQuestStage(Quest quest) {
		return questStages[quest.getId()];
	}

	public void setQuestStage(int quest, int stage) {
		if (stage == 0) {
			activeQuests.remove(QuestConstants.QUESTS_BY_ID[quest]);
		}
		
		questStages[quest] = ((byte) stage);
		
		if (questStages[quest] == QuestConstants.QUESTS_BY_ID[quest].getFinalStage()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("Congratulations, you have completed the quest: " + QuestConstants.QUESTS_BY_ID[quest].getName() + "!"));
			//updateQuestPoints();
			
			if (activeQuests.size() > 0)
				activeQuests.remove(quest);

			player.getClient().queueOutgoingPacket(new SendString(QuestConstants.QUESTS_BY_ID[quest].getPoints() + "", 4444));
			player.getClient().queueOutgoingPacket(new SendString("You've completed " + QuestConstants.QUESTS_BY_ID[quest].getName() + "!", 301));

			player.getClient().queueOutgoingPacket(new SendString(getQuestPoints() + "", 304));
			player.getClient().queueOutgoingPacket(new SendInterface(297));
		} else {
			if (!activeQuests.contains(QuestConstants.QUESTS_BY_ID[quest])) {
				activeQuests.add(QuestConstants.QUESTS_BY_ID[quest]);
			}
		}

		updateQuestLine(QuestConstants.QUESTS_BY_ID[quest].getId());
	}

	public void incrQuestStage(Quest quest) {
		questStages[quest.getId()] += 1;
		
		if (questStages[quest.getId()] > quest.getFinalStage()) {
			Exception e = new Exception("Quest attempt to increment a completed quest!");
			e.printStackTrace();
			questStages[quest.getId()] = quest.getFinalStage();
			return;
		}

		if (questStages[quest.getId()] == quest.getFinalStage()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("Congratulations, you have completed the quest: " + quest.getName() + "!"));
			//updateQuestPoints();
			activeQuests.remove(quest);

			player.getClient().queueOutgoingPacket(new SendString(quest.getPoints() + "", 4444));
			player.getClient().queueOutgoingPacket(new SendString("You've completed " + quest.getName() + "!", 301));

			player.getClient().queueOutgoingPacket(new SendString(getQuestPoints() + "", 304));
			player.getClient().queueOutgoingPacket(new SendInterface(297));
		}

		updateQuestLine(quest.getId());
	}

	public boolean isQuestCompleted(Quest quest) {
		return questStages[quest.getId()] == quest.getFinalStage();
	}

	public void setCompleted(Quest quest) {
		questStages[quest.getId()] = quest.getFinalStage();
		
		if (questStages[quest.getId()] == quest.getFinalStage()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("Congratulations, you have completed the quest: " + quest.getName() + "!"));
			updateQuestPoints();
			activeQuests.remove(quest);

			player.getClient().queueOutgoingPacket(new SendString(quest.getPoints() + "", 4444));
			player.getClient().queueOutgoingPacket(new SendString("You've completed " + quest.getName() + "!", 301));

			player.getClient().queueOutgoingPacket(new SendString(getQuestPoints() + "", 304));
			player.getClient().queueOutgoingPacket(new SendInterface(297));
		}
		
		updateQuestLine(quest.getId());
	}

	public boolean isQuestActive(Quest quest) {
		return activeQuests.contains(quest);
	}

	public void setQuestActive(Quest quest, boolean active) {
		if (active) {
			if (!activeQuests.contains(quest))
				activeQuests.add(quest);
		} else {
			activeQuests.remove(quest);
		}

		updateQuestLine(quest.getId());
	}

	public boolean isClickQuestObject(int option, int id) {
		for (int i = 0; i < activeQuests.size(); i++) {
			Quest k = activeQuests.get(i);
			if (k.clickObject(player, option, id)) {
				return true;
			}
		}

		return false;
	}

	public boolean isUseItemOnQuestObject(int item, int object) {
		for (int i = 0; i < activeQuests.size(); i++) {
			Quest k = activeQuests.get(i);
			if (k.useItemOnObject(player, item, object)) {
				return true;
			}
		}

		return false;
	}

	public byte[] getQuestStages() {
		return questStages;
	}

	public List<Quest> getActiveQuests() {
		return activeQuests;
	}
}
