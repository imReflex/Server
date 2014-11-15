package org.endeavor.game.content;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.spawning.Spawn;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.content.dialogue.impl.SelectTitleDialogue;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.content.skill.Skill;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendEnterString;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class QuestTab {
	public static final String QUEST_TAB_WINDOW = "questtabwindow";
	public static final String QUEST_TAB_PAGE_KEY = "questtabpagekey";
	
	public static final int[] QUEST_TAB_LINE_IDS = { 7332, 7333, 7334, 
		7336, 7383, 7339, 7338, 7340, 7346, 7341, 7342,
		7337, 7343, 7335, 7344, 7345, 7347, 7348, 682, 
		12772, 673, 7352, 17510, 7353,
		12129, 8438, 12852, 15841, 7354,
		7355, 7356, 8679, 7459, 16149,
		6987, 7357, 12836, 7358, 7359, 14169, 10115, 14604,
		7360, 12282, 13577, 12839, 7361, 16128
	};
	
	public static final int
		BUTTON_1 = 28164,
		BUTTON_2 = 28165,
		BUTTON_3 = 28166,
		BUTTON_4 = 28168,
		BUTTON_5 = 28215,
		BUTTON_6 = 28171,
		BUTTON_7 = 28170,
		BUTTON_8 = 28172,
		BUTTON_9 = 28178,
		BUTTON_10 = 28173,
		BUTTON_11 = 28174,
		BUTTON_12 = 28169,
		BUTTON_13 = 28175,
		BUTTON_14 = 28167,
		BUTTON_15 = 28176,
		BUTTON_16 = 28177,
		BUTTON_17 = 28179,
		BUTTON_18 = 28180,
		BUTTON_19 = 2170,
		BUTTON_20 = 49228,
		BUTTON_21 = 2161,
		BUTTON_22 = 28184,
		BUTTON_23 = 68102,
		BUTTON_24 = 28185,
		BUTTON_25 = 47097,
		BUTTON_26 = 32246,
		BUTTON_27 = 50052,
		BUTTON_28 = 61225,
		BUTTON_29 = 28186,
		BUTTON_30 = 28187,
		BUTTON_31 = 28188,
		BUTTON_32 = 33231,
		BUTTON_33 = 29035,
		BUTTON_34 = 63021,
		BUTTON_35 = 27075,
		BUTTON_36 = 28189,
		BUTTON_37 = 50036,
		BUTTON_38 = 28190,
		BUTTON_39 = 28191,
		BUTTON_40 = 55089,
		BUTTON_41 = 39131,
		BUTTON_42 = 57012,
		BUTTON_43 = 28192,
		BUTTON_44 = 47250,
		BUTTON_45 = 53009,
		BUTTON_46 = 50039,
		BUTTON_47 = 28193,
		BUTTON_48 = 63000
		;
	
	public static String getLoadoutName(Player p, int index) {
		if (p.getCustomSpawnLoadouts()[index] == null) {
			return "@or1@Custom Loadout " + (index + 1);
		} else {
			return p.getCustomSpawnLoadouts()[index].getName();
		}
	}

	public static void onLogin(Player p) {
		
		for(int i = 29161; i <= 29268; i++) {
			p.send(new SendString("", i));
		}
		
		int page = 0;
		
		if (p.getAttributes().get(QUEST_TAB_PAGE_KEY) == null) {
			p.getAttributes().set(QUEST_TAB_PAGE_KEY, 0);
		} else {
			page = p.getAttributes().getInt(QUEST_TAB_PAGE_KEY);
		}
		
		for (int i = 0; i < QUEST_TAB_LINE_IDS.length; i++) {
			p.send(new SendString("", QUEST_TAB_LINE_IDS[i]));
		}
		
		if (page == 0) {
		
			p.getAttributes().set(QUEST_TAB_WINDOW, 0);
			
			/**
			 * Header
			 */
			p.send(new SendString("@or2@Home Page", 663));
			
			p.send(new SendString("@gre@Character", QUEST_TAB_LINE_IDS[0]));
			p.send(new SendString("@gre@Combat", QUEST_TAB_LINE_IDS[1]));
			p.send(new SendString("@gre@Spawn", QUEST_TAB_LINE_IDS[2]));
			p.send(new SendString("@gre@Quests", QUEST_TAB_LINE_IDS[3]));
			p.send(new SendString("@gre@Reward Points", QUEST_TAB_LINE_IDS[4]));
			//p.send(new SendString("@gre@How to Play", QUEST_TAB_LINE_IDS[5]));
		} else if (page == 1) {//Character
			/**
			 * Header
			 */
			p.send(new SendString("@or2@Character", 663));
			
			//int premiumDays = !p.hasPremium() ? 0 : p.getPremiumDays() - Misc.getElapsed(p.getPremiumDay(), p.getPremiumYear());

			p.send(new SendString("@or1@Membership: " + "@gre@" + p.getRankName(), QUEST_TAB_LINE_IDS[0]));
			p.send(new SendString("Title: @gre@" + p.getTitle() + " @or1@[Click to set]", QUEST_TAB_LINE_IDS[1]));
			
			/**
			 * Button to Home
			 */
			p.send(new SendString("Back", QUEST_TAB_LINE_IDS[3]));
		} else if (page == 2) {//Combat
			
			/**
			 * Header
			 */
			p.send(new SendString("@or2@Combat", 663));
			
			/**
			 * Exp lock
			 */
			p.send(new SendString("@or1@Exp Status: " + (p.getSkill().isExpLocked() ? "@red@Locked" : "@gre@Unlocked"),
							QUEST_TAB_LINE_IDS[0]));
			
			/**
			 * ::pure
			 */
			p.send(new SendString("Activate ::pure", QUEST_TAB_LINE_IDS[1]));
			
			/**
			 * Slayer task
			 */
			if (p.getSlayer().getTask() == null) {
				p.send(new SendString("@or1@Slayer task: @red@None", QUEST_TAB_LINE_IDS[2]));
			} else {
				String end = p.getSlayer().getAmount() + " " + p.getSlayer().getTask() + "s";
				if (end.length() > 17) {
					end = end.substring(0, 17) + "..";
				}
				p.send(new SendString("@or1@Slayer task: @gre@" + end, QUEST_TAB_LINE_IDS[2]));
			}
			
			/**
			 * Social Slayer
			 */
			Player partner = p.getSlayer().getPartner();

			if (partner == null)
				p.send(new SendString("Social slayer: @red@[Click to set]", QUEST_TAB_LINE_IDS[3]));
			else
				p.send(new SendString("Social slayer: @gre@" + partner.getUsername(), QUEST_TAB_LINE_IDS[3]));
			
			/**
			 * Wild kills and deaths
			 */
			p.send(new SendString("@or1@Wild K/D: " + (p.getKills() >= p.getDeaths() ? "@gre@" : "@red@") 
					+ "" + p.getKills() + "/" + p.getDeaths(), QUEST_TAB_LINE_IDS[4]));
			
			/**
			 * Button to Home
			 */
			p.send(new SendString("Back", QUEST_TAB_LINE_IDS[7]));
			
		} else if (page == 3) {//Spawn
			/**
			 * Header
			 */
			p.send(new SendString("@or2@Spawn", 663));
			
			/**
			 * Spawnables
			 */
			p.send(new SendString("", QUEST_TAB_LINE_IDS[0]));
			
			/**
			 * Make spawn method where it checks that the item has no value
			 * before it can be spawned
			 * 
			 * 
			 */
			p.send(new SendString("@or1@Clear Equipment", QUEST_TAB_LINE_IDS[0]));
			p.send(new SendString("@or1@Clear Inventory", QUEST_TAB_LINE_IDS[1]));
			
			p.send(new SendString(getLoadoutName(p, 0), QUEST_TAB_LINE_IDS[2]));
			p.send(new SendString(getLoadoutName(p, 1), QUEST_TAB_LINE_IDS[3]));
			p.send(new SendString(getLoadoutName(p, 2), QUEST_TAB_LINE_IDS[4]));
			p.send(new SendString(getLoadoutName(p, 3), QUEST_TAB_LINE_IDS[5]));
			p.send(new SendString(getLoadoutName(p, 4), QUEST_TAB_LINE_IDS[6]));
			p.send(new SendString(getLoadoutName(p, 5), QUEST_TAB_LINE_IDS[7]));
			
			p.send(new SendString("@or1@Melee Pure Equipment", QUEST_TAB_LINE_IDS[9]));
			p.send(new SendString("@or1@Ranged Pure Equipment", QUEST_TAB_LINE_IDS[10]));
			p.send(new SendString("@or1@Magic Pure Equipment", QUEST_TAB_LINE_IDS[11]));
			p.send(new SendString("@or1@Hybrid Pure Equipment", QUEST_TAB_LINE_IDS[12]));
			p.send(new SendString("@or1@Initiate Pure Equipment", QUEST_TAB_LINE_IDS[13]));
			p.send(new SendString("@or1@Mystic Magic Equipment", QUEST_TAB_LINE_IDS[14]));
			p.send(new SendString("@or1@Mystic Melee Equipment", QUEST_TAB_LINE_IDS[15]));
			p.send(new SendString("@or1@Mystic Ranged Equipment", QUEST_TAB_LINE_IDS[16]));
			
			p.send(new SendString("@or1@Iron Armour Set", QUEST_TAB_LINE_IDS[18]));
			p.send(new SendString("@or1@Initiate Armour Set", QUEST_TAB_LINE_IDS[19]));
			p.send(new SendString("@or1@Adamant Armour Set", QUEST_TAB_LINE_IDS[20]));
			p.send(new SendString("@or1@Mystic Armour Set", QUEST_TAB_LINE_IDS[21]));
			p.send(new SendString("@or1@Black d'hide Set", QUEST_TAB_LINE_IDS[22]));
			p.send(new SendString("@or1@Ghostly Robes Set", QUEST_TAB_LINE_IDS[23]));
			
			p.send(new SendString("@or1@Melee Weapons", QUEST_TAB_LINE_IDS[25]));
			p.send(new SendString("@or1@Ranged Weapons", QUEST_TAB_LINE_IDS[26]));
			p.send(new SendString("@or1@Magic Weapons", QUEST_TAB_LINE_IDS[27]));
			p.send(new SendString("@or1@Ammo", QUEST_TAB_LINE_IDS[28]));
			p.send(new SendString("@or1@Dragon dagger", QUEST_TAB_LINE_IDS[29]));
			p.send(new SendString("@or1@Magic shortbow", QUEST_TAB_LINE_IDS[30]));
			p.send(new SendString("@or1@Amulets", QUEST_TAB_LINE_IDS[31]));
			p.send(new SendString("@or1@Climbing Boots", QUEST_TAB_LINE_IDS[32]));
			p.send(new SendString("@or1@Adamant Darts", QUEST_TAB_LINE_IDS[33]));
			p.send(new SendString("@or1@Rune Crossbow", QUEST_TAB_LINE_IDS[34]));
			p.send(new SendString("@or1@Ancient Staff", QUEST_TAB_LINE_IDS[35]));
			p.send(new SendString("@or1@Granite Maul", QUEST_TAB_LINE_IDS[36]));
			p.send(new SendString("@or1@Obby Maul", QUEST_TAB_LINE_IDS[37]));
			p.send(new SendString("@or1@Adamant Gloves", QUEST_TAB_LINE_IDS[38]));
			p.send(new SendString("@or1@Unholy Book", QUEST_TAB_LINE_IDS[39]));
			
			
			/**
			 * Button to Home
			 */
			p.send(new SendString("Back", QUEST_TAB_LINE_IDS[41]));
		} else if (page == 4) {//Quests
			/**
			 * Header
			 */
			p.send(new SendString("@or2@Quests", 663));
			
			
			/**
			 * Quests
			 */
			p.send(new SendString((p.getQuesting().isQuestActive(QuestConstants.QUESTS_BY_ID[0]) ? "@yel@"
							: p.getQuesting().isQuestCompleted(QuestConstants.QUESTS_BY_ID[0]) ? "@gre@" : "@red@")
							+ QuestConstants.QUESTS_BY_ID[0].getName(), 
							QUEST_TAB_LINE_IDS[0]));
			
			p.send(new SendString((p.getQuesting().isQuestActive(QuestConstants.QUESTS_BY_ID[1]) ? "@yel@"
					: p.getQuesting().isQuestCompleted(QuestConstants.QUESTS_BY_ID[1]) ? "@gre@" : "@red@")
					+ QuestConstants.QUESTS_BY_ID[1].getName(), 
					QUEST_TAB_LINE_IDS[1]));
			
			p.send(new SendString((p.getQuesting().isQuestActive(QuestConstants.QUESTS_BY_ID[2]) ? "@yel@"
					: p.getQuesting().isQuestCompleted(QuestConstants.QUESTS_BY_ID[2]) ? "@gre@" : "@red@")
					+ QuestConstants.QUESTS_BY_ID[2].getName(), 
					QUEST_TAB_LINE_IDS[2]));
			
			/**
			 * Button to Home
			 */
			p.send(new SendString("Back", QUEST_TAB_LINE_IDS[4]));
			
		} else if (page == 5) {//Reward Points
			/**
			 * Header
			 */
			p.send(new SendString("@or2@Reward Points", 663));
			
			/**
			 * Slayer points
			 */
			p.send(new SendString("@or1@Slayer Points: " + (p.getSlayerPoints() > 0 ? "@gre@" : "@red@")
							+ p.getSlayerPoints(), QUEST_TAB_LINE_IDS[1]));
			
			/**
			 * Dung points
			 */
			p.send(new SendString("@or1@Dung Points: " + (p.getDungPoints() == 0 ? "@red@" : "@gre@") + p.getDungPoints(),
							QUEST_TAB_LINE_IDS[2]));
		
		
			/**
			 * Skill points
			 */
			/*p.send(new SendString("@or1@Skill points: " + (p.getSkillPoints() == 0 ? "@red@" : "@gre@")
							+ p.getSkillPoints(), QUEST_TAB_LINE_IDS[4]));*/
		
			/**
			 * Pest points
			 */
			p.send(new SendString("@or1@Pest Points: " + (p.getPestPoints() == 0 ? "@red@" : "@gre@")
							+ p.getPestPoints(), QUEST_TAB_LINE_IDS[3]));
		
			/**
			 * Pk points
			 */
			p.send(new SendString("@or1@Pk Points: " + (p.getEarningPotential().getPkp() == 0 ? "@red@" : "@gre@")
							+ p.getEarningPotential().getPkp(), QUEST_TAB_LINE_IDS[4]));
			
			/**
			 * Button to Home
			 */
			p.send(new SendString("Back", QUEST_TAB_LINE_IDS[8]));
			
			
		} else if (page == 6) {//How to play
			/**
			 * Header
			 */
			p.send(new SendString("@or2@How to Play", 663));
			
			/**
			 * Buttons
			 */
			
			p.send(new SendString("Getting Started", QUEST_TAB_LINE_IDS[0]));
			p.send(new SendString("Premium Membership", QUEST_TAB_LINE_IDS[1]));
			p.send(new SendString("Donating", QUEST_TAB_LINE_IDS[2]));
			p.send(new SendString("Voting", QUEST_TAB_LINE_IDS[3]));
			p.send(new SendString("Community Rules", QUEST_TAB_LINE_IDS[4]));
			p.send(new SendString("Premium Membership", QUEST_TAB_LINE_IDS[5]));
			p.send(new SendString("Player-owned Shops", QUEST_TAB_LINE_IDS[6]));
			p.send(new SendString("Client Settings", QUEST_TAB_LINE_IDS[7]));
			p.send(new SendString("Player-owned Shops", QUEST_TAB_LINE_IDS[8]));
			p.send(new SendString("Spawning", QUEST_TAB_LINE_IDS[9]));
			
			/**
			 * Button to Home
			 */
			p.send(new SendString("Back", QUEST_TAB_LINE_IDS[11]));
			
		}
		/* QuestTab.updateSlayerTask(p);
			QuestTab.updateExpLock(p);
			QuestTab.updateKDRDisplay(p);
			QuestTab.updateEP(p);
			QuestTab.updatePKP(p);
			QuestTab.updateSocialSlayer(p);
			QuestTab.updateDungPoints(p);
			QuestTab.updateVotePoints(p);
			QuestTab.updateSlayerPoints(p);
			QuestTab.updateDonatorPoints(p);
			QuestTab.updateSkillPoints(p);
			QuestTab.updatePremium(p);
			QuestTab.updatePestPoints(p);
			QuestTab.updateTitle(p);*/
	}
	
	public static void setTabPage(Player p, int page) {
		p.getAttributes().set(QUEST_TAB_PAGE_KEY, page);
		onLogin(p);
		p.send(new SendConfig(699, 0));
	}
	
	public static boolean clickButton(Player p, int id) {
		int page = p.getAttributes().getInt(QUEST_TAB_PAGE_KEY);
		
		switch (page) {
		case 0://home directory
			switch (id) {
			case BUTTON_1:
				setTabPage(p, 1);
				return true;
			case BUTTON_2:
				setTabPage(p, 2);
				return true;
			case BUTTON_3:
				setTabPage(p, 3);
				return true;
			case BUTTON_4:
				setTabPage(p, 4);
				return true;
			case BUTTON_5:
				setTabPage(p, 5);
				return true;
			case BUTTON_6:
				//setTabPage(p, 6);
				return true;
			}
			break;
		case 1:
			switch (id) {
			case BUTTON_4:
				setTabPage(p, 0);
				return true;
			case BUTTON_2:
				p.start(new SelectTitleDialogue(p));
				return true;
			}
			break;
		case 2:
			switch (id) {
			case BUTTON_2:
				p.start(new ConfirmDialogue(p, new String[] {"This will replace your current combat stats.", 
						"Any Prayer or Defence levels will be reset.", "Are you sure you want to do this?"}) {
					public void onConfirm() {
						if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
							player.getClient().queueOutgoingPacket(new SendMessage("You can't use this here."));
							return;
						}
			
						if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
							player.getClient().queueOutgoingPacket(new SendMessage("You cannot do this here."));
							return;
						}
			
						if (ItemCheck.hasEquipmentOn(player)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("You must remove your equipment to do this command."));
							return;
						}
			
						player.getSkill().getExperience()[1] = 0.0D;
						player.getLevels()[1] = 1;
						player.getMaxLevels()[1] = 1;
						player.getSkill().update(1);
			
						player.getSkill().getExperience()[5] = Skill.EXP_FOR_LEVEL[50];
						player.getLevels()[5] = 52;
						player.getMaxLevels()[5] = 52;
						player.getSkill().update(5);
			
						for (int i = 0; i <= 6; i++) {
							if ((i != 1) && (i != 5)) {
								player.getSkill().getExperience()[i] = Skill.EXP_FOR_LEVEL[99];
								player.getLevels()[i] = 99;
								player.getMaxLevels()[i] = 99;
								player.getSkill().update(i);
							}
						}
						player.getSkill().updateCombatLevel();
					}
				});
				return true;
			case BUTTON_4:
				p.setEnterXInterfaceId(100);
				p.getClient().queueOutgoingPacket(new SendEnterString());
				return true;
			
			case BUTTON_8:
				setTabPage(p, 0);
				return true;
			}
			break;
		case 3:
			if (Spawn.clickButton(p, id)) {
				return true;
			}
			
			switch (id) {
			case BUTTON_42:
				setTabPage(p, 0);
				return true;
			}
			break;
		case 4:
			switch (id) {
			case BUTTON_1:
				p.getQuesting().openQuestPage(0);
				return true;
			case BUTTON_2:
				p.getQuesting().openQuestPage(1);
				return true;
			case BUTTON_3:
				p.getQuesting().openQuestPage(2);
				return true;
			
			case BUTTON_5:
				setTabPage(p, 0);
				return true;
			}
			break;
		case 5:
			switch (id) {
			case BUTTON_9:
				setTabPage(p, 0);
				return true;
			}
			break;
		case 6:
			switch (id) {
			case BUTTON_12:
				setTabPage(p, 0);
				return true;
			}
			break;
		case 7:
			
			break;
		case 8:
			
			break;
		}
		
		return false;
	}

	public static void updateSlayerTask(Player p) {
		onLogin(p);
	}

	public static void updateSlayerPoints(Player p) {
		onLogin(p);
	}

	public static void updateSocialSlayer(Player p) {
		onLogin(p);
	}

	public static void updatePremium(Player p) {
		onLogin(p);
	}

	public static void updateExpLock(Player p) {
		onLogin(p);
	}
	
	public static void updateTitle(Player p) {
		onLogin(p);
	}

	public static void updateDungPoints(Player p) {
		onLogin(p);
	}

	public static void updateVotePoints(Player p) {
		onLogin(p);
	}

	public static void updateDonatorPoints(Player p) {
		onLogin(p);
	}

	public static void updateSkillPoints(Player p) {
		onLogin(p);
	}
	
	public static void updatePestPoints(Player p) {
		onLogin(p);
	}

	public static void updatePKP(Player p) {
		onLogin(p);
	}

	public static void updateEP(Player p) {
		onLogin(p);
	}

	public static void updateKDRDisplay(Player p) {
		onLogin(p);
	}

	public static void updatePlayersOnline(Player p, int amount) {
		p.send(new SendString("@or1@Players Online: @gre@" + amount, 640));
	}
}
