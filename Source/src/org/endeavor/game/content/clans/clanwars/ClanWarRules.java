package org.endeavor.game.content.clans.clanwars;

import java.util.ArrayList;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

/**
 * 
 * @author Allen K. 
 *
 */
public class ClanWarRules {

	enum CombatWarElements {
		
		MELEE,
		MAGIC, 
		RANGED,
		PRAYER,
		SUMMONING,
		FOOD,
		POTIONS;
		
	}
	
	private int victoryKills;
	private boolean stragglers;
	private int timeLimit;
	private boolean keepItems;
	private ArrayList<CombatWarElements> combatElements;
	private ClanArena arena;
	
	/**
	 * DEFAULT RULES
	 */
	public ClanWarRules() {
		this.victoryKills = 1;
		this.stragglers = false;
		this.timeLimit = 15;
		this.keepItems = true;
		ArrayList<CombatWarElements> temp = new ArrayList<CombatWarElements>();
		temp.add(CombatWarElements.MELEE);
		temp.add(CombatWarElements.MAGIC);
		temp.add(CombatWarElements.RANGED);
		temp.add(CombatWarElements.PRAYER);
		temp.add(CombatWarElements.SUMMONING);
		temp.add(CombatWarElements.FOOD);
		temp.add(CombatWarElements.POTIONS);
		this.combatElements = temp;
		this.arena = ClanArena.CLASSIC;
	}
	
	public ClanWarRules(int victoryKills, boolean stragglers, int timeLimit, boolean keepItems, ArrayList<CombatWarElements> combatElements, ClanArena arena) {
		this.victoryKills = victoryKills;
		this.stragglers = stragglers;
		this.timeLimit = timeLimit;
		this.keepItems = keepItems;
		this.combatElements = combatElements;
		this.arena = arena;
	}
	
	public int getVictoryKills() {
		return victoryKills;
	}
	
	public boolean getStragglers() {
		return stragglers;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
	
	public boolean isKeepItems() {
		return keepItems;
	}
	
	public ArrayList<CombatWarElements> getCombatElements() {
		return combatElements;
	}
	
	public boolean containsCombatElement(CombatWarElements element) {
		return combatElements.contains(element);
	}
	
	public ClanArena getArena() {
		return arena;
	}
	
	public void setVictoryKills(int victoryKills) {
		this.victoryKills = victoryKills;
	}
	
	public void setStragglers(boolean stragglers) {
		this.stragglers = stragglers;
	}
	
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public void setKeepItems(boolean keepItems) {
		this.keepItems = keepItems;
	}
	
	public void switchCombatElement(CombatWarElements element) {
		if(!combatElements.contains(element))
			combatElements.add(element);
		else
			combatElements.remove(element);
	}
	
	public void setArena(ClanArena arena) {
		this.arena = arena;
	}
	
}
