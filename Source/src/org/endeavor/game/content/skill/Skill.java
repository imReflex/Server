package org.endeavor.game.content.skill;

import static org.endeavor.game.content.skill.SkillConstants.*;
import java.io.Serializable;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendQuickSong;
import org.endeavor.game.entity.player.net.out.impl.SendSkill;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/**
 * Handles all skill functionality
 * 
 * @author Michael Sasse
 * 
 */
public class Skill implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7209227586033943743L;

	public static final int[] EXP_FOR_LEVEL = new int[125];

	private double[] experience = new double[25];
	private int combatLevel = 0;
	private int totalLevel = 0;
	private transient Player player;
	private boolean expLock = false;

	private long lock = 0L;

	/**
	 * Declares a table to get the experience for a level
	 */
	public static final void declare() {
		int i = 0;
		for (int j = 0; j < EXP_FOR_LEVEL.length; j++) {
			int l = j + 1;
			int i1 = (int) (l + 300.0D * Math.pow(2.0D, l / 7.0D));
			i += i1;
			EXP_FOR_LEVEL[j] = (i / 4);
		}
	}

	/**
	 * Constructs a new skill instance
	 * 
	 * @param player
	 *            The player creating the skill instance
	 */
	public Skill(Player player) {
		this.player = player;
		for (int i = 0; i < 25; i++)
			if (i == 3) {
				getLevels()[i] = 10;
				experience[i] = 1154.0D;
			} else {
				getLevels()[i] = 1;
				experience[i] = 0.0D;
			}
	}

	/**
	 * Resets the players skill back to default
	 * 
	 * @param id
	 *            The id of the skill to reset
	 */
	public void reset(int id) {
		if (id == 3) {
			getLevels()[id] = 10;
			experience[id] = 1154.0D;
			player.getMaxLevels()[id] = 10;
		} else {
			getLevels()[id] = 1;
			player.getMaxLevels()[id] = 1;
			experience[id] = 0.0D;
		}

		update(id);
		updateCombatLevel();
	}

	/**
	 * Gets the players total experience
	 * 
	 * @return The players total experience
	 */
	public long getTotalExperience() {
		long total = 0L;

		for (double i : experience) {
			total = (long) (total + (i > 200000000.0D ? 200000000.0D : i));
		}

		return total;
	}

	/**
	 * Gets the total amount of combat experience
	 * 
	 * @return The total amount of combat experience
	 */
	public long getTotalCombatExperience() {
		long total = 0L;

		for (int i = 0; i <= 6; i++) {
			total = (long) (total + experience[i]);
		}

		return total;
	}

	/**
	 * Updates the skills on login
	 */
	public void onLogin() {
		updateLevelsForExperience();
		updateCombatLevel();
		updateTotalLevel();
		//updateOrbs();

		for (int i = 0; i < 25; i++) {
			update(i);
		}

		player.getAchievements().set(player, "Achieve 138 combat", combatLevel + getSummoningLevelAddon(), true);
		player.getAchievements().set(player, "Reach 1,500 Total level", totalLevel, true);
		player.getAchievements().set(player, "Reach 2,000 Total level", totalLevel, true);
	}
	
	/**
	 * Deducts an amount from a skill
	 * 
	 * @param id
	 *            The id of the skill
	 * @param amount
	 *            The amount to remove from the skill
	 */
	public void deductFromLevel(int id, int amount) {
		getLevels()[id] = ((short) (getLevels()[id] - amount));

		if (getLevels()[id] < 0) {
			getLevels()[id] = 0;
		}

		update(id);
	}

	/**
	 * Sets a level by the id
	 * 
	 * @param id
	 *            The id of the skill
	 * @param level
	 *            The level to set the skill too
	 */
	public void setLevel(int id, int level) {
		getLevels()[id] = ((byte) level);
		update(id);
	}

	/**
	 * Gets the amount of experience for a level
	 * 
	 * @param skillId
	 *            The id of the skill
	 * @param level
	 *            The level the player is getting the experience for
	 * @return The amount of experience for a level
	 */
	public int getXPForLevel(int skillId, int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points = (int) (points + Math.floor(lvl + 300.0D * Math.pow(2.0D, lvl / 7.0D)));
			if ((lvl >= level) || (lvl == 99) && skillId != DUNGEONEERING || (lvl == 120) && skillId == DUNGEONEERING)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Updates all of the players skills
	 */
	public void update() {
		for (int i = 0; i < 25; i++)
			update(i);
	}

	/**
	 * Gets if the player has at least 2 99 skills
	 * 
	 * @return The player has at least 2 99's
	 */
	public boolean hasTwo99s() {
		byte c = 0;
		int index = 0;
		
		for (int i : player.getMaxLevels()) {
			if ((i == 99) && index != SkillConstants.DUNGEONEERING || i == 120 && index == SkillConstants.DUNGEONEERING) {
				if ((c = (byte) (c + 1)) == 2) {
					return true;
				}
			}
			
			index++;
		}

		return false;
	}

	/**
	 * Updates a skill by the id
	 * 
	 * @param id
	 *            The id of the skill being updated
	 */
	public void update(int id) {
		/*if ((id == 23) || (id == 22)) {
			return;
		}*/
		
		if (id == PRAYER) {
			player.send(new SendString("" + player.getLevels()[PRAYER] + "/" + player.getMaxLevels()[PRAYER], 687));
		}

		if (id == 21) {
			player.getClient().queueOutgoingPacket(new SendString(player.getLevels()[21] + "/" + player.getMaxLevels()[21], 18045));
			//player.getClient().queueOutgoingPacket(new SendSkill(21, getLevels()[21], (int) experience[21]));
			//return;
		}

		/** Manually upate these skills... **/
		player.getClient().queueOutgoingPacket(new SendString(player.getLevels()[21] + "", 18165));
		player.getClient().queueOutgoingPacket(new SendString(player.getMaxLevels()[21] + "", 18169));
		player.getClient().queueOutgoingPacket(new SendString(player.getLevels()[22] + "", 18166));
		player.getClient().queueOutgoingPacket(new SendString(player.getMaxLevels()[22] + "", 18170));
		player.getClient().queueOutgoingPacket(new SendString(player.getLevels()[23] + "", 18167));
		player.getClient().queueOutgoingPacket(new SendString(player.getMaxLevels()[23] + "", 18171));
		player.getClient().queueOutgoingPacket(new SendString(player.getLevels()[24] + "", 18168));
		player.getClient().queueOutgoingPacket(new SendString(player.getMaxLevels()[24] + "", 18172));
		
		/*if (id == 24) {
			player.getClient().queueOutgoingPacket(new SendSkill(24, getLevels()[24], (int) experience[24]));
			return;
		}*/

		player.getClient().queueOutgoingPacket(new SendSkill(id, getLevels()[id], (int) experience[id]));
	}

	/*public void updateOrbs() {
		player.send(new SendString("" +player.getLevels()[PRAYER] + "", SkillConstants.CURRENT_PRAYER_UPDATE_ID));
		player.send(new SendString("" +player.getMaxLevels()[PRAYER] + "", SkillConstants.MAX_PRAYER_UPDATE_ID));
		
		player.send(new SendString("" +player.getLevels()[HITPOINTS] + "", SkillConstants.CURRENT_HEALTH_UPDATE_ID));
	}*/
	
	/**
	 * Restores the players levels back to normal
	 */
	public void restore() {
		for (int i = 0; i < 25; i++) {
			getLevels()[i] = player.getMaxLevels()[i];
			update(i);
		}
	}

	/**
	 * Resets the players combat stats, attack through magic
	 */
	public void resetCombatStats() {
		for (int i = 0; i <= 7; i++) {
			getLevels()[i] = player.getMaxLevels()[i];
			update(i);
		}
	}

	/**
	 * Adds combat experience after dealing damage
	 * 
	 * @param type
	 *            The type of combat the player dealt damage with
	 * @param hit
	 *            The amount of damage dealt
	 */
	public void addCombatExperience(CombatTypes type, int hit) {
		if ((expLock) || (player.getMagic().isDFireShieldEffect())
				|| (player.getController().equals(ARConstants.AR_CONTROLLER))) {
			return;
		}

		if (type == CombatTypes.MAGIC) {
			player.getMagic().getSpellCasting().addSpellExperience();
		}

		if (hit <= 0) {
			return;
		}

		double exp = hit * 4.0D;
		switch (type) {
		case MELEE:
			switch (player.getEquipment().getAttackStyle()) {
			case ACCURATE:
				addExperience(0, exp);
				break;
			case AGGRESSIVE:
				addExperience(2, exp);
				break;
			case CONTROLLED:
				addExperience(0, exp / 3.0D);
				addExperience(2, exp / 3.0D);
				addExperience(1, exp / 3.0D);
				break;
			case DEFENSIVE:
				addExperience(1, exp);
				break;
			}

			break;
		case MAGIC:
			addExperience(6, exp);
			break;
		case RANGED:
			addExperience(4, exp);
			switch (player.getEquipment().getAttackStyle()) {
			case ACCURATE:
				addExperience(4, exp);
				break;
			case AGGRESSIVE:
				addExperience(4, exp);
				break;
			case CONTROLLED:
				addExperience(4, exp);
				break;
			case DEFENSIVE:
				addExperience(4, exp / 2.0D);
				addExperience(1, exp / 2.0D);
				break;
			}
			break;
		}

		addExperience(3, hit * 1.33D);
	}

	/**
	 * Gets if the player has a combat levels
	 * @return
	 */
	public boolean hasCombatLevels() {
		for (int i = 0; i <= 6; i++) {
			if ((i == 3) && (player.getMaxLevels()[i] > 10)) {
				return true;
			}

			if (player.getMaxLevels()[i] > 1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds experience to the experience array
	 * 
	 * @param id
	 *            The id of the skill to add experience too
	 * @param experience
	 *            The amount of experience too add
	 * @return
	 */
	public double addExperience(int id, double experience) {
		if ((expLock) && (id <= 6) || player.getController().equals(ARConstants.AR_CONTROLLER)) {
			return 0;
		}

		experience = experience * SkillConstants.EXPERIENCE_RATES[id] * (GameConstants.IS_DOUBLE_EXP_WEEKEND ? 2.0D : 1.0D);

		this.experience[id] += experience;

		if (player.getMaxLevels()[id] == 99 && id != DUNGEONEERING || player.getMaxLevels()[id] == 120 && id == DUNGEONEERING) {
			if (this.experience[id] >= 200000000) {
				this.experience[id] = 200000000;
			} else {
				//player.getClient().queueOutgoingPacket(new SendUpdateExperience(id, experience));
			}

			update(id);
			return experience;
		}

		//player.getClient().queueOutgoingPacket(new SendUpdateExperience(id, experience));

		int newLevel = getLevelForExperience(id, this.experience[id]);
		
		if (newLevel > 99 && id != DUNGEONEERING) {
			newLevel = 99;
		}
		
		if (newLevel > 120 && id == DUNGEONEERING) {
			newLevel = 120;
		}

		if (player.getMaxLevels()[id] < newLevel) {
			getLevels()[id] = ((short) (newLevel - (player.getMaxLevels()[id] - getLevels()[id])));
			player.getMaxLevels()[id] = ((short) (newLevel));

			updateTotalLevel();

			if ((id == 0) || (id == 2) || (id == 1) || (id == 3) || (id == 4) || (id == 6) || (id == 5) || id == SkillConstants.SUMMONING) {
				updateCombatLevel();
			}

			onLevelup(newLevel, id);

			if (id == DUNGEONEERING ? newLevel == 120 : newLevel == 99 && id > 6) {
				World.sendGlobalMessage(player.getUsername() + " has achieved level " + newLevel + " in " + SkillConstants.SKILL_NAMES[id] + ". Congratulations!", true);
			}
		}

		if (this.experience[id] >= 200000000) {
			this.experience[id] = 200000000;
		}
		
		update(id);
		return experience;
	}

	/**
	 * Actions that take place on leveling up
	 * 
	 * @param lvl
	 *            The level the player has just achieved
	 * @param skill
	 *            The skill the player achieved the level in
	 */
	public void onLevelup(int lvl, int skill) {
		player.getUpdateFlags().sendGraphic(SkillConstants.LEVELUP_GRAPHIC);

		if (lvl >= 70) {
			String line1 = "Congratulations! You've just advanced " + Misc.getAOrAn(SkillConstants.SKILL_NAMES[skill]) + " " + SkillConstants.SKILL_NAMES[skill] + " level!";
			String line2 = "You have reached level " + lvl + "!";

			player.getClient().queueOutgoingPacket(new SendMessage(line1));
			player.getClient().queueOutgoingPacket(new SendMessage(line2));

			if (lvl < 99)
				player.getClient().queueOutgoingPacket(new SendQuickSong(SkillConstants.LEVEL_UP_SOUNDS[Misc.randomNumber(SkillConstants.LEVEL_UP_SOUNDS.length)], 1));
			else {
				player.getClient().queueOutgoingPacket(new SendQuickSong(99, 2));
			}

			if (lvl <= 20) {
				player.getClient().queueOutgoingPacket(new SendChatInterface(SkillConstants.CHAT_INTERFACES[skill][1]));

				if ((skill != 4) && (skill != 14) && (skill != 17) && (skill != 19)) {
					player.getClient().queueOutgoingPacket(new SendString(line1, SkillConstants.CHAT_INTERFACES[skill][1] + 1));
					player.getClient().queueOutgoingPacket(
							new SendString(line2, SkillConstants.CHAT_INTERFACES[skill][1] + 2));
				} else {
					player.getClient().queueOutgoingPacket(new SendString(line1, SkillConstants.CHAT_INTERFACES[skill][2]));
					player.getClient().queueOutgoingPacket(new SendString(line2, SkillConstants.CHAT_INTERFACES[skill][3]));
				}
			}
		}
	}

	/**
	 * Updates all of the levels for the players experience
	 */
	public void updateLevelsForExperience() {
		for (int i = 0; i < 25; i++)
			player.getMaxLevels()[i] = getLevelForExperience(i, experience[i]);
	}

	/**
	 * Updates the total level
	 */
	public void updateTotalLevel() {
		totalLevel = 0;
		
		for (int i = 0; i < 25; i++) {
			totalLevel += player.getMaxLevels()[i];
		}

		player.getAchievements().set(player, "Reach 1,500 Total level", totalLevel, true);
		player.getAchievements().set(player, "Reach 2,000 Total level", totalLevel, true);
	}
	
	/**
	 * Gets the summoning combat level addition
	 * @return
	 */
	public int getSummoningLevelAddon() {
		return (int) (((player.getMaxLevels()[1] + player.getMaxLevels()[3] + Math.floor(player.getMaxLevels()[5] / 2) + Math.floor(player.getMaxLevels()[SkillConstants.SUMMONING] / 2)) * 0.25D)
				- ((player.getMaxLevels()[1] + player.getMaxLevels()[3] + Math.floor(player.getMaxLevels()[5] / 2)) * 0.25D));
	}

	/**
	 * Updates the players combat level
	 */
	public void updateCombatLevel() {
		int s = this.combatLevel;

		double combatLevel = (player.getMaxLevels()[1] + player.getMaxLevels()[3] + Math.floor(player.getMaxLevels()[5] / 2)) * 0.25D;

		double warrior = (player.getMaxLevels()[0] + player.getMaxLevels()[2]) * 0.325D;

		double ranger = player.getMaxLevels()[4] * 0.4875D;

		double mage = player.getMaxLevels()[6] * 0.4875D;

		this.combatLevel = ((int) (combatLevel + Math.max(warrior, Math.max(ranger, mage))));

		int toDisplay = this.combatLevel + getSummoningLevelAddon();
		if (s != combatLevel) {
			player.getAchievements().set(player, "Achieve 138 combat", this.combatLevel + getSummoningLevelAddon(), true);
			player.setAppearanceUpdateRequired(true);
			player.getClient().queueOutgoingPacket(new SendString("Combat Level: " + toDisplay, 19000));
		}
	}

	/**
	 * Gets a level based on the amount of experience provided
	 * 
	 * @param id
	 *            The skill to check the level for
	 * @param experience
	 *            The amount of experience to check for a level
	 * @return The level based on the provided experience
	 */
	public byte getLevelForExperience(final int id, double experience) {
		byte lvl = 0;
		int i = 0;
		for (int j = 0; j <= 121; j++) {
			int l = j + 1;
			int i1 = (int) (l + 300.0D * Math.pow(2.0D, l / 7.0D));
			i += i1;
			int xp = i / 4;

			if (xp <= experience) {
				lvl = (byte) (j + 2);
			}
		}

		return (id != DUNGEONEERING ? lvl > 99 : lvl > 120) ? (byte) (id != DUNGEONEERING ? 99 : 120) : lvl == 0 ? 1 : lvl;
	}

	/**
	 * Locks the players skill from performing another skill for a certain delay
	 * 
	 * @param delay
	 *            The delay to lock the skills
	 */
	public void lock(int delay) {
		lock = (World.getCycles() + delay);
	}

	/**
	 * Gets if the players skill is locked
	 * 
	 * @return The players skills are locked
	 */
	public boolean locked() {
		return lock > World.getCycles();
	}

	/**
	 * Gets if the experience is locked
	 * 
	 * @return The experience is locked
	 */
	public boolean isExpLocked() {
		return expLock;
	}

	/**
	 * Sets the experience locked or unlocked
	 * 
	 * @param locked
	 *            If he experience is locked or unlocked
	 */
	public void setExpLock(boolean locked) {
		expLock = locked;
	}

	/**
	 * Gets the players levels
	 * 
	 * @return
	 */
	public short[] getLevels() {
		return player.getLevels();
	}

	/**
	 * Sets the players current experience
	 * 
	 * @param experience
	 */
	public void setExperience(double[] experience) {
		this.experience = experience;
	}

	/**
	 * Gets the players current experience
	 * 
	 * @return
	 */
	public double[] getExperience() {
		return experience;
	}

	/**
	 * Gets the players combat levels
	 * 
	 * @return
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Gets the players total level
	 * 
	 * @return
	 */
	public int getTotalLevel() {
		return totalLevel;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
