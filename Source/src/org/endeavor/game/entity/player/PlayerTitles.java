package org.endeavor.game.entity.player;

import java.io.Serializable;

import java.util.ArrayList;

import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PlayerTitles implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4686194286778414710L;

	private transient Player player;
	
	private final ArrayList<String> titlesAvailable = new ArrayList<String>();
	
	public PlayerTitles(Player player) {
		this.player = player;
	}
	
	public void checkForNewAchievmentTitles() {
		int completed = player.getAchievements().getCompletedAchievmentAmount();
		
		if (player.getRights() >= 1 && player.getRights() <= 2) {
			addTitle("Donator");
		}
		
		if (completed >= 5) {
			addTitle("Sir");
		}
		
		if (completed >= 20) {
			addTitle("Duke");
		}
		
		if (completed >= 30) {
			addTitle("Prince");
		}
		
		if (completed >= 40) {
			addTitle("King");
		}
		
		if (completed >= Achievements.getAchievementAmount() && QuestConstants.hasCompletedAllQuests(player)) {
			addTitle("Emperor");
		}
		
		if (player.getAchievements().isCompleted("Cast 10,000 spells")) {
			addTitle("Divine");
		}
		
		if (player.getAchievements().isCompleted("Defeat 500 monsters")) {
			addTitle("Warrior");
		}
		
		if (player.getAchievements().isCompleted("Create 500 potions")) {
			addTitle("Herbalist");
		}
		
		if (player.getAchievements().isCompleted("Fish 1,000 items")) {
			addTitle("Fisherman");
		}
		
		if (player.getAchievements().isCompleted("Reach 1,500 Total level")
				&& player.getSkill().getCombatLevel() == 3) {
			addTitle("Skiller");
		}
		
		if (player.getAchievements().isCompleted("Achieve 138 combat")) {
			addTitle("Magnificent");
		}
		
		if (player.getAchievements().isCompleted("Use 1,000 consumables")) {
			addTitle("Survivor");
		}
		
		if (player.getAchievements().isCompleted("Mine 500 ores")) {
			addTitle("Miner");
		}
		
		if (player.getAchievements().isCompleted("Harvest 500 crops")) {
			addTitle("Farmer");
		}
		
		if (player.getAchievements().isCompleted("Complete 50 Slayer tasks")) {
			addTitle("Powerful");
		}
		
		if (player.getAchievements().isCompleted("Kill 50 Corporeal beasts")) {
			addTitle("Corporeal");
		}
		
		if (player.getAchievements().isCompleted("Achieve 1,000 total GWD KC")) {
			addTitle("Warbeast");
		}
		
		if (player.getAchievements().isCompleted("Bury 5,000 bones")) {
			addTitle("The Holy");
		}
		
		if (player.getAchievements().isCompleted("Cast 10,000 spells")) {
			addTitle("Magical");
		}
		
		if (player.getAchievements().isCompleted("Win 100 Duels")) {
			addTitle("Champion");
		}
		
		if (player.getAchievements().isCompleted("Equip the Max cape")
				&& QuestConstants.hasCompletedAllQuests(player) && player.getAchievements().getCompletedAchievmentAmount()
					== Achievements.getAchievementAmount()) {
			addTitle("Overlord");
		}
	}
	
	public void checkForNewPKAchievments() {
		int kills = player.getKills();
		
		if (kills >= 5) {
			addTitle("Junior Cadet");
		}
		
		if (kills >= 15) {
			addTitle("Soldier");
		}
		
		if (kills >= 35) {
			addTitle("Serjeant");
		}
		
		if (kills >= 40) {
			addTitle("Commander");
		}
		
		if (kills >= 50) {
			addTitle("War-chief");
		}
		
		if (kills >= 75) {
			addTitle("Assassin");
		}
		
		if (kills >= 125) {
			addTitle("Warmonger");
		}
	}
	
	public void addTitle(String title) {
		if (!titlesAvailable.contains(title)) {
			player.getClient().queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You've unlocked a new title: " + "'" + title + "'" + "!"));
			titlesAvailable.add(title);
		}
	}
	
	public String[] getAvailableTitlesForSelection() {
		if (titlesAvailable.size() == 0) {
			return new String[] { PlayerConstants.isOwner(player) || player.getRights() >= 3 ? "Staff" : "Noob" };
		}
		
		String[] titles = new String[titlesAvailable.size() + 1];
		
		int index = 0;
		titles[index] = PlayerConstants.isOwner(player) || player.getRights() >= 3 ? "Staff" : "Noob";
		
		for (String i : titlesAvailable) {
			index++;
			titles[index] = i;
		}
		
		return titles;
	}
	
	public String[] getTitles() {
		/*if (PlayerConstants.isOwner(player) && GameSettings.DEV_MODE) {
			return null;
		}
		*/
		
		if (titlesAvailable.size() == 0) {
			return null;
		}
		
		String[] titles = new String[titlesAvailable.size()];
		
		for (int i = 0; i < titles.length; i++) {
			titles[i] = titlesAvailable.get(i);
		}
		
		return titles;
	}

	public void addTitlesFromArray(String[] titles) {
		for (String i : titles) {
			if (i != null && !titlesAvailable.contains(i)) {
				titlesAvailable.add(i);
			}
		}
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
